package com.okayboom.particlesim.physics;

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
	public Vector generate(SourceOfRandomness random, GenerationStatus status) {
		return new Vector(withChanceOfZero(random), withChanceOfZero(random));
	}

	double withChanceOfZero(SourceOfRandomness random) {
		return (random.nextDouble() < chanceOfZero) ? 0.0 : random.nextDouble(
				-rangeMax, rangeMax);
	}
}
