package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Vector.v;

import java.util.Optional;

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

	public static final Box box(Vector a, Vector b) {
		return new Box(a, b);
	}

	public Optional<Box> intersection(Box b2) {

		Vector intMin = min.max(b2.min);
		Vector intMax = max.min(b2.max);

		boolean doIntersect = intMin.x < intMax.x && intMin.y < intMax.y;
		Box box = doIntersect ? box(intMin, intMax) : null;
		return Optional.ofNullable(box);
	}

	public boolean doIntersect(Box b) {
		return intersection(b).isPresent();
	}

	@Override
	public String toString() {
		return "Box(min" + min + ", max" + max + ")";
	}

	@Override
	public int hashCode() {
		int result = ((max == null) ? 0 : max.hashCode());
		return 31 * result + ((min == null) ? 0 : min.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		}
		return min.equals(other.min);
	}

	public Vector mid() {
		double x = (min.x + max.x) / 2;
		double y = (min.y + max.y) / 2;
		return v(x, y);
	}

	public Vector minMax() {
		return v(min.x, max.y);
	}

	public Vector maxMin() {
		return v(max.x, min.y);
	}

	public Box union(Box b2) {
		Vector unionMin = min.min(b2.min);
		Vector unionMax = max.max(b2.max);
		return box(unionMin, unionMax);
	}
}