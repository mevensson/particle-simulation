package com.okayboom.particlesim;

public final class SimResult {

	public static final SimResult EMPTY = new SimResult(null, 0);

	public final double totalBoxMomentum;
	public final SimSettings simSettings;

	SimResult(SimSettings simSettings, double totalBoxMomentum) {
		this.simSettings = simSettings;
		this.totalBoxMomentum = totalBoxMomentum;
	}

	public SimResult givenSettings(SimSettings ss) {
		return new SimResult(ss, totalBoxMomentum);
	}

	public SimResult totalBoxMomentum(double tbm) {
		return new SimResult(simSettings, tbm);
	}
}
