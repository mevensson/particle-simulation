package com.okayboom.particlesim.physics;

public final class Box {
	public final Vector a;
	public final Vector b;

	public Box(Vector a, Vector b) {
		this.a = a;
		this.b = b;
	}

	public static final Box box(double x1, double y1, double x2, double y2) {
		return new Box(Vector.v(x1, y1), Vector.v(x2, y2));
	}
}