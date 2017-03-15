package com.okayboom.particlesim.basicsim;

import java.util.ArrayList;
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

		final SpatialMap<Particle> map = QuadTree.empty(walls, 10, boundaryMargin);

		for (final Particle particle : particles) {
			final Box bundingBox = particleBoundingBox(particle);
			map.add(bundingBox, particle);
		}

		int totalMomentum = 0;
		final List<Particle> movedParticles = new ArrayList<>();
		while (!particles.isEmpty()) {

			final Particle p1 = particles.remove(0);

			final Optional<Collision> collisionOpt = findCollision(map, p1,
					particles, movedParticles);

				if (collisionOpt.isPresent()) {
					final Collision collision = collisionOpt.get();

				final Particle p2 = collision.otherParticle;
				particles.remove(p2);

				final double collisionTime = collision.collisionTime;
				PHY.interact(p1, p2, collisionTime);
				movedParticles.add(p1);
				movedParticles.add(p2);
			} else {
				movedParticles.add(p1.move(1));
			}

		}

		for (final Particle movedParticle : movedParticles) {
			particles.add(movedParticle);
			totalMomentum += PHY.wall_collide(movedParticle, walls);
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

	private Optional<Collision> findCollision(final SpatialMap<Particle> map,
			final Particle p1, final List<Particle> particles,
			final List<Particle> movedParticles) {

		final List<Particle> candidates = map.get(particleBoundingBox(p1));

		if (candidates.size() > 100)
			System.err.println("To many candidates: " + candidates.size());

		Optional<Collision> collision = Optional.empty();
		for (final Particle p2 : candidates) {
			if (!movedParticles.contains(p2)) {
				final double collisionTime = PHY.collide(p1, p2);
				if (collisionTime != Physics.NO_COLLISION) {
					if (!collision.isPresent()
							|| collisionTime < collision.get().collisionTime) {
						collision = Optional.of(new Collision(p2, collisionTime));
					}
				}
			}
		}
		return collision;
	}
}
