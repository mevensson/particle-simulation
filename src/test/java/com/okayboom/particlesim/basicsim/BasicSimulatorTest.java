package com.okayboom.particlesim.basicsim;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;

public class BasicSimulatorTest {

	@Test
	public void shouldCalculateTotalBoxMomentumFast() {
		final SimSettings settings = SimSettings.EMPTY.settingsName("Baseline")
				.maxInitialVelocity(24).particleCount(2000).boxWidth(1000)
				.boxHeight(1000).steps(10);
		final BasicSimulator simulator = new BasicSimulator();
		final SimResult result = simulator.simulate(settings);
		assertThat(result.totalBoxMomentum, is(7695.0));
	}

	@Ignore
	@Test
	public void shouldCalculateTotalBoxMomentumSlow() {
		final SimSettings settings = SimSettings.EMPTY.settingsName("Baseline")
				.maxInitialVelocity(24).particleCount(2000).boxWidth(1000)
				.boxHeight(1000).steps(500);
		final BasicSimulator simulator = new BasicSimulator();
		final SimResult result = simulator.simulate(settings);
		assertThat(result.totalBoxMomentum, is(381305.0));
	}

}
