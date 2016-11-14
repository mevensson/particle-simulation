package com.okayboom.particlesim;

public final class SimSettings {

	public static SimSettings EMPTY = new SimSettings("", 0, 0, 0, 0);

	public static SimSettings DEFAULT = EMPTY.settingsName("Default settings")
			.maxInitialVelocity(24).particleCount(10000).boxWidth(10000)
			.boxHeight(10000);

	public final String settingsName;
	public final double maxInitialVelocity;
	public final int particleCount;
	public final int boxWidth;
	public final int boxHeight;

	SimSettings(String settingsName, double maxInitialVelocity,
			int particleCount, int boxWidth, int boxHeight) {
		this.settingsName = settingsName;
		this.maxInitialVelocity = maxInitialVelocity;
		this.particleCount = particleCount;
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
	}

	public SimSettings settingsName(String sn) {
		return new SimSettings(sn, maxInitialVelocity, particleCount, boxWidth,
				boxHeight);
	}

	public SimSettings maxInitialVelocity(double miv) {
		return new SimSettings(settingsName, miv, particleCount, boxWidth,
				boxHeight);
	}

	public SimSettings particleCount(int pc) {
		return new SimSettings(settingsName, maxInitialVelocity, pc, boxWidth,
				boxHeight);
	}

	public SimSettings boxWidth(int bw) {
		return new SimSettings(settingsName, maxInitialVelocity, particleCount,
				bw, boxHeight);
	}

	public SimSettings boxHeight(int bh) {
		return new SimSettings(settingsName, maxInitialVelocity, particleCount,
				boxWidth, bh);
	}

	@Override
	public String toString() {
		return "SimSettings[\"" + settingsName + "\", maxInitVel="
				+ maxInitialVelocity + ", particles=" + particleCount
				+ ", box=" + boxWidth + "x" + boxHeight + "]";
	}

}
