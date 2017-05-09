package edu.brown.cs.sound;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.AudioDBProxy;
import edu.brown.cs.database.Database;

public class SoundEngine {

	private String audio_id;
	private String path;
	private SoundRead sr = null;
	List<Double> ampData = new ArrayList<Double>();
	List<Double> freqData = new ArrayList<Double>();
	List<Double> tempoData = new ArrayList<Double>();
	List<float []> animationFreqData = new ArrayList<float []>();
	//the lowest number for scaled values
	private double SCALE_BOUND_LOW = 0;

	//the highest number for scaled values
	private double SCALE_BOUND_HIGH = 1;


	public SoundEngine(String path) {
		//TODO: save metadata to database appropriately
		this.path = path;
	}

	public void setAudioId(String a_id) {
		this.audio_id = a_id;
	}

	public List<Double> getMetaData(SoundParameter sp) {
		List<Double> toReturn = new ArrayList<Double>();
		switch(sp){
			case GENERAL_AMPLITUDE:
				toReturn = getGeneralAmpData();
				ampData = toReturn;
				break;
			case SPECIFIC_AMPLITUDE:
				toReturn = getSpecificAmpData();
				ampData = toReturn;
				break;
			case FREQUENCY:
				toReturn = getFreqData();
				freqData = toReturn;
				break;
			case TEMPO:
				toReturn = getTempoData();
				tempoData = toReturn;
				break;
		}
		return toReturn;

	}

	public void setSoundReader(double framerate) {
		sr = new SoundRead(framerate);
		sr.read(path);
		ampData = getGeneralAmpData();
		freqData = getFreqData();
		tempoData = getTempoData();
		animationFreqData = getAnimationData();

	}

	private List<float []> getAnimationData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return sr.getAnimationData();
		}
	}

	private List<Double> getFreqData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return scaleData(sr.getFrequenciesByVideoFrame());
		}
	}

	private List<Double> getGeneralAmpData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return scaleData(sr.getGeneralAmplitudesByVideoFrame());
		}
	}

	private List<Double> getSpecificAmpData() {
		if (sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return scaleData(sr.getSpecificAmplitudesByVideoFrame());
		}
	}

	private List<Double> getTempoData() {
		if(sr == null) {
			System.out.println("ERROR: Set a sound reader for the framerate.");
			return null;
		} else {
			return scaleData(sr.getBeatsByVideoFrame());
		}
	}

	/**
	 * Scales sound data within pre-
	 * determined scaleMag
	 * @param toScale - list of data to be scaled
	 * @return scaled data
	 */
	private List<Double> scaleData(List<Double> toScale) {

		double max = findMaxValue(toScale);
		double min = findMinValue(toScale);

		double scaleFactor = SCALE_BOUND_HIGH/(max - min);
		List<Double> toReturn = new ArrayList<Double>();
		for(double d: toScale){
			toReturn.add(((d - min) + SCALE_BOUND_LOW)*scaleFactor);
		}
		return toReturn;

	}

	private double findMaxValue(List<Double> lst) {
		Double max = Double.MIN_VALUE;
		for(double d: lst) {
			if(d > max) {
				max = d;
			}
		}

		return max;
	}

	private double findMinValue(List<Double> lst) {
		Double min = Double.MAX_VALUE;
		for(double d: lst) {
			if(d < min) {
				min = d;
			}
		}

		return min;
	}

	public JsonObject getAnimationAsJson() {
		return JSONBuilder.convert(animationFreqData);
	}

}