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
		//all meta-data is set
		sr.read(filename);
	}
	
	public enum SoundParam {
		AMPLITUDE,FREQUENCY,TEMPO
	}
	
	public List<Double> getMetaData(SoundParam sp) {
		List<Double> toReturn = new ArrayList<Double>();
		switch(sp){
		case AMPLITUDE:
			toReturn = getAmpData();
			break;
		case FREQUENCY:
			toReturn = getFreqData();
			break;
		case TEMPO:
			break;
		}
		return toReturn;
		
	}
	
	private List<Double> getFreqData() {
		return sr.getFrequencyData();
	}
	
	private List<Double> getAmpData() {
		return sr.getAmplitudeData();
	}
	/**
	 * Sends sound meta-data to database
	 */
	public void sendToDataBase() {
		//TODO: Fill this function!
		//remember to make make class that packages information in JSON
	}
	
	
	
	

}
