package com.okayboom.particlesim.collision;

import static com.okayboom.particlesim.physics.Box.box;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

	@Test
	public void testGetBoundary() throws Exception {
		QuadTree<String> qt = QuadTree.empty(totalArea, 2);
		assertEquals(totalArea, qt.boundary());
	}

	@Test
	public void testDistribution() throws Exception {
		QuadTree<String> qt = QuadTree.empty(totalArea, 3);

		qt.add(box(1, 1, 2, 2), "A");
		qt.add(box(7, 7, 8, 8), "B");
		qt.add(box(4, 4, 6, 6), "Center");

		assertTrue("Should be leaf", qt.isLeaf());
		assertNull("Should not have children", qt.childTrees());
		assertEquals("Should have three values", 3, qt.values().size());

		// adding the fourth element
		qt.add(box(3, 3, 4, 4), "C");

		assertFalse("Should not be leaf", qt.isLeaf());
		assertNotNull("Should have children", qt.childTrees());
		assertEquals("Should have 1 value, that ovelaps child boundaries", 1,
				qt.values().size());

		// center
		assertEquals("Center", qt.values().get(0).value);
		assertEquals(box(4, 4, 6, 6), qt.values().get(0).box);

		assertEquals(4, qt.childTrees().size());

		// validating value in child tree node
		Optional<QuadTree<String>> maxMaxChild = qt.childTrees().stream()
				.filter(ct -> ct.boundary().equals(box(5, 5, 10, 10)))
				.findFirst();

		assertTrue(maxMaxChild.isPresent());
		List<String> values = maxMaxChild.get().get(box(6, 6, 9, 9));
		assertEquals(1, values.size());
		assertEquals("B", values.get(0));
	}
}
