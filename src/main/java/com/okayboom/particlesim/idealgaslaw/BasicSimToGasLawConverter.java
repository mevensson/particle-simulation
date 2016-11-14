package com.okayboom.particlesim.idealgaslaw;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;

public class BasicSimToGasLawConverter implements SimToGasLawConverter {

	@Override
	public GasLawValue convert(SimResult simResult) {
		SimSettings settings = simResult.simSettings;
		GasLawValue gasLaw = GasLawValue.empty();

		gasLaw = gasLaw.sampleName(settings.settingsName);
		gasLaw = gasLaw.volume(boxVolume(settings));
		gasLaw = gasLaw.pressure(pressure(simResult));
		gasLaw = gasLaw.substance(settings.particleCount);
		gasLaw = gasLaw.temperature(linearDistributionTemperature(settings));

		return gasLaw;
	}

	private int boxVolume(SimSettings settings) {
		return settings.boxWidth * settings.boxHeight;
	}

	private double pressure(SimResult simResult) {
		long stepCount = simResult.simSettings.steps;
		int boxArea = boxArea(simResult.simSettings);
		double momentum = simResult.totalBoxMomentum;

		boolean isDivisionByZero = boxArea == 0 || stepCount == 0;
		return isDivisionByZero ? 0 : momentum / stepCount / boxArea;
	}

	private int boxArea(SimSettings settings) {
		return 2 * (settings.boxWidth + settings.boxHeight);
	}

	private double linearDistributionTemperature(SimSettings settings) {
		return settings.maxInitialVelocity / 2;
	}
}
