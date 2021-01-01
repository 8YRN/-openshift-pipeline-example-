
package com.appspot.datastore;

import com.appspot.analyser.Analyser;

public class SphereInfo {
	private double currentRatio;
	private double targetRatio;
	private double sphereTotalTime;

	public SphereInfo(double currentRatio, double targetRatio, double totalTime) {
		this.currentRatio = currentRatio;
		this.targetRatio = targetRatio;
		this.sphereTotalTime = totalTime;
	}

	public double getTargetRatio() {
		return targetRatio;
	}
