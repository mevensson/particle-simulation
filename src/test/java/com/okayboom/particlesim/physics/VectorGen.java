package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Vector.v;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class VectorGen extends Generator<Vector> {

	private static final double chanceOfZero = 0.1;
	private static final double rangeMax = 1e6;

	public VectorGen() {
		super(Vector.class);
	}

	@Override
	public Vector generate(final SourceOfRandomness random, final GenerationStatus status) {
		return v(withChanceOfZero(random), withChanceOfZero(random));
	}

	double withChanceOfZero(final SourceOfRandomness random) {
		return (random.nextDouble() < chanceOfZero) ? 0.0 : random.nextDouble(
				-rangeMax, rangeMax);
	}
}
