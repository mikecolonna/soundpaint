package edu.brown.cs.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;

public class SoundRead {
	
	private double origSampleRate = 0;
	private double timeResolution = (1.0/24.0);
	private int numFramesTime = 0;
	private int totalNumFrames = 0;
	private List<Double> ampData = new ArrayList<Double>();
	private List<Double> freqData = new ArrayList<Double>();
	
	public SoundRead(double framRate) {
		this.timeResolution = numFramesTime;
	}
	
	public void read(String filename){
		String [] splitInput = filename.split("/");
	    String fileName = splitInput[splitInput.length - 1];
	     
	    if(fileName.contains(".wav")){
	    	  readWav(filename);
	    }else if(fileName.contains(".mid")){
	    	  readMidi(filename);
	    }else{
	    	System.out.println("ERROR: File is not a midi or wave file");
	    }
	}
	
	private void readMidi(String filename){
		
	}
	
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
	         double maxTime = 0;
	         double minTime = 0;
	         int count = 0;
	         
	         do
	         {
	            // Read frames into buffer
	            framesRead = wavFile.readFrames(buffer, 100);

	            
	            // Loop through frames and look for minimum and maximum value
	          
	            for (int s=0 ; s<framesRead * numChannels ; s++)
	            {
	            	if(count/sampleRate <= timeResolution+(1/sampleRate) && 
	            			count/sampleRate > timeResolution) {
	            		System.out.println(count/sampleRate > timeResolution);
	            		numFramesTime = count;
	            	}
	            	ampData.add(new SoundData(count/sampleRate,new Amplitude(buffer[s])));
	            	
	            	
	               if (buffer[s] > max) {
	            	   max = buffer[s];
	            	   maxTime = count/sampleRate;
	            	
	               }
	               if (buffer[s] < min) {
	            	   min = buffer[s];
	            	   minTime = count/sampleRate;
	               }
	               count++;
	            }
	         }
	         while (framesRead != 0);
	         
	         totalNumFrames = count;
	        System.out.println("TOTAL LENGTH SECS: " + count/sampleRate);
	        
	         // Close the wavFile
	         wavFile.close();

	         // Output the minimum and maximum value
	         System.out.printf("Min: %f, Max: %f\n", min, max);
	         System.out.printf("MinTime: %f, MaxTime: %f\n", minTime, maxTime);
	         
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
	}
	//or maybe do time as input
	//if num samples is greater thna amount that can be given then 
	//overlap is given in a percentage amount (i.e .3 -> 30%)
	public List<Double> getFrequencyData() {
			double overlap = 0.5;
			List<float []> fftData = new ArrayList<float []>();
			int lastAdd = 0;
			System.out.println("LAST ADD: " + lastAdd +
					"numFramesTime: " + numFramesTime +
					"total num frames: " + totalNumFrames);
			while(lastAdd+(numFramesTime-1) < totalNumFrames){
					float [] toAdd = new float [numFramesTime];
					for(int i = 0; i < numFramesTime;i++) {
						toAdd[i] = (float)ampData.get(lastAdd + i).getValue();
					}
					fftData.add(toAdd);
					lastAdd = (int) Math.ceil(lastAdd+(numFramesTime-1)*overlap) - 1;
				
				
			}
			System.out.println("Done!sound");
			List<FFT> fftList = new ArrayList<FFT>();
			List<double []> allFreq = new ArrayList<double []>();
			for(float [] f: fftData) {
				FFT transform = new FFT(f.length,new HammingWindow());
				transform.forwardTransform(f);
				double [] freqSample = new double [f.length/2];
				for(int i = 0; i < freqSample.length; i++){
					freqSample[i] = transform.binToHz(i, 44100);
				}
				allFreq.add(freqSample);
				fftList.add(transform);
			}
			
		
			
			System.out.println("Number of bins (should be): " + numFramesTime/2);
			System.out.println("What it is: " + fftData.get(0).length);
			
			System.out.println("BIN VALUE FOR LAST BIN: " + fftList.get(0).binToHz(fftData.get(0).length -1, 44100));
			System.out.println("BIN VALUE FOR HALF BIN: " + fftList.get(0).binToHz(fftData.get(0).length/2, 44100));
			System.out.println("BIN VALUE FOR first  BIN: " + fftList.get(0).binToHz(1, 44100));
			
			for(int i = 0; i < fftData.size();i++){
				double toAdd = fftList.get(i).binToHz(findMax(fftData.get(i)), 
						(float)origSampleRate);
				freqData.add(toAdd);
			}
		
			
			
			
			
			
			
			return freqData;	
		
	}
	
	private int findMax(float[] data){
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
	
	

}
