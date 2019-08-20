package io.github.crashgamescrmc.UltimateCars.physics;

public class Engine {

	public Engine(PistonForceGraph torqueGraph, double engine_resistance, double piston_turn_radius) {
		setTorqueGraph(torqueGraph);
		setEngine_resistance(engine_resistance);
		setPiston_turn_radius(piston_turn_radius);
	}

	private PistonForceGraph torqueGraph;

	private double engine_resistance_coefficient;

	private double maxRPM;

	private double piston_turn_radius;

	private double current_rpm = 0;

	public void Update(double time) {
		double engine_force = current_rpm / 60 / piston_turn_radius;
		
	}

	public double getPower(double rpm) {
		return getTorque(rpm) * rpm / 60;
	}

	public double getTorque(double rpm) {
		return torqueGraph.getTorque(rpm);
	}

	public double getEngine_resistance() {
		return engine_resistance_coefficient;
	}

	public void setEngine_resistance(double engine_resistance) {
		this.engine_resistance_coefficient = engine_resistance;
	}

	public double getMaxRPM() {
		return maxRPM;
	}

	public void setMaxRPM(double maxRPM) {
		this.maxRPM = maxRPM;
	}

	public double getPiston_turn_radius() {
		return piston_turn_radius;
	}

	public void setPiston_turn_radius(double piston_turn_radius) {
		this.piston_turn_radius = piston_turn_radius;
	}

	public double getRpm() {
		return current_rpm;
	}

	public void setRpm(double rpm) {
		this.current_rpm = rpm;
	}

	public PistonForceGraph getTorqueGraph() {
		return torqueGraph;
	}

	public void setTorqueGraph(PistonForceGraph torqueGraph) {
		this.torqueGraph = torqueGraph;
	}

	public double getCurrent_rpm() {
		return current_rpm;
	}

	public void setCurrent_rpm(double current_rpm) {
		this.current_rpm = current_rpm;
	}

	public double getCubic_capacity() {
		return cubic_capacity;
	}

	public void setCubic_capacity(double cubic_capacity) {
		this.cubic_capacity = cubic_capacity;
	}

}
