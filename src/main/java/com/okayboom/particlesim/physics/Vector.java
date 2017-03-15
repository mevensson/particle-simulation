package com.okayboom.particlesim.physics;

public final class Vector {
	public static Vector v(final double x, final double y) {
		return new Vector(x, y);
	}

	private double x;
	private double y;

	private Vector(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(final double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(final double y) {
		this.y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		long temp;
		temp = Double.doubleToLongBits(x);
		int result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vector other = (Vector) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public Vector mult(final double factor) {
		return v(x * factor, y * factor);
	}

	public Vector add(final Vector other) {
		return v(x + other.x, y + other.y);
	}

	public double abs() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector min(final Vector v2) {
		return v(Math.min(x, v2.x), Math.min(y, v2.y));
	}

	public Vector max(final Vector v2) {
		return v(Math.max(x, v2.x), Math.max(y, v2.y));
	}

	public Vector sub(final Vector other) {
		return v(x - (other.x), y - (other.y));
	}
}
