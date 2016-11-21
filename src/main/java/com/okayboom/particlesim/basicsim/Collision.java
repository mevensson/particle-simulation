package com.okayboom.particlesim.basicsim;

public final class Collision {
	final int otherParticleIndex;
	final double collisionTime;

	Collision(int otherParticleIndex, double collisionTime) {
		this.otherParticleIndex = otherParticleIndex;
		this.collisionTime = collisionTime;
	}
}