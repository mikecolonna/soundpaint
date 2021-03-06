package edu.brown.cs.sound;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import be.tarsos.dsp.pitch.AMDF;
import be.tarsos.dsp.util.fft.FFT;
import be.tarsos.dsp.util.fft.HammingWindow;

public class SoundRead {
	public SoundRead () {}
	private int PEAK_WINDOW_WIDTH = 50;

	//original sample rate of uploaded audio file
	private double origSampleRate = 0;

	//the amount of seconds needed to be
	//sampled for video frame rate
	private double timeResolution;

	//the amount of sound frames that 
	//correspond to the timeResolution
	protected int soundFramesPerVideoFrame = 0;

	//the total number of frames in the uploaded audio file
	private int totalSoundFrames = 0;

	private double animationTimeResolution = (1.0/60.0);
	private int animationNumFramesTime = 736;
	List<float []> animationfftData = new ArrayList<float []>();

	private boolean metadataPopulated = false;

	//List of amplitudes from audio file in db (scaled)
	private List<Double> rawAmplitudes = new ArrayList<>();

	private double rawAmplitudeSum;

	//List of frequency data from audio file in kilohertz(scaled)
	private List<Double> frequenciesByVideoFrame = new ArrayList<>();

	//List of tempoData from audio file in beats per minute (scaled)
	private List<Double> beatsByVideoFrame = new ArrayList<>();

	private List<Double> specificAmplitudesByVideoFrame = new ArrayList<Double>();

	private List<Double> generalAmplitudesByVideoFrame = new ArrayList<>();

	//highest amplitude to be considered a beat
	private double highBeatAmp = 1;

	//lowest amplitude to be considered a beat
	private double lowBeatAmp = 0;


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
		//MP3File.openMP3File(new File(filename));
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

			int frameCount = 0;
			float [] toAdd =  new float [animationNumFramesTime];
			do {
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);

