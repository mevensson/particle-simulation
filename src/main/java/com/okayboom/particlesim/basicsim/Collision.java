package com.okayboom.particlesim.basicsim;

import com.okayboom.particlesim.physics.Particle;

public final class Collision {
	final Particle otherParticle;
	final double collisionTime;

	Collision(final Particle otherParticle, final double collisionTime) {
		this.otherParticle = otherParticle;
		this.collisionTime = collisionTime;
	}

	@Override
	public String toString() {
		return "Collision [otherParticle=" + otherParticle + ", collisionTime="
				+ collisionTime + "]";
	}
}