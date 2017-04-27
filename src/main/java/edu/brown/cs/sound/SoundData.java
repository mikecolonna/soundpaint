package edu.brown.cs.sound;

public class SoundData {
	
	private double time;
	private double value;
	
	public SoundData(double time, double value) {
		this.time = time;
		this.value = value;
	}
	
	public double getTime() {
		return this.time;
	}
	
	public double  getValue() {
		return value;
	}

}
