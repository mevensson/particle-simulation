package com.okayboom.particlesim.idealgaslaw;

public final class GasLawValue {

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

	@Override
	public String toString() {
		return "GasLawValue [press=" + pressure + ", vol=" + volume
				+ ", subst=" + substance + ", temp=" + temperature
				+ ", sampleName='" + sampleName + "']";
	}

	@Override
	public int hashCode() {
		final int prm = 31;
		int res = 1;
		long tmp;
		tmp = Double.doubleToLongBits(pressure);
		res = prm * res + (int) (tmp ^ (tmp >>> 32));
		res = prm * res + ((sampleName == null) ? 0 : sampleName.hashCode());
		tmp = Double.doubleToLongBits(substance);
		res = prm * res + (int) (tmp ^ (tmp >>> 32));
		tmp = Double.doubleToLongBits(temperature);
		res = prm * res + (int) (tmp ^ (tmp >>> 32));
		tmp = Double.doubleToLongBits(volume);
		res = prm * res + (int) (tmp ^ (tmp >>> 32));
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GasLawValue other = (GasLawValue) obj;
		if (Double.doubleToLongBits(pressure) != Double
				.doubleToLongBits(other.pressure))
			return false;
		if (sampleName == null) {
			if (other.sampleName != null)
				return false;
		} else if (!sampleName.equals(other.sampleName))
			return false;
		if (Double.doubleToLongBits(substance) != Double
				.doubleToLongBits(other.substance))
			return false;
		if (Double.doubleToLongBits(temperature) != Double
				.doubleToLongBits(other.temperature))
			return false;
		return Double.doubleToLongBits(volume) == Double
				.doubleToLongBits(other.volume);
	}
}
