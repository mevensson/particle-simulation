package com.okayboom.particlesim;

public interface SimResult {

	SimSettings givenSettings();

	long totalStepCount();

	double totalBoxMomentum();
}
