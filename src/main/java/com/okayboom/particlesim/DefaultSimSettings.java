package com.okayboom.particlesim;

public class DefaultSimSettings implements SimSettings {

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
