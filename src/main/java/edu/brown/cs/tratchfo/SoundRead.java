package edu.brown.cs.tratchfo;

import java.io.File;

public class SoundRead {

	
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

}
