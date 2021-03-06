package com.okayboom.particlesim.physics;

public class Physics {

	public static final double NO_MOMENTUM = 0;
	public static final double NO_COLLISION = -1;

	private double fabs(double n) {
		return n < 0 ? -n : n;
	}

	private double sqr(double n) {
		return n * n;
	}

	/** Moves the a particle. */
	public int euler(Particle a, double time_step) {
		a.position.x = a.position.x + time_step * a.velocity.x;
		a.position.y = a.position.y + time_step * a.velocity.y;
		return 0;
	}

	/**
	 * wall_collide checks if a particle has exceeded the boundary and returns a
	 * momentum. Use this momentum to calculate the pressure.
	 */
	public double wall_collide(Particle p, Box box) {
		double gPreassure = 0.0;

		if (p.position.x < box.a.x) {
			p.velocity.x = -p.velocity.x;
			p.position.x = box.a.x + (box.a.x - p.position.x);
			gPreassure += 2.0 * fabs(p.velocity.x);
		}
		if (p.position.x > box.b.x) {
			p.velocity.x = -p.velocity.x;
			p.position.x = box.b.x - (p.position.x - box.b.x);
			gPreassure += 2.0 * fabs(p.velocity.x);
		}
		if (p.position.y < box.a.y) {
			p.velocity.y = -p.velocity.y;
			p.position.y = box.a.y + (box.a.y - p.position.y);
			gPreassure += 2.0 * fabs(p.velocity.y);
		}
		if (p.position.y > box.b.y) {
			p.velocity.y = -p.velocity.y;
			p.position.y = box.b.y - (p.position.y - box.b.y);
			gPreassure += 2.0 * fabs(p.velocity.y);
		}
		return gPreassure;
	}

	/**
	 * The routine collide returns -1 if there will be no collision this time
	 * step, otherwise it will return when the collision occurs.
	 */
	public double collide(Particle p1, Particle p2) {
		double a, b, c;
		double temp, t1, t2;

		a = sqr(p1.velocity.x - p2.velocity.x)
				+ sqr(p1.velocity.y - p2.velocity.y);
		b = 2 * ((p1.position.x - p2.position.x)
				* (p1.velocity.x - p2.velocity.x) + (p1.position.y - p2.position.y)
				* (p1.velocity.y - p2.velocity.y));
		c = sqr(p1.position.x - p2.position.x)
				+ sqr(p1.position.y - p2.position.y) - 4 * 1 * 1;

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
	public void interact(Particle p1, Particle p2, double t) {
		double c, s, a, b, tao;
		Particle p1temp = new Particle(new Vector(0, 0), new Vector(0, 0));
		Particle p2temp = new Particle(new Vector(0, 0), new Vector(0, 0));

		if (t >= 0) {

			/* Move to impact point */
			euler(p1, t);
			euler(p2, t);

			/* Rotate the coordinate system around p1 */
			p2temp.position.x = p2.position.x - p1.position.x;
			p2temp.position.y = p2.position.y - p1.position.y;

			/* Givens plane rotation, Golub, van Loan p. 216 */
			a = p2temp.position.x;
			b = p2temp.position.y;
			if (p2.position.y == 0) {
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
			p2temp.position.x = c * p2temp.position.x + s * p2temp.position.y;
			p2temp.position.y = 0.0;

			p2temp.velocity.x = c * p2.velocity.x + s * p2.velocity.y;
			p2temp.velocity.y = -s * p2.velocity.x + c * p2.velocity.y;
			p1temp.velocity.x = c * p1.velocity.x + s * p1.velocity.y;
			p1temp.velocity.y = -s * p1.velocity.x + c * p1.velocity.y;

			/* Assume the balls has the same mass... */
			p1temp.velocity.x = -p1temp.velocity.x;
			p2temp.velocity.x = -p2temp.velocity.x;

			p1.velocity.x = c * p1temp.velocity.x - s * p1temp.velocity.y;
			p1.velocity.y = s * p1temp.velocity.x + c * p1temp.velocity.y;
			p2.velocity.x = c * p2temp.velocity.x - s * p2temp.velocity.y;
			p2.velocity.y = s * p2temp.velocity.x + c * p2temp.velocity.y;

			/* Move the balls the remaining time. */
			c = 1.0 - t;
			euler(p1, c);
			euler(p2, c);
		}
	}
}
