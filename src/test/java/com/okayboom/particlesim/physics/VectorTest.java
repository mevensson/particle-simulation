package com.okayboom.particlesim.physics;

import static org.junit.Assert.*;
import static com.okayboom.particlesim.physics.Vector.*;

import org.junit.Test;

public class VectorTest {

	@Test
	public void testMinAndMax() throws Exception {

		Vector v1 = v(0, 0);
		Vector v2 = v(-1, 1);

		assertEquals(v(-1, 0), v1.min(v2));
		assertEquals(v(0, 1), v1.max(v2));
	}
}
