package edu.brown.cs.sound;

public class Frequency extends SoundParameter {
	
	private double value;
	
	public Frequency(double value) {
		super(value);
	}
	
	public double getValue() {
		return this.value;
	}
	
}
