package com.okayboom.particlesim.basicsim;

import java.util.List;
import java.util.Optional;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.collision.QuadTree;
import com.okayboom.particlesim.collision.SpatialMap;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;
import com.okayboom.particlesim.physics.Vector;

public class QuadTreeSimulator implements Simulator {

	private static final Vector NEG_RADIUS = Vector.v(-1, -1);
	private static final Vector POS_RADIUS = Vector.v(1, 1);
	private static final Physics PHY = new Physics();

	@Override
	public SimResult simulate(final SimSettings settings) {

		final List<Particle> particles = SimUtil.particles(settings);
		final Box walls = SimUtil.walls(settings);

		final double boundaryMargin = settings.maxInitialVelocity;

		double totalMomentum = 0;
		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			totalMomentum += simulateOneStep(particles, walls, boundaryMargin);
		}

		return SimResult.EMPTY.givenSettings(settings).totalBoxMomentum(
				totalMomentum);
	}

	private double simulateOneStep(final List<Particle> particles, final Box walls,
			final double boundaryMargin) {

		final boolean[] hasMoved = new boolean[particles.size()];

		final SpatialMap<Integer> map = QuadTree.empty(walls, 10, boundaryMargin);

		for (int i = 0; i < particles.size(); ++i) {
			final Particle particle = particles.get(i);
			final Box bundingBox = particleBoundingBox(particle);
			map.add(bundingBox, i);
		}

		int totalMomentum = 0;
		for (int i = 0; i < particles.size(); ++i) {

			final Particle p1 = particles.get(i);

			final Optional<Collision> collisionOpt = findCollision(map, p1,
					particles, hasMoved);

			if (collisionOpt.isPresent()) {
				final Collision collision = collisionOpt.get();

				hasMoved[collision.otherParticleIndex] = true;

				final Particle p2 = particles.get(collision.otherParticleIndex);
				final double collisionTime = collision.collisionTime;
				PHY.interact(p1, p2, collisionTime);
			} else {
				PHY.euler(p1, 1);
			}

			hasMoved[i] = true;
			totalMomentum += PHY.wall_collide(p1, walls);
		}

		return totalMomentum;
	}

	private Box particleBoundingBox(final Particle particle) {
		final Vector position = particle.position;

		final Vector positionPlus = position.add(POS_RADIUS);
		final Vector positionMinus = position.add(NEG_RADIUS);

		final Vector nextPosition = position.add(particle.velocity);

		final Vector nextPositionPlus = nextPosition.add(POS_RADIUS);
		final Vector nextPositionMinus = nextPosition.add(NEG_RADIUS);

		final Vector boxMin = positionMinus.min(nextPositionMinus);
		final Vector boxMax = positionPlus.max(nextPositionPlus);

		return Box.box(boxMin, boxMax);
	}

	private Optional<Collision> findCollision(final SpatialMap<Integer> map,
			final Particle p, final List<Particle> particles, final boolean[] hasMoved) {

		final List<Integer> candidates = map.get(particleBoundingBox(p));

		if (candidates.size() > 100)
			System.err.println("To many candidates: " + candidates.size());

		for (final int candidate : candidates) {

			if (!hasMoved[candidate]) {

				final Particle p2 = particles.get(candidate);
				final double collisionTime = PHY.collide(p, p2);

				if (collisionTime != Physics.NO_COLLISION)
					return Optional.of(new Collision(candidate, collisionTime));

			}
		}
		return Optional.empty();
	}
}
