package com.okayboom.particlesim.basicsim;

public final class Collision {
	final int otherParticleIndex;
	final double collisionTime;

	Collision(final int otherParticleIndex, final double collisionTime) {
		this.otherParticleIndex = otherParticleIndex;
		this.collisionTime = collisionTime;
	}

	@Override
	public String toString() {
		return "Collision [otherParticleIndex=" + otherParticleIndex
				+ ", collisionTime=" + collisionTime + "]";
	}
}