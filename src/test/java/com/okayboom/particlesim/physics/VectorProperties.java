package com.okayboom.particlesim.physics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

@RunWith(JUnitQuickcheck.class)
public class VectorProperties {

	private static final double DELTA = 1e-10;

	@Property
	public void scaleWithFactor(@From(VectorGen.class) Vector v,
			@InRange(min = "-100", max = "100") double factor) {

		Vector vf = v.mult(factor);
		assertEquals(vf.getX(), v.getX() * factor, DELTA);
		assertEquals(vf.getY(), v.getY() * factor, DELTA);
	}

	@Property
	public void triangleInequality(@From(VectorGen.class) Vector a,
			@From(VectorGen.class) Vector b) {

		Vector sum = a.add(b);
		assertTrue(sum.abs() <= (a.abs() + b.abs()));
	}

	@Property
	public void absIsPositive(@From(VectorGen.class) Vector a) {
		assertTrue(a.abs() >= 0);
	}

	@Property
	public void absIsAtLeastAsLargeAsIndividualVectorComponents(
			@From(VectorGen.class) Vector a) {
		assertTrue(a.abs() >= Math.abs(a.getX()));
		assertTrue(a.abs() >= Math.abs(a.getY()));
	}
}
