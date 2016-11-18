package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Box.box;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoxTest {

	@Test
	public void testMinAndMaxProperies() throws Exception {
		Box b = box(-1, 0, -1.5, 4);

		assertEquals(Vector.v(-1.5, 0), b.min);
		assertEquals(Vector.v(-1, 4), b.max);
	}
}
