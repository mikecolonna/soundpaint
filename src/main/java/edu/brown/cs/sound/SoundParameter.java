package edu.brown.cs.sound;

public abstract class SoundParameter {
	
	private double value;
	
	public SoundParameter(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}

}
