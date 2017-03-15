package com.okayboom.particlesim.basicsim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;

public class BasicSimulator implements Simulator {

	private static final Physics PHY = new Physics();

	@Override
	public SimResult simulate(final SimSettings settings) {

		final List<Particle> particles = SimUtil.particles(settings);

		final Box walls = SimUtil.walls(settings);

		double totalMomentum = 0;
		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			totalMomentum += simulateOneStep(particles, walls);
		}

		return SimResult.EMPTY.givenSettings(settings).totalBoxMomentum(
				totalMomentum);
	}

	private double simulateOneStep(final List<Particle> particles, final Box walls) {
		final int particleCount = particles.size();

		final List<Particle> hasMoved = new ArrayList<>();

		int totalMomentum = 0;
		for (int i = 0; i < particleCount; ++i) {
			final Particle p1 = particles.get(i);

			if (!hasMoved.contains(p1)) {
				final Optional<Collision> collisionOpt = findCollision(particles, i,
						hasMoved);

				if (collisionOpt.isPresent()) {
					final Collision collision = collisionOpt.get();

					final Particle p2 = particles.get(collision.otherParticleIndex);
					hasMoved.add(p2);

					final double collisionTime = collision.collisionTime;
					PHY.interact(p1, p2, collisionTime);
				} else {
					PHY.euler(p1, 1);
				}
				hasMoved.add(p1);
			}

			totalMomentum += PHY.wall_collide(p1, walls);
		}

		return totalMomentum;
	}

	private Optional<Collision> findCollision(final List<Particle> particles,
			final int firstParticleIndex, final List<Particle> hasMoved) {
		Optional<Collision> collision = Optional.empty();
		final Particle p1 = particles.get(firstParticleIndex);
		for (int j = firstParticleIndex + 1; j < particles.size(); ++j) {

			final Particle p2 = particles.get(j);
			if (!hasMoved.contains(p2)) {

				final double collisionTime = PHY.collide(p1, p2);

				if (collisionTime != Physics.NO_COLLISION) {
					if (!collision.isPresent()
							|| collisionTime < collision.get().collisionTime) {
						collision = Optional.of(new Collision(j, collisionTime));
					}
				}

			}
		}
		return collision;
	}
}
