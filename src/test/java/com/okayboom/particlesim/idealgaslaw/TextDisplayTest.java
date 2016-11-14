package com.okayboom.particlesim.idealgaslaw;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TextDisplayTest {

	String expectedOutput = "";

	private void line(String text) {
		expectedOutput += text + System.lineSeparator();
	}

	private void line() {
		line("");
	}

	private void startLines() {
		line();
		line(" Ideal gas law summary");
		line(" =====================");
		line();
		line("     PV = nRT");
		line("       <=>");
		line("      R = PV/(nT)");
		line();
		line("    where");
		line("    P is pressure");
		line("    V is volume");
		line("    n is is the ammount of substance (here particle count)");
		line("    T is the absolute temperature of the gas");
		line("    R is the ideal gas P is pressure");
		line();
		line(" Simulation results");
		line(" ==================");
		line();
		line(" | P            | V            | n            | T            | R*           | Name");
		line(" +--------------+--------------+--------------+--------------+--------------+---------------------");
	}

	private void endLines() {
		line(" +--------------+--------------+--------------+--------------+--------------+---------------------");
		line();
		line(" *) Calculated from the other values");
		line();
	}

	@Test
	public void testEmptyResult() throws Exception {

		List<GasLawValue> emptyList = Arrays.asList();

		startLines();
		line(" |                              NO SIMULATIONS                              |");
		endLines();

		assertResultSummaryEqualsExpected(emptyList);
	}

	@Test
	public void testSomeResult() throws Exception {

		GasLawValue g1 = GasLawValue.empty().pressure(1001).volume(1002)
				.substance(1003).temperature(1004).sampleName("First Sim");

		GasLawValue g2 = GasLawValue.empty().pressure(2).volume(3).substance(4)
				.temperature(5).sampleName("2nd simulation");

		startLines();
		line(" |      1001,00 |      1002,00 |      1003,00 |      1004,00 |     0,996018 | First Sim");
		line(" |      2,00000 |      3,00000 |      4,00000 |      5,00000 |     0,300000 | 2nd simulation");
		endLines();

		assertResultSummaryEqualsExpected(Arrays.asList(g1, g2));
	}

	private void assertResultSummaryEqualsExpected(List<GasLawValue> values) {
		TextDisplay cut = new TextDisplay();
		String actualOutput = cut.summarizeResults(values);
		assertEquals("Has matching output", expectedOutput, actualOutput);
	}
}
