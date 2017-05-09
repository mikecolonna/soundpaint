package edu.brown.cs.sound;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.AudioDBProxy;

public class SoundEngine {

	private double id;
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
		sendToDataBase();
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


	/**
	 * Sends sound meta-data to database
	 */
	private void sendToDataBase() {
		//remember to make make class that packages information in JSON
		String a_id = AudioDB.generateId();
		String v_id = "";
		String srcFilepath = path;
		File soundMetaDataDir = new File("soundMetaData");
		File ampDataF = new File("soundMetaData/ampData");
		File freqDataF = new File("soundMetaData/freqData");
		File tempoDataF = new File("soundMetaData/tempoData");
		File animationF = new File("soundMetaData/animationData");
		try{
			soundMetaDataDir.mkdir();
			ampDataF.mkdir();
			freqDataF.mkdir();
			tempoDataF.mkdir();
			animationF.mkdir();
		}catch(SecurityException e) {
			System.out.println("ERROR: Security Exception");
		}
		String ampFilePath = "soundMetaData/ampData/" + "amp_"+ a_id + ".txt";
		String freqFilePath = "soundMetaData/freqData/" + "freq_"+ a_id + ".txt";
		String tempoFilePath = "soundMetaData/tempoData/"+ "tempo_"+ a_id + ".txt";
		String animationFilePath = "soundMetaData/animationData/"+ "animation_"+ a_id + ".txt";
		//File for amp
		File ampFile =  new File(ampFilePath);
		if(!ampFile.exists()) {
			try {
				ampFile.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR: Cannot create new file.");
			}
		}
		//File for freq
		File freqFile = new File(freqFilePath);
		if(!freqFile.exists()) {
			try {
				freqFile.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR: Cannot create new file.");
			}
		}
		//File for tempo
		File tempoFile = new File(tempoFilePath);
		if(!tempoFile.exists()) {
			try {
				tempoFile.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR: Cannot create new file.");
			}
		}
		//File for animation freq
		File animationFile = new File(animationFilePath);
		if(!animationFile.exists()) {
			try {
				animationFile.createNewFile();
			} catch (IOException e) {
				System.out.println("ERROR: Cannot create new file.");
			}
		}
		JsonObject ampObj = JSONBuilder.convert(ampData);
		try (FileWriter file = new FileWriter(ampFilePath)) {
			file.write(ampObj.toString());
			System.out.println("Successfully Copied AMP JSON Object to File...");
		} catch (IOException e) {
			System.out.println("ERROR: Cannot write sound data to file");
		}
		JsonObject freqObj = JSONBuilder.convert(freqData);
		try (FileWriter file = new FileWriter(freqFilePath)) {
			file.write(freqObj.toString());
			System.out.println("Successfully Copied FREQ JSON Object to File...");
		} catch (IOException e) {
			System.out.println("ERROR: Cannot write sound data to file");
		}
		JsonObject tempoObj = JSONBuilder.convert(tempoData);
		try (FileWriter file = new FileWriter(tempoFilePath)) {
			file.write(tempoObj.toString());
			System.out.println("Successfully Copied TEMPO JSON Object to File...");
		} catch (IOException e) {
			System.out.println("ERROR: Cannot write sound data to file");
		}
		JsonObject animationObj = JSONBuilder.convert(animationFreqData);
		try (FileWriter file = new FileWriter(animationFilePath)) {
			file.write(animationObj.toString());
			System.out.println("Successfully Copied ANIMATION_FREQ JSON Object to File...");
		} catch (IOException e) {
			System.out.println("ERROR: Cannot write sound data to file");
		}

		String ampFilepath = "soundMetaData/ampData/"+ "amp_" + a_id + ".txt";
		String freqFilepath = "soundMetaData/freqData/"+ "freq_"+ a_id + ".txt";
		String tempoFilepath = "soundMetaData/tempoData/"+ "tempo_"+ a_id + ".txt";
		AudioDB.createAudio(a_id, v_id, srcFilepath,
				ampFilepath, freqFilepath,tempoFilepath);

		System.out.println("Done writing sound data :) ");


	}
}
