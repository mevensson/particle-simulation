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

		List<Particle> particles = SimUtil.particles(settings);
		Box walls = SimUtil.walls(settings);

		double boundaryMargin = settings.maxInitialVelocity;

		double totalMomentum = 0;
		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			totalMomentum += simulateOneStep(particles, walls, boundaryMargin);
		}

		return SimResult.EMPTY.givenSettings(settings).totalBoxMomentum(
				totalMomentum);
	}

	private double simulateOneStep(List<Particle> particles, Box walls,
			double boundaryMargin) {

		boolean[] hasMoved = new boolean[particles.size()];

		SpatialMap<Integer> map = QuadTree.empty(walls, 10, boundaryMargin);

		for (int i = 0; i < particles.size(); ++i) {
			Particle particle = particles.get(i);
			Box bundingBox = particleBoundingBox(particle);
			map.add(bundingBox, i);
		}

		int totalMomentum = 0;
		for (int i = 0; i < particles.size(); ++i) {

			Particle p1 = particles.get(i);

			Optional<Collision> collisionOpt = findCollision(map, p1,
					particles, hasMoved);

			if (collisionOpt.isPresent()) {
				Collision collision = collisionOpt.get();

				hasMoved[collision.otherParticleIndex] = true;

				Particle p2 = particles.get(collision.otherParticleIndex);
				double collisionTime = collision.collisionTime;
				PHY.interact(p1, p2, collisionTime);
			} else {
				PHY.euler(p1, 1);
			}

			hasMoved[i] = true;
			totalMomentum += PHY.wall_collide(p1, walls);
		}

		return totalMomentum;
	}

	private Box particleBoundingBox(Particle particle) {
		Vector position = particle.position;

		Vector positionPlus = position.add(POS_RADIUS);
		Vector positionMinus = position.add(NEG_RADIUS);

		Vector nextPosition = position.add(particle.velocity);

		Vector nextPositionPlus = nextPosition.add(POS_RADIUS);
		Vector nextPositionMinus = nextPosition.add(NEG_RADIUS);

		Vector boxMin = positionMinus.min(nextPositionMinus);
		Vector boxMax = positionPlus.min(nextPositionPlus);

		return Box.box(boxMin, boxMax);
	}

	private Optional<Collision> findCollision(SpatialMap<Integer> map,
			Particle p, List<Particle> particles, boolean[] hasMoved) {

		List<Integer> candidates = map.get(particleBoundingBox(p));

		if (candidates.size() > 100)
			System.err.println("To many candidates: " + candidates.size());

		for (int candidate : candidates) {

			if (!hasMoved[candidate]) {

				Particle p2 = particles.get(candidate);
				double collisionTime = PHY.collide(p, p2);

				if (collisionTime != Physics.NO_COLLISION)
					return Optional.of(new Collision(candidate, collisionTime));

			}
		}
		return Optional.empty();
	}
}
