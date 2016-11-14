package com.okayboom.particlesim.idealgaslaw;

import static org.junit.Assert.*;

import org.junit.Test;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;

public class SimToGasLawConverterTest {

	SimToGasLawConverter cut = new BasicSimToGasLawConverter();

	@Test
	public void testEmpty() throws Exception {

		SimSettings simSettings = SimSettings.EMPTY.settingsName("")
				.maxInitialVelocity(0).particleCount(0).boxWidth(0)
				.boxHeight(0);

		SimResult simResult = SimResult.EMPTY.givenSettings(simSettings)
				.totalStepCount(0).totalBoxMomentum(0);

		GasLawValue expectedGasLawValue = GasLawValue.empty().pressure(0)
				.substance(0).temperature(0).volume(0).sampleName("");

		GasLawValue actualGasLawValue = cut.convert(simResult);

		assertEquals("Conversion", expectedGasLawValue, actualGasLawValue);
	}

	@Test
	public void testUnique() throws Exception {

		SimSettings simSettings = SimSettings.EMPTY.settingsName("Alfa")
				.maxInitialVelocity(222).particleCount(2).boxWidth(3)
				.boxHeight(4);

		SimResult simResult = SimResult.EMPTY.givenSettings(simSettings)
				.totalStepCount(5).totalBoxMomentum(35);

		int boxArea = (simSettings.boxHeight + simSettings.boxWidth) * 2;
		double expectedPressure = simResult.totalBoxMomentum / boxArea
				/ simResult.totalStepCount;

		GasLawValue expectedGasLawValue = GasLawValue.empty()
				.pressure(expectedPressure).substance(2).temperature(111)
				.volume(12).sampleName("Alfa");

		GasLawValue actualGasLawValue = cut.convert(simResult);

		assertEquals("Exact expected pressure", 0.5, expectedPressure, 0.0);
		assertEquals("Conversion", expectedGasLawValue, actualGasLawValue);

	}
}
