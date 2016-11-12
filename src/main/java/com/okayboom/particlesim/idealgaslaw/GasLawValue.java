package com.okayboom.particlesim.idealgaslaw;

public class GasLawValue {

	public static final GasLawValue EMPTY = new GasLawValue(0, 0, 0, 0, "");

	static GasLawValue empty() {
		return EMPTY;
	}

	public final double pressure;
	public final double volume;
	public final double substance;
	public final double temperature;
	public final String sampleName;

	public GasLawValue(double pressure, double volume, double substance,
			double temperatue, String sampleName) {
		this.pressure = pressure;
		this.volume = volume;
		this.substance = substance;
		this.temperature = temperatue;
		this.sampleName = sampleName;
	}

	public GasLawValue pressure(double p) {
		return new GasLawValue(p, volume, substance, temperature, sampleName);
	}

	public GasLawValue volume(double v) {
		return new GasLawValue(pressure, v, substance, temperature, sampleName);
	}

	public GasLawValue substance(double s) {
		return new GasLawValue(pressure, volume, s, temperature, sampleName);
	}

	public GasLawValue temperature(double t) {
		return new GasLawValue(pressure, volume, substance, t, sampleName);
	}

	public GasLawValue sampleName(String n) {
		return new GasLawValue(pressure, volume, substance, temperature, n);
	}
}
