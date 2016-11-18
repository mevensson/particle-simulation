package com.okayboom.particlesim.physics;

public final class Box {
	public final Vector min;
	public final Vector max;

	public Box(Vector a, Vector b) {
		this.min = a.min(b);
		this.max = a.max(b);
	}

	public static final Box box(double x1, double y1, double x2, double y2) {
		return new Box(Vector.v(x1, y1), Vector.v(x2, y2));
	}
}