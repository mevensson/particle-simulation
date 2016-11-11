package com.okayboom.particlesim;

public interface Settings {

	static Settings defaults() {
		return new DefaultSettings();
	}

	double timeStep();

	double maxInitialVelocity();

	int particleCount();

	long boxWidth();

	long boxHeight();
}
