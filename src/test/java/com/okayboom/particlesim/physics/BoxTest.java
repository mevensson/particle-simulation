package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Box.box;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

public class BoxTest {

	@Test
	public void testMinAndMaxProperies() throws Exception {
		Box b = box(-1, 0, -1.5, 4);

		assertEquals(Vector.v(-1.5, 0), b.min);
		assertEquals(Vector.v(-1, 4), b.max);
	}

	@Test
	public void testNoIntersect() throws Exception {
		Box b1 = box(0, 0, 1, 1);
		Box b2 = box(0, 0, -1, -1);
		Optional<Box> expected = Optional.empty();

		assertEquals(expected, b1.intersect(b2));
		assertEquals(expected, b2.intersect(b1));
	}

	@Test
	public void testIntersect() throws Exception {
		Box b1 = box(0, 0, 10, 1);
		Box b2 = box(5, 0.5, 20, 20);
		Optional<Box> expected = Optional.of(box(5, 0.5, 10, 1));

		assertEquals(expected, b1.intersect(b2));
		assertEquals(expected, b2.intersect(b1));
	}
}
