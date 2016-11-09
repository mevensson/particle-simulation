package com.okayboom.particlesim.physics;

public final class Particle {
	public Vector position;
	public Vector velocity;

	public Particle(Vector position, Vector velocity) {
		this.position = position;
		this.velocity = velocity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;
		result = ((position == null) ? 0 : position.hashCode());
		result = prime * result
				+ ((velocity == null) ? 0 : velocity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Particle other = (Particle) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (velocity == null) {
			if (other.velocity != null)
				return false;
		} else if (!velocity.equals(other.velocity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Particle [pos=" + position + ", vel=" + velocity + "]";
	}
}
