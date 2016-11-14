package com.okayboom.particlesim.idealgaslaw;

import java.util.List;

public class TextDisplay {

	private static class Text {
		String txt = "";

		private void line(String text) {
			txt += text + System.lineSeparator();
		}

		private void line() {
			line("");
		}
	}

	public String summarizeResults(List<GasLawValue> values) {

		Text t = new Text();

		t.line();
		t.line(" Ideal gas law summary");
		t.line(" =====================");
		t.line();
		t.line("     PV = nRT");
		t.line("       <=>");
		t.line("      R = PV/(nT)");
		t.line();
		t.line("    where");
		t.line("    P is pressure");
		t.line("    V is volume");
		t.line("    n is is the ammount of substance (here particle count)");
		t.line("    T is the absolute temperature of the gas");
		t.line("    R is the ideal gas P is pressure");
		t.line();
		t.line(" Simulation results");
		t.line(" ==================");
		t.line();
		t.line(" | P            | V            | n            | T            | R*           | Name");
		t.line(" +--------------+--------------+--------------+--------------+--------------+---------------------");
		if (values.isEmpty())
			t.line(" |                              NO SIMULATIONS                              |");
		else {
			for (GasLawValue v : values)
				t.line(dataLine(v));
		}

		t.line(" +--------------+--------------+--------------+--------------+--------------+---------------------");
		t.line();
		t.line(" *) Calculated from the other values");
		t.line();

		return t.txt;
	}

	private String dataLine(GasLawValue v) {
		return String.format(" | %12g | %12g | %12g | %12g | %12g | %s",
				v.pressure, v.volume, v.substance, v.temperature,
				gasConstantR(v), v.sampleName);
	}

	private double gasConstantR(GasLawValue v) {
		return v.pressure * v.volume / v.substance / v.temperature;
	}
}
