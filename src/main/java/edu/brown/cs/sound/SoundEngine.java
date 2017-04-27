package edu.brown.cs.sound;

import java.util.ArrayList;
import java.util.List;

public class SoundEngine {

	private double id;
	private SoundRead sr;
	
	
	public SoundEngine() {	
	}

	public void uploadSoundData(String filename, double id, double frameRate){
		this.id = id;
		//get sound audio information
		sr = new SoundRead(frameRate);
		sr.read(filename);
	}
	
	public enum SoundParam {
		AMPLITUDE,FREQUENCY,PITCH
	}
	
	public List<Double> getMetaData(SoundParam sp) {
		List<Double> toReturn = new ArrayList<Double>();
		switch(sp){
		case AMPLITUDE:
			break;
		case FREQUENCY:
			break;
		case PITCH:
			break;
		}
		return toReturn;
		
	}
	
	private List<Double> getFreqData(){
		return sr.getFrequencyData();
	}
	
	
	
	

}
