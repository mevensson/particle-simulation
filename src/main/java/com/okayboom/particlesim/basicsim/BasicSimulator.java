package com.okayboom.particlesim.basicsim;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.codepoetics.protonpack.StreamUtils;
import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;
import com.okayboom.particlesim.physics.Box;
import com.okayboom.particlesim.physics.Particle;
import com.okayboom.particlesim.physics.Physics;
import com.okayboom.particlesim.physics.Vector;

public class BasicSimulator implements Simulator {

	private static final Physics PHY = new Physics();

	@Override
	public SimResult simulate(SimSettings settings) {

		double totalMomentum = 0;

		List<Particle> particles = initiate(settings);
		int size = particles.size();

		Box walls = new Box(Vector.v(0, 0), Vector.v(settings.boxWidth,
				settings.boxHeight));

		for (int timeStep = 0; timeStep < settings.steps; ++timeStep) {
			for (int i = 0; i < size; ++i) {
				Particle p1 = particles.get(i);

				boolean hasParticleInteraction = false;
				for (int j = i + 1; j < size; ++j) {
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

	private List<Particle> initiate(SimSettings settings) {
		return particleStream(settings).limit(settings.particleCount).collect(
				Collectors.toList());
	}

	private Stream<Particle> particleStream(SimSettings settings) {
		Random rnd = new Random();
		Stream<Vector> velos = velocityStream(settings, rnd);
		Stream<Vector> poses = positionStream(settings, rnd);
		return StreamUtils.zip(poses, velos, (s, v) -> new Particle(s, v));
	}

	private Stream<Vector> positionStream(SimSettings settings, Random rnd) {
		Stream<Double> xPos = rnd.doubles(0, settings.boxWidth).boxed();
		Stream<Double> yPos = rnd.doubles(0, settings.boxWidth).boxed();
		return StreamUtils.zip(xPos, yPos, (x, y) -> Vector.v(x, y));
	}

	private Stream<Vector> velocityStream(SimSettings settings, Random rnd) {
		Stream<Double> radians = rnd.doubles(0, Math.PI * 2).boxed();
		Stream<Double> speeds = rnd.doubles(0, settings.maxInitialVelocity)
				.boxed();

		return StreamUtils.zip(radians, speeds, (r, s) -> {
			double x = s * Math.cos(r);
			double y = s * Math.sin(r);
			return Vector.v(x, y);
		});
	}
}
