package com.okayboom.particlesim.physics;

import static com.okayboom.particlesim.physics.Vector.v;

public class Physics {

	public static final double NO_COLLISION = -1;

	private double fabs(final double n) {
		return n < 0 ? -n : n;
	}

	private double sqr(final double n) {
		return n * n;
	}

	/** Moves the a particle. */
	public int euler(final Particle a, final double time_step) {
		a.position.setX(a.position.getX() + time_step * a.velocity.getX());
		a.position.setY(a.position.getY() + time_step * a.velocity.getY());
		return 0;
	}

	/**
	 * wall_collide checks if a particle has exceeded the boundary and returns a
	 * momentum. Use this momentum to calculate the pressure.
	 */
	public double wall_collide(final Particle p, final Box box) {
		double gPreassure = 0.0;

		if (p.position.getX() < box.min.getX()) {
			p.velocity.setX(-p.velocity.getX());
			p.position.setX(box.min.getX() + (box.min.getX() - p.position.getX()));
			gPreassure += 2.0 * fabs(p.velocity.getX());
		}
		if (p.position.getX() > box.max.getX()) {
			p.velocity.setX(-p.velocity.getX());
			p.position.setX(box.max.getX() - (p.position.getX() - box.max.getX()));
			gPreassure += 2.0 * fabs(p.velocity.getX());
		}
		if (p.position.getY() < box.min.getY()) {
			p.velocity.setY(-p.velocity.getY());
			p.position.setY(box.min.getY() + (box.min.getY() - p.position.getY()));
			gPreassure += 2.0 * fabs(p.velocity.getY());
		}
		if (p.position.getY() > box.max.getY()) {
			p.velocity.setY(-p.velocity.getY());
			p.position.setY(box.max.getY() - (p.position.getY() - box.max.getY()));
			gPreassure += 2.0 * fabs(p.velocity.getY());
		}
		return gPreassure;
	}

	/**
	 * The routine collide returns -1 if there will be no collision this time
	 * step, otherwise it will return when the collision occurs.
	 */
	public double collide(final Particle p1, final Particle p2) {
		double a, b, c;
		double temp, t1, t2;

		a = sqr(p1.velocity.getX() - p2.velocity.getX())
				+ sqr(p1.velocity.getY() - p2.velocity.getY());
		b = 2 * ((p1.position.getX() - p2.position.getX())
				* (p1.velocity.getX() - p2.velocity.getX()) + (p1.position.getY() - p2.position.getY())
				* (p1.velocity.getY() - p2.velocity.getY()));
		c = sqr(p1.position.getX() - p2.position.getX())
				+ sqr(p1.position.getY() - p2.position.getY()) - 4 * 1 * 1;

		if (a != 0.0) {
			temp = sqr(b) - 4 * a * c;
			if (temp >= 0) {
				temp = Math.sqrt(temp);
				t1 = (-b + temp) / (2 * a);
				t2 = (-b - temp) / (2 * a);

				if (t1 > t2) {
					temp = t1;
					t1 = t2;
					t2 = temp;
				}
				if ((t1 >= 0) & (t1 <= 1))
					return t1;
				else if ((t2 >= 0) & (t2 <= 1))
					return t2;
			}
		}
		return -1;
	}

	/** The routine interact moves two particles involved in a collision. */
	public void interact(final Particle p1, final Particle p2, final double t) {
		double c, s, a, b, tao;
		final Particle p1temp = new Particle(v(0, 0), v(0, 0));
		final Particle p2temp = new Particle(v(0, 0), v(0, 0));

		if (t >= 0) {

			/* Move to impact point */
			euler(p1, t);
			euler(p2, t);

			/* Rotate the coordinate system around p1 */
			p2temp.position.setX(p2.position.getX() - p1.position.getX());
			p2temp.position.setY(p2.position.getY() - p1.position.getY());

			/* Givens plane rotation, Golub, van Loan p. 216 */
			a = p2temp.position.getX();
			b = p2temp.position.getY();
			if (p2.position.getY() == 0) {
				c = 1;
				s = 0;
			} else {
				if (fabs(b) > fabs(a)) {
					tao = -a / b;
					s = 1 / (Math.sqrt(1 + sqr(tao)));
					c = s * tao;
				} else {
					tao = -b / a;
					c = 1 / (Math.sqrt(1 + sqr(tao)));
					s = c * tao;
				}
			}

			/* This should be equal to 2r */
			p2temp.position.setX(c * p2temp.position.getX() + s * p2temp.position.getY());
			p2temp.position.setY(0.0);

			p2temp.velocity.setX(c * p2.velocity.getX() + s * p2.velocity.getY());
			p2temp.velocity.setY(-s * p2.velocity.getX() + c * p2.velocity.getY());
			p1temp.velocity.setX(c * p1.velocity.getX() + s * p1.velocity.getY());
			p1temp.velocity.setY(-s * p1.velocity.getX() + c * p1.velocity.getY());

			/* Assume the balls has the same mass... */
			p1temp.velocity.setX(-p1temp.velocity.getX());
			p2temp.velocity.setX(-p2temp.velocity.getX());

			p1.velocity.setX(c * p1temp.velocity.getX() - s * p1temp.velocity.getY());
			p1.velocity.setY(s * p1temp.velocity.getX() + c * p1temp.velocity.getY());
			p2.velocity.setX(c * p2temp.velocity.getX() - s * p2temp.velocity.getY());
			p2.velocity.setY(s * p2temp.velocity.getX() + c * p2temp.velocity.getY());

			/* Move the balls the remaining time. */
			c = 1.0 - t;
			euler(p1, c);
			euler(p2, c);
		}
	}
}
