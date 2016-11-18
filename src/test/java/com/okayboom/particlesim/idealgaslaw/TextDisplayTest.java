package com.okayboom.particlesim.idealgaslaw;

import static org.junit.Assert.*;

import java.text.DecimalFormatSymbols;
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

		char sep = DecimalFormatSymbols.getInstance().getDecimalSeparator();
		startLines();
		line(String.format(
				" |      1001%s00 |      1002%s00 |      1003%s00 |      1004%s00 |     0%s996018 | First Sim",
				sep, sep, sep, sep, sep));
		line(String.format(
				" |      2%s00000 |      3%s00000 |      4%s00000 |      5%s00000 |     0%s300000 | 2nd simulation",
				sep, sep, sep, sep, sep));
		endLines();

		assertResultSummaryEqualsExpected(Arrays.asList(g1, g2));
	}

	private void assertResultSummaryEqualsExpected(List<GasLawValue> values) {
		TextDisplay cut = new TextDisplay();
		String actualOutput = cut.summarizeResults(values);
		assertEquals("Has matching output", expectedOutput, actualOutput);
	}
}
