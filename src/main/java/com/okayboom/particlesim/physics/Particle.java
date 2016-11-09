package com.okayboom.particlesim.physics;

public final class Particle {
	public Vector position;
	public Vector velocity;

	public Particle(Vector position, Vector velocity) {
		this.position = position;
		this.velocity = velocity;
	}
}
