package edu.brown.cs.sound;

import java.util.ArrayList;
import java.util.List;

public class SoundEngine {

	private double id;
	private String path;
	private SoundRead sr = null;
	
	
	public SoundEngine(String path) {
		//TODO: save metadata to database appropriately
		this.path = path;
	}
	
	public List<Double> getMetaData(SoundParameter sp) {
		List<Double> toReturn = new ArrayList<Double>();
		switch(sp){
		case AMPLITUDE:
			toReturn = getAmpData();
			break;
		case FREQUENCY:
			toReturn = getFreqData();
			break;
		case TEMPO:
			toReturn = getTempoData();
			break;
		}
		return toReturn;
		
	}

	public void setSoundReader(double framerate) {
		sr = new SoundRead(framerate);
		sr.read(path);
	}
	
	private List<Double> getFreqData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return sr.getScaledFrequencyData();
		}
	}
	
	private List<Double> getAmpData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return sr.getScaledAmplitudeData();
		}
	}
	
	private List<Double> getTempoData() {
		if(sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return sr.getScaledTempoData();
		}
	}
	/**
	 * Sends sound meta-data to database
	 */
	public void sendToDataBase() {
		//TODO: Fill this function!
		//remember to make make class that packages information in JSON
	}
}
