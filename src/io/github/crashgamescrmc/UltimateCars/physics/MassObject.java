package io.github.crashgamescrmc.UltimateCars.physics;

import org.bukkit.util.Vector;

public class MassObject {

	public MassObject(double mass) {
		this.mass = mass;
		setVelocity(new Vector());
		setAcceleration(new Vector());
	}

	protected double mass;
	protected Vector velocity;
	protected Vector acceleration;

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public Vector getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getMass() {
		return mass;
	}

	/**
	 * <img src=
	 * "https://wikimedia.org/api/rest_v1/media/math/render/svg/5b3bf12f95f6d0174755a9248ba34e638cf90310">
	 * 
	 * @param density
	 * @param velocity
	 * @param coefficient
	 * @param area
	 * @return
	 */
	public static double Drag(double density, double velocity, double coefficient, double area) {
		return 0.5 * density * velocity * velocity * coefficient * area;
	}
}