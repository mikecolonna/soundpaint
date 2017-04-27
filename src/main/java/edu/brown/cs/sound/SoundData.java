package edu.brown.cs.sound;

public class SoundData {
	
	private double time;
	private SoundParameter param;
	
	public SoundData(double time, SoundParameter param) {
		this.time = time;
		this.param = param;
	}
	
	public double getTime() {
		return this.time;
	}
	
	public double  getValue() {
		return param.getValue();
	}

}
