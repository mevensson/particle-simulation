package com.okayboom.particlesim;

public final class SimSettings {

	public static SimSettings EMPTY = new SimSettings("", 0, 0, 0, 0, 0);

	public final String settingsName;
	public final double maxInitialVelocity;
	public final int particleCount;
	public final int boxWidth;
	public final int boxHeight;
	public final long steps;

	SimSettings(String settingsName, double maxInitialVelocity,
			int particleCount, int boxWidth, int boxHeight, long steps) {
		this.settingsName = settingsName;
		this.maxInitialVelocity = maxInitialVelocity;
		this.particleCount = particleCount;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.steps = steps;
	}

	public SimSettings settingsName(String sn) {
		return new SimSettings(sn, maxInitialVelocity, particleCount, boxWidth,
				boxHeight, steps);
	}

	public SimSettings maxInitialVelocity(double miv) {
		return new SimSettings(settingsName, miv, particleCount, boxWidth,
				boxHeight, steps);
	}

	public SimSettings particleCount(int pc) {
		return new SimSettings(settingsName, maxInitialVelocity, pc, boxWidth,
				boxHeight, steps);
	}

	public SimSettings boxWidth(int bw) {
		return new SimSettings(settingsName, maxInitialVelocity, particleCount,
				bw, boxHeight, steps);
	}

	public SimSettings boxHeight(int bh) {
		return new SimSettings(settingsName, maxInitialVelocity, particleCount,
				boxWidth, bh, steps);
	}

	public SimSettings steps(int s) {
		return new SimSettings(settingsName, maxInitialVelocity, particleCount,
				boxWidth, boxHeight, s);
	}

	@Override
	public String toString() {
		return "SimSettings[\"" + settingsName + "\", maxInitVel="
				+ maxInitialVelocity + ", particles=" + particleCount
				+ ", box=" + boxWidth + "x" + boxHeight + ", steps=" + steps
				+ "]";
	}
}
