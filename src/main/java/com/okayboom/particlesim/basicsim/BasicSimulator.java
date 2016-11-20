package com.okayboom.particlesim.basicsim;

import java.util.List;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;

public class BasicSimulator implements Simulator {

	private static final Physics PHY = new Physics();

	@Override
	public SimResult simulate(SimSettings settings) {

		double totalMomentum = 0;

		List<Particle> particles = SimUtil.particles(settings);
		int particleCount = particles.size();

		Box walls = SimUtil.walls(settings);

		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			for (int i = 0; i < particleCount; ++i) {
				Particle p1 = particles.get(i);

				boolean hasParticleInteraction = false;
				for (int j = i + 1; j < particleCount; ++j) {
					Particle p2 = particles.get(j);

					double collisionTime = PHY.collide(p1, p2);
					boolean willCollide = collisionTime != Physics.NO_COLLISION;

					if (willCollide) {
						PHY.interact(p1, p2, collisionTime);
						hasParticleInteraction = true;
						break;
					}
				}

				if (!hasParticleInteraction) {
					PHY.euler(p1, 1);
				}

				double collisionMomentum = PHY.wall_collide(p1, walls);

				if (collisionMomentum != Physics.NO_MOMENTUM) {
					totalMomentum += collisionMomentum;
				}
			}
		}

		return SimResult.EMPTY.givenSettings(settings).totalBoxMomentum(
				totalMomentum);
	}
}
