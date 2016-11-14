package com.okayboom.particlesim;

public final class SimResult {

	public static final SimResult EMPTY = new SimResult(null, 0, 0);

	public final double totalBoxMomentum;
	public final long totalStepCount;
	public final SimSettings simSettings;

	SimResult(SimSettings simSettings, long totalStepCount,
			double totalBoxMomentum) {
		this.simSettings = simSettings;
		this.totalStepCount = totalStepCount;
		this.totalBoxMomentum = totalBoxMomentum;
	}

	public SimResult givenSettings(SimSettings ss) {
		return new SimResult(ss, totalStepCount, totalBoxMomentum);
	}

	public SimResult totalStepCount(long tsc) {
		return new SimResult(simSettings, tsc, totalBoxMomentum);
	}

	public SimResult totalBoxMomentum(double tbm) {
		return new SimResult(simSettings, totalStepCount, tbm);
	}
}
