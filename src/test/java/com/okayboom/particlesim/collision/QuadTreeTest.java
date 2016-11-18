package com.okayboom.particlesim.collision;

import static com.okayboom.particlesim.physics.Box.box;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.okayboom.particlesim.physics.Box;

public class QuadTreeTest {

	Box totalArea = box(0, 0, 10, 10);

	@Test
	public void testCanAddAndGet() throws Exception {

		QuadTree<String> qt = QuadTree.empty(totalArea);

		qt.add(box(1, 1, 2, 2), "Foo");
		qt.add(box(-1, -1, 0, 0), "Bar"); // outside total area

		assertEquals(Arrays.asList("Foo"), qt.get(box(0, 0, 15, 15)));
		assertEquals(Arrays.asList(), qt.get(box(2, 0, 15, 15)));
	}

	@Test
	public void testLeafLimit() throws Exception {

		QuadTree<String> qt = QuadTree.empty(totalArea, 2);

		qt.add(box(1, 1, 2, 2), "A");
		qt.add(box(6, 6, 7, 7), "B");

		assertTrue("QT should still be a leaf", qt.isLeaf());

		qt.add(box(8, 8, 9, 9), "C");
		assertFalse("QT not be a leaf", qt.isLeaf());
	}
}
