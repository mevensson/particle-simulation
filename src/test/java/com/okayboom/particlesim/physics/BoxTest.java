package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Box.box;
import static com.okayboom.particlesim.physics.Vector.v;
import static org.junit.Assert.*;

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

		assertEquals(expected, b1.intersection(b2));
		assertEquals(expected, b2.intersection(b1));
	}

	@Test
	public void testIntersect() throws Exception {
		Box b1 = box(0, 0, 10, 1);
		Box b2 = box(5, 0.5, 20, 20);
		Optional<Box> expected = Optional.of(box(5, 0.5, 10, 1));

		assertEquals(expected, b1.intersection(b2));
		assertEquals(expected, b2.intersection(b1));
	}

	@Test
	public void testUnion() throws Exception {
		Box b1 = box(0, 0, 1, 1);
		Box b2 = box(0, 0, -1, -1);
		Box expected = box(-1, -1, 1, 1);

		assertEquals(expected, b1.union(b2));
		assertEquals(expected, b2.union(b1));
	}

	@Test
	public void testMid() throws Exception {
		assertEquals(v(2, 20), box(1, 10, 3, 30).mid());
		assertEquals(v(0, 0), box(0, 0, 0, 0).mid());
	}

	@Test
	public void testMinMaxAndMaxMin() throws Exception {
		assertEquals(v(1, 30), box(1, 10, 3, 30).minMax());
		assertEquals(v(3, 10), box(1, 10, 3, 30).maxMin());
	}

	@Test
	public void testDoSorround() throws Exception {
		Box outer = box(0, 0, 2, 2);
		Box inner = box(0.5, 0.5, 1, 1);
		Box overlapping = box(-0.5, 0, 0.75, 0.75);

		assertTrue("Outer sorrounds inner", outer.doSorround(inner));
		assertTrue("Outer sorrounds outer", outer.doSorround(outer));
		assertTrue("inner sorrounds inner", inner.doSorround(inner));
		assertTrue("inner sorrounds inner", inner.doSorround(inner));
		assertTrue("inner sorrounds inner", inner.doSorround(inner));

		assertFalse("overlapping overlapps outer",
				overlapping.doSorround(outer));
		assertFalse("outer overlapps overlapping",
				outer.doSorround(overlapping));
		assertFalse("overlapping overlapps inner",
				overlapping.doSorround(inner));
		assertFalse("inner overlapps overlapping",
				inner.doSorround(overlapping));
	}

	@Test
	public void testAddMargin() throws Exception {
		assertEquals(box(-1, -1, 1, 1), box(0, 0, 0, 0).pad(1));
		assertEquals(box(-1, -1, 1, 1), box(0, 0, 0, 0).pad(-1));
		assertEquals(box(0.5, 9.5, 2.5, 20.5), box(1, 10, 2, 20).pad(0.5));
	}
}
