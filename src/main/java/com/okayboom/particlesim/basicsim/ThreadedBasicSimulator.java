package com.okayboom.particlesim.basicsim;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;

public class ThreadedBasicSimulator implements Simulator {

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
		final ExecutorService threadPool = Executors.newFixedThreadPool(2);
		final int particleCount = particles.size();

		final boolean[] hasMoved = new boolean[particleCount];

		final int[] totalMomentum = new int[1];
		for (int i = 0; i < particleCount; ++i) {
			final int index = i;
			final Particle p1 = particles.get(i);
			threadPool.execute(() -> {

				Optional<Collision> collisionOpt;

				synchronized (hasMoved) {
					collisionOpt = findCollision(particles, index, hasMoved);
					hasMoved[index] = true;
					if (collisionOpt.isPresent()) {
						final Collision collision = collisionOpt.get();
						hasMoved[collision.otherParticleIndex] = true;
					}
				}

				if (collisionOpt.isPresent()) {
					final Collision collision = collisionOpt.get();
					final Particle p2 = particles.get(collision.otherParticleIndex);
					final double collisionTime = collision.collisionTime;
					PHY.interact(p1, p2, collisionTime);
				} else {
					PHY.euler(p1, 1);
				}

				final double momentum = PHY.wall_collide(p1, walls);

				synchronized (totalMomentum) {
					totalMomentum[0] += momentum;
				}
			});
		}

		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (final InterruptedException e) {
		}

		return totalMomentum[0];
	}

	private Optional<Collision> findCollision(final List<Particle> particles,
			final int firstParticleIndex, final boolean[] hasMoved) {

		final Particle p1 = particles.get(firstParticleIndex);
		for (int j = firstParticleIndex + 1; j < particles.size(); ++j) {

			if (!hasMoved[j]) {

				final Particle p2 = particles.get(j);
				final double collisionTime = PHY.collide(p1, p2);

				if (collisionTime != Physics.NO_COLLISION)
					return Optional.of(new Collision(j, collisionTime));

			}
		}
		return Optional.empty();
	}
}
