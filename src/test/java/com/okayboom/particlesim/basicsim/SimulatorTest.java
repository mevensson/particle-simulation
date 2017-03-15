package com.okayboom.particlesim.basicsim;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.okayboom.particlesim.SimResult;
import com.okayboom.particlesim.SimSettings;
import com.okayboom.particlesim.Simulator;

@RunWith(Parameterized.class)
public class SimulatorTest {

	@Parameters
	public static Object[] data() {
		return new Object[] {
				new BasicSimulator(),
				new QuadTreeSimulator()
		};
	}

	private final Simulator simulator;

	public SimulatorTest(final Simulator simulator) {
		this.simulator = simulator;
	}

	@Test
	public void shouldCalculateTotalBoxMomentumFast() {
		final SimSettings settings = SimSettings.EMPTY.settingsName("Baseline")
				.maxInitialVelocity(24).particleCount(2000).boxWidth(1000)
				.boxHeight(1000).steps(10);
		final SimResult result = simulator.simulate(settings);
		assertThat(result.totalBoxMomentum, is(7695.0));
	}

	@Ignore
	@Test
	public void shouldCalculateTotalBoxMomentumSlow() {
		final SimSettings settings = SimSettings.EMPTY.settingsName("Baseline")
				.maxInitialVelocity(24).particleCount(2000).boxWidth(1000)
				.boxHeight(1000).steps(500);
		final SimResult result = simulator.simulate(settings);
		assertThat(result.totalBoxMomentum, is(381305.0));
	}

}
