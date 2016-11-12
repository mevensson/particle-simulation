package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Vector.v;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PhysicsTest {

	@Test
	public void testEuler() throws Exception {
		Vector position = v(1, -1);
		Vector velocity = v(2.2, 1.1);
		double time = 2.0;

		Vector expectedPos = velocity.mult(time).add(position);
		Particle expectedParticle = new Particle(expectedPos, velocity);

		Particle p = new Particle(position, velocity);
		new Physics().euler(p, time);

		assertEquals(expectedParticle, p);

	}
}