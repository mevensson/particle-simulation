package com.okayboom.particlesim.basicsim;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;

public class ThreadedBasicSimulator2 implements Simulator {

	private static final Physics PHY = new Physics();

	private final ExecutorService threadPool = Executors.newFixedThreadPool(1);

	@Override
	public SimResult simulate(final SimSettings settings) {

		final List<Particle> particles = SimUtil.particles(settings);

		final Box walls = SimUtil.walls(settings);

		double totalMomentum = 0;
		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			System.out.println("step " + timeStep);
			totalMomentum += simulateOneStep(particles, walls);
		}

		return SimResult.EMPTY.givenSettings(settings).totalBoxMomentum(
				totalMomentum);
	}

	private double simulateOneStep(final List<Particle> particles, final Box walls) {
		final int particleCount = particles.size();

		final boolean[] hasMoved = new boolean[particleCount];

		int totalMomentum = 0;
		for (int i = 0; i < particleCount; ++i) {
			final Particle p1 = particles.get(i);

			final Optional<Collision> collisionOpt = findCollision(particles, i,
					hasMoved);

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

	private Optional<Collision> findCollision(final List<Particle> particles,
			final int firstParticleIndex, final boolean[] hasMoved) {
		final Particle p1 = particles.get(firstParticleIndex);
		final List<Callable<Optional<Collision>>> taskList = new ArrayList<>();
		for (int j = firstParticleIndex + 1; j < particles.size(); ++j) {
			if (!hasMoved[j]) {
				final Particle p2 = particles.get(j);
				final int otherIndex = j;
				taskList.add(() -> {
					final double collisionTime = PHY.collide(p1, p2);
					if (collisionTime != Physics.NO_COLLISION) {
						return Optional.of(new Collision(otherIndex, collisionTime));
					}
					return Optional.empty();
				});
			}
		}

		Optional<Collision> collision = Optional.empty();
		try {
			final List<Future<Optional<Collision>>> results =
					threadPool.invokeAll(taskList);

			for (final Future<Optional<Collision>> result : results) {
				final Optional<Collision> newCollision = result.get();
				if (newCollision.isPresent()) {
					if (!collision.isPresent()
							|| newCollision.get().collisionTime < collision.get().collisionTime) {
						collision = newCollision;
					}
				}
			}
		} catch (final InterruptedException | ExecutionException e) {
		}

		return collision;
	}
}