				// Loop through frames and look for minimum and maximum value
				for (int s = 0; s < framesRead * numChannels; s++) {

					toAdd[(frameCount % animationNumFramesTime)] = (float) buffer[s];

					//checks to find amount of audio frames that correlate to
					//amount of video frames
					if (frameCount / sampleRate <= timeResolution + (1/sampleRate) &&
							frameCount / sampleRate > timeResolution) {
						soundFramesPerVideoFrame = frameCount;
					}

					if((frameCount % animationNumFramesTime == 0) && frameCount != 0) {
						FFT transform = new FFT(toAdd.length,new HammingWindow());

						transform.forwardTransform(toAdd);

						animationfftData.add(toAdd);
						toAdd = new float [animationNumFramesTime];
					}

					//add amplitude data
					rawAmplitudes.add((buffer[s]));
					rawAmplitudeSum += Math.abs(buffer[s]);

					if (buffer[s] > max) {
						max = buffer[s];

					}
					if (buffer[s] < min) {
						min = buffer[s];
					}
					frameCount++;
				}
			} while (framesRead != 0);

			System.out.println("min: " + min + " max: " + max);

			//set total amount of frames in audio file
			totalSoundFrames = frameCount;
			// Close the wavFile
			wavFile.close();

			//get rest of meta data, this populates internal instance variables
			populateMetadata();
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}


	public void populateMetadata() {

		if (soundFramesPerVideoFrame == 0) {
			metadataPopulated = true;
		}

		if (!metadataPopulated) {

			//List of video frame chunks/ frames that have
			// been transformed by fft
			List<float []> sampleValuesByVideoFrame = new ArrayList<>();

			//counter to find the beginning of every video frame sample
			int lastAddedSoundFrameIndex = 0;

			//loops through every chunk of video frame data from sound data
			Deque<Double> precedingFramesAmplitudeDeque = new ArrayDeque<>();

			while(lastAddedSoundFrameIndex + (soundFramesPerVideoFrame - 1) < totalSoundFrames){
				//	System.out.println(lastAdd + " / " + totalNumFrames);
				//define video frame rate chunk
				float[] sampleValue = new float [soundFramesPerVideoFrame ];

				double averageLoudness = 0;

				double frameAmplitudeSum = 0;

				for(int i = 0; i < soundFramesPerVideoFrame ;i++) {
					sampleValue[i] = rawAmplitudes.get(lastAddedSoundFrameIndex + i).floatValue();
					double amplitudeSampleValue = Math.abs(sampleValue[i]);
					frameAmplitudeSum += amplitudeSampleValue;
					averageLoudness += Math.pow(amplitudeSampleValue, 2);
				}

				double precedingFramesAmplitudeSum = 0;

				for (Double value : precedingFramesAmplitudeDeque) {
					precedingFramesAmplitudeSum += value;
				}

				double precedingframesAmplitudeAverage = precedingFramesAmplitudeSum / precedingFramesAmplitudeDeque.size();
				double frameAmplitudeAverage = frameAmplitudeSum / soundFramesPerVideoFrame;


				if (frameAmplitudeAverage > precedingframesAmplitudeAverage) {
					beatsByVideoFrame.add(1.0);
				} else {
					beatsByVideoFrame.add(0.0);
				}

				precedingFramesAmplitudeDeque.addLast(frameAmplitudeAverage);

				if (precedingFramesAmplitudeDeque.size() > PEAK_WINDOW_WIDTH) {
					precedingFramesAmplitudeDeque.poll();
					if (precedingFramesAmplitudeDeque.size() > PEAK_WINDOW_WIDTH) {
						throw new IllegalArgumentException();
					}
				}

				// Taking the square root three times allows the data to fit the range better
				generalAmplitudesByVideoFrame.add(Math.sqrt(Math.sqrt(averageLoudness)));

				//add video chunk to fft samples list
				sampleValuesByVideoFrame.add(sampleValue);

				//find next start of video frame chunk
				lastAddedSoundFrameIndex = (int) Math.ceil(lastAddedSoundFrameIndex + (soundFramesPerVideoFrame - 1));
			}

			//loop through each video chunk to transform via fft the
			//sound data in each chunk
			int fn = 0;
			for(float [] f: sampleValuesByVideoFrame) {

				//Define FFT object
				FFT transform = new FFT(f.length,new HammingWindow());

				//FFT forward transform on f in fftData
				transform.forwardTransform(f);

				//find which index/freq has highest amp value
				int maxIndex = findMaxIndex(f);

				double frequencyWithHighestAmplitude = transform.binToHz(maxIndex,
						(float)origSampleRate);

				specificAmplitudesByVideoFrame.add((double) f[maxIndex]);

				frequenciesByVideoFrame.add(frequencyWithHighestAmplitude);
				fn++;
			}

			metadataPopulated = true;

			System.out.println("Done reading Sound Data ! ");


		}
	}

	/**
	 * Returns amplitude data 
	 * @return
	 */
	public List<Double> getSpecificAmplitudesByVideoFrame() {
		return specificAmplitudesByVideoFrame;
	}
	public List<float []> getAnimationData() {
		return animationfftData;
	}

	public List<Double> getGeneralAmplitudesByVideoFrame() {
		return generalAmplitudesByVideoFrame;
	}
	/**
	 * Returns tempo data
	 * @return
	 */
	public List<Double> getBeatsByVideoFrame() {
		return beatsByVideoFrame;
	}
	/**
	 * Collects and sets frequency and tempo data
	 * @return
	 */
	public List<Double> getFrequenciesByVideoFrame() {
		return frequenciesByVideoFrame;
	}

	private int findMaxIndex(float[] data){
		float max = -1;
		int toReturn = -1;
		//only look through half of data because of nyquist
		for(int i = 0; i < (data.length/2); i++) {
			if(data[i] > max){
				max = data[i];
				toReturn = i;
			}
		}



		return toReturn;
	}



}