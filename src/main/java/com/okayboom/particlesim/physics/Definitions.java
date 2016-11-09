package com.okayboom.particlesim.physics;

public interface Definitions {

	/* Maximum number of particles/processor */
	int MAX_NO_PARTICLES = 15000;
	/* Initial number of particles/processor */
	int INIT_NO_PARTICLES = 500;

	int MAX_INITIAL_VELOCITY = 50;

	double BOX_HORIZ_SIZE = 10000.0;
	double BOX_VERT_SIZE = 10000.0;
	double WALL_LENGTH = (2.0 * BOX_HORIZ_SIZE + 2.0 * BOX_VERT_SIZE);

	int PARTICLE_BUFFER_SIZE = MAX_NO_PARTICLES / 5;
	int COMM_BUFFER_SIZE = 5 * PARTICLE_BUFFER_SIZE;
}
