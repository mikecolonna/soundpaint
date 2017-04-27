package edu.brown.cs.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.pitch.AMDF;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;

public class SoundRead {
	
	//original sample rate of uploaded audio file
	private double origSampleRate = 0;
	
	//the amount of seconds needed needed to be 
	//sampled for video frame rate
	private double timeResolution;
	
	//the amount of sound frames that 
	//correspond to the timeResolution
	private int numFramesTime = 0;
	
	//the total number of frames in the uploaded audio file
	private int totalNumFrames = 0;
	
	//the tightest/smallest bound for the scaled 
	//audio values to fit into
	private double scaleMag = 1;
	
	//List of amplitudes from audio file in db (scaled)
	private List<Double> ampData = new ArrayList<Double>();
	
	//List of frequency data from audio file in kilohertz(scaled)
	private List<Double> freqData = new ArrayList<Double>();
	
	//List of tempoData from audio file in beats per minute (scaled)
	private List<Double> tempoData = new ArrayList<Double>();
	
	
	/**
	 * SoundRead is a class that calculates the 
	 * meta-data(frequency,tempo, and amplitude) from audio files 
	 * @param frameRate - video frame sample rate for audio data to be sampled
	 */
	public SoundRead(double frameRate) {
		this.timeResolution = frameRate;
	}
	
	
	/**
	 * Takes in audio filepaths for mp3 (coming soon), wav, and midi
	 * files and uploads their data
	 * @param filename - filepath for audio file
	 */
	public void read(String filename) {
		
		//splitting input to find file type
		String [] splitInput = filename.split("/");
	    String fileName = splitInput[splitInput.length - 1];
	     
	    if(fileName.contains(".wav")) {
	    	  readWav(filename);
	    }else if(fileName.contains(".mid")) {
	    	  readMidi(filename);
	    }else if(fileName.contains(".mp3")) {
	    		readMp(filename); 
	    }else{
	    	System.out.println("ERROR: File is not a midi or wave file");
	    }
	}
	//TODO: Abstract read functions into classes, 
	//and make an interface for frequency and tempo collection
	/**
	 * Reads audio file and call functions to 
	 * collect meta-data
	 * @param filename - audio file path
	 */
	private void readMp(String filename) {
		
	}
	/**
	 * Reads audio file and call functions to 
	 * collect meta-data
	 * @param filename - audio file path
	 */
	private void readMidi(String filename){
		
	}
	
	/**
	 * Reads audio file and call functions to 
	 * collect meta-data
	 * @param filename - audio file path
	 */
	private void readWav(String filename){
		try
	      {
	         // Open the wav file specified as the first argument
	         WavFile wavFile = WavFile.openWavFile(new File(filename));

	         // Display information about the wav file
	         wavFile.display();

	         // Get the number of audio channels in the wav file
	         int numChannels = wavFile.getNumChannels();

	         // Create a buffer of 100 frames
	         double[] buffer = new double[100 * numChannels];

	         int framesRead;
	         double min = Double.MAX_VALUE;
	         double max = Double.MIN_VALUE;
	         
	         double sampleRate = wavFile.getSampleRate();
	         origSampleRate = sampleRate;
	         
	         int count = 0;
	         
	         do
	         {
	            // Read frames into buffer
	            framesRead = wavFile.readFrames(buffer, 100);

	            
	            // Loop through frames and look for minimum and maximum value
	          
	            for (int s=0 ; s<framesRead * numChannels ; s++)
	            {
	            	//checks to find amount of audio frames that correlate to 
	            	//amount of video frames
	            	if(count/sampleRate <= timeResolution+(1/sampleRate) && 
	            			count/sampleRate > timeResolution) {
	            		numFramesTime = count;
	            	}
	            	
	            	//add amplitude data
	            	ampData.add((buffer[s]));

	               if (buffer[s] > max) {
	            	   max = buffer[s];
	            	
	               }
	               if (buffer[s] < min) {
	            	   min = buffer[s];
	               }
	               count++;
	            }
	         }
	         while (framesRead != 0);
	         
	         //set total amount of frame sin audio file
	         totalNumFrames = count;
	        
	         // Close the wavFile
	         wavFile.close();
	         
	         //get rest of meta data, this populates internal instance variables
					getScaledFrequencyData();
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
	}
	
	
	/**
	 * Returns amplitude data 
	 * @return
	 */
	public List<Double> getScaledAmplitudeData() {
		return scaleData(freqData);
	}
	/**
	 * Returns tempo data
	 * @return
	 */
	public List<Double> getScaledTempoData() {
		return scaleData(tempoData);
	}
	/**
	 * Collects and sets frequency and tempo data
	 * @return
	 */
	public List<Double> getScaledFrequencyData() {

		//check if frequency Data is cached 
		if(freqData.isEmpty()) {
			
			//define overlap of frame sound samples
			double overlap = 0.5;
			
			//List of video frame chunks/ frames that have 
			// been transformed by fft
			List<float []> fftData = new ArrayList<float []>();
			
			//counter to find the beginning of every video frame sample
			int lastAdd = 0;
			
			//loops through every chunk of video frame data from sound data
			while(lastAdd+(numFramesTime-1) < totalNumFrames){
					
					//define video frame rate chunk 
					float [] toAdd = new float [numFramesTime];
					
					//add sound data into video frame rate chunk
					for(int i = 0; i < numFramesTime;i++) {
						toAdd[i] = ampData.get(lastAdd + i).floatValue();
					}
					
					//add video chunk to fft samples list
					fftData.add(toAdd);
					
					//find next start of video frame chunk
					lastAdd = (int) Math.ceil(lastAdd+(numFramesTime-1)*overlap) - 1;
				
				
			}
			
			//List of FFT Objects indexed according to fftData
			List<FFT> fftList = new ArrayList<FFT>();
		
			//loop through each video chunk to transform via fft the 
			//sound data in each chunk
			for(float [] f: fftData) {
				
				//Define FFT object
				FFT transform = new FFT(f.length,new HammingWindow());
				
				//FFT forward transform on f in fftData
				transform.forwardTransform(f);
				
				//add FFT object to list
				fftList.add(transform);
			}
			
			//loop through fft samples
			for(int i = 0; i < fftData.size();i++) {
				//freq is in kilohertz
				double toAdd = fftList.get(i).binToHz(findMaxIndex(fftData.get(i)), 
						(float)origSampleRate);
				
				//tempo defined as beats per minute
				//1 khz is equal to 60000 beats per minute
				tempoData.add(toAdd*60000);

				freqData.add(toAdd);
			}
		}

		return scaleData(freqData);
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
		double sub = min;
		if(min > 0) {
			sub = -min;
		}

		double scaleFactor = Math.abs(scaleMag)/(max - min);
		List<Double> toReturn = new ArrayList<Double>();
		for(double d: toScale){
			toReturn.add((d + sub)*scaleFactor);
		}
		return toReturn;
		
	}
	
	private int findMaxIndex(float[] data){
		float max = Float.MIN_VALUE;
		int toReturn = -1;
		//only look through half of data because of nyquist
		for(int i = 0; i < (data.length/2); i++){
			if(data[i] > max){
				max = data[i];
				toReturn = i;
			}
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
	
	

}
