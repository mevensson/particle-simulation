package com.okayboom.particlesim;

public class DefaultSettings implements Settings {

	@Override
	public double timeStep() {
		return 1;
	}

	@Override
	public double maxInitialVelocity() {
		return 50;
	}

	@Override
	public int particleCount() {
		return 10000;
	}

	@Override
	public long boxWidth() {
		return 10000;
	}

	@Override
	public long boxHeight() {
		return 10000;
	}
}
