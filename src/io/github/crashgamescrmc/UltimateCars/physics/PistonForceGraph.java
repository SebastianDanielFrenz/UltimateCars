package io.github.crashgamescrmc.UltimateCars.physics;

public class PistonForceGraph {

	private double[] RPMs;
	private double[] torques;

	public PistonForceGraph(double[] RPMs, double[] torques) {
		this.RPMs = RPMs;
		this.torques = torques;
	}

	public double getTorque(double RPM) {
		if (RPM > RPMs[0]) {
			return torques[0];
		}
		for (int i = 1; i < torques.length - 1; i++) {
			if (RPM > RPMs[i]) {
				return torques[i] + ((torques[i + 1] - torques[i]) / (RPMs[i + 1] - RPMs[i]) * (RPM - RPMs[i]));
			}
		}
		return 0;
	}

	public double[] getRPMs() {
		return RPMs;
	}

	public void setRPMs(double[] rPMs) {
		RPMs = rPMs;
	}

	public double[] getTorques() {
		return torques;
	}

	public void setTorques(double[] torques) {
		this.torques = torques;
	}

}
