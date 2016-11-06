package com.okayboom.particlesim.physics;

public final class Particle {
	public final Vector position;
	public final Vector velocity;

	public Particle(Vector position, Vector velocity) {
		this.position = position;
		this.velocity = velocity;
	}
}
