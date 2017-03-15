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
		int totalMomentum = 0;
		final List<Particle> movedParticles = new ArrayList<>();
		while (!particles.isEmpty()) {
			final Particle p1 = particles.remove(0);

			final Optional<Collision> collisionOpt = findCollision(particles, p1);

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

	private Optional<Collision> findCollision(final List<Particle> particles,
			final Particle p1) {
		Optional<Collision> collision = Optional.empty();
		for (final Particle p2 : particles) {
			final double collisionTime = PHY.collide(p1, p2);

			if (collisionTime != Physics.NO_COLLISION) {
				if (!collision.isPresent()
						|| collisionTime < collision.get().collisionTime) {
					collision = Optional.of(new Collision(p2, collisionTime));
				}
			}
		}
		return collision;
	}
}
