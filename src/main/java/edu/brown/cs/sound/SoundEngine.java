package edu.brown.cs.sound;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.AudioDBProxy;

public class SoundEngine {

	private double id;
	private String path;
	private SoundRead sr = null;
	List<Double> ampData = new ArrayList<Double>();
	List<Double> freqData = new ArrayList<Double>();
	List<Double> tempoData = new ArrayList<Double>();
	
	
	public SoundEngine(String path) {
		//TODO: save metadata to database appropriately
		this.path = path;
	}
	
	public List<Double> getMetaData(SoundParameter sp) {
		List<Double> toReturn = new ArrayList<Double>();
		switch(sp){
		case AMPLITUDE:
			toReturn = getAmpData();
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
		//sendToDataBase();
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
	private void sendToDataBase() {
		//TODO: Fill this function!
		//remember to make make class that packages information in JSON
		String a_id = AudioDB.generateId();
		String v_id = null;
		String srcFilepath = path;
		File soundMetaDataDir = new File("soundMetaData");
		File ampDataF = new File("ampData");
		File freqDataF = new File("freqData");
		File tempoDataF = new File("tempoData");
		try{
			soundMetaDataDir.mkdir();
			ampDataF.mkdir();
			freqDataF.mkdir();
			tempoDataF.mkdir();
		}catch(SecurityException e) {
			
		}
//		JSONObject ampObj = JSONBuilder.convert(ampData);
//		try (FileWriter file = new FileWriter("/soundMetaData/ampData/" + "amp_"+ a_id + ".txt")) {
//			file.write(ampObj.toJSONString());
//			System.out.println("Successfully Copied JSON Object to File...");
//			System.out.println("\nJSON Object: " + ampObj);
//		}
//		JSONObject freqObj = JSONBuilder.convert(freqData);
//		try (FileWriter file = new FileWriter("/soundMetaData/freqData/"+ "freq_"+ a_id + ".txt")) {
//			file.write(freqObj.toJSONString());
//			System.out.println("Successfully Copied JSON Object to File...");
//			System.out.println("\nJSON Object: " + freqObj);
//		}
//		JSONObject tempoObj = JSONBuilder.convert(tempoData);
//		try (FileWriter file = new FileWriter("/soundMetaData/freqData/"+ "tempo_"+ a_id + ".txt")) {
//			file.write(tempoObj.toJSONString());
//			System.out.println("Successfully Copied JSON Object to File...");
//			System.out.println("\nJSON Object: " + tempoObj);
//		}
//		
//	    String ampFilepath = "/soundMetaData/ampData/"+ "amp_" + a_id + ".txt";
//	    String freqFilepath = "/soundMetaData/freqData/"+ "freq_"+ a_id + ".txt";
//	    String tempoFilepath = "/soundMetaData/freqData/"+ "tempo_"+ a_id + ".txt";
//		AudioDB.createAudio(a_id, v_id, srcFilepath,
//			     ampFilepath, freqFilepath,tempoFilepath);
//	
			     
	}
}
