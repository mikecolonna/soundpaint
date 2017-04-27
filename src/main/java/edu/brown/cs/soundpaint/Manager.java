package edu.brown.cs.soundpaint;

import edu.brown.cs.tratchfo.SoundParameter;
import edu.brown.cs.video.*;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.tratchfo.SoundRead;

import org.bytedeco.javacv.*;

import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import org.bytedeco.javacv.FrameGrabber.Exception;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by tynan on 4/15/17.
 */
public class Manager {


  //TODO: Find a way to move all these commands and handlers to separate modules

  private FilterProcessor filterProcessor;
  private List<BufferedImage> sequence;

  /**
   * Returns the Pattern-Command mapping that defines the soundpaint
   * program's CLI behavior.
   *
   * @return A map of all the Patterns and Commands that need to be recognized
   * by the CLI
   */
  public Map<Pattern, Command> getPatternCommandMap() {
    return new ImmutableMap.Builder<Pattern, Command>()
        .put(Pattern.compile("help"), this::helpCommand)
        .put(Pattern.compile("sequence\\s+(.+)"), this::sequenceCommand)
        .put(Pattern.compile("filter\\s+\"(.*?)\""), this::filterCommand)
        .put(Pattern.compile("process\\s+(.+)"), this::processCommand)
        .put(Pattern.compile("sound\\s+(.+)"), this::soundCommand)
        .put(Pattern.compile("render\\s+(.+)"), this::renderCommand)
        .build();
  }

  public void helpCommand(List<String> tokens, String cmd) {
    System.out.println("Welcome to SoundPaint's command line interface.");
    System.out.println("When commands are made available for use, they will be"
        + " listed here");
  }

  public void sequenceCommand(List<String> tokens, String cmd) {

    if (tokens.size() == 3) {
      String outputPath = tokens.get(2);
      if (outputPath.charAt(outputPath.length() - 1) != '/') {
        System.out.println("ERROR: Invalid output path. Must be a directory, append a slash to the end.");
        return;
      }

      sequence = BitmapSequence.getBitmapSequenceFromPath(tokens.get(1));

      new File(outputPath).mkdir();
      for (int i = 0; i < sequence.size(); i++) {
        String path = outputPath + i + ".png";
        try {
          ImageIO.write(sequence.get(i),"png", new File(path));
        } catch (IOException e) {
          System.out.println("ERROR: Could not write an image to disk.");
        }
      }

      System.out.println("Bitmap sequence rendered.");
    } else {
      System.out.println("ERROR: Please input two arguments to the sequence command.");
    }
  }

  public void soundCommand(List<String> tokens, String cmd) {
	  if (tokens.size() == 2) {	     
	   //read file
		  SoundRead sr = new SoundRead();
		  sr.read(tokens.get(1));

	  }else {
	      System.out.println("ERROR: Please input an arguments to the sequence command.");
	  }
  }

  
  public void filterCommand(List<String> tokens, String cmd) {
    if (tokens.size() >= 2) {
      String filters = tokens.get(1);
      filters = filters.substring(1, filters.length() - 1);
      
      System.out.println(filters);
      
      filterProcessor = new FilterProcessor(filters);
      //filterProcessor.add(new GrayscaleFilter());
      System.out.printf("Filter set to %s.\n", filters);
    } else {
      System.out.println(
          "ERROR: Please input at least 1 argument to the 'filter' command.");
    }
  }
  
  public void processCommand(List<String> tokens, String cmd) {
    if (filterProcessor == null) {
      System.out.println("ERROR: Please specify filters using the 'filter' command.");
      return;
    }
    
    if (tokens.size() == 3) {
      String inputPath = tokens.get(1);
      String outputPath = tokens.get(2);
      
      filterProcessor.process(inputPath, outputPath);
    } else {
      System.out.println("ERROR: Please input 2 arguments to the 'process' command.");
    }
  }

  public void renderCommand(List<String> tokens, String cmd) {

    Map<SoundParameter, VideoFilterSpecification> filterMap = new HashMap<>();
    VideoFilterSpecification colorSpec = new VideoFilterSpecification(VideoParameter.COLOR, 1.0);

    filterMap.put(null, colorSpec);

    if (tokens.size() == 2) {
      RenderEngine.renderVideo(filterMap, new FFmpegFrameGrabber(tokens.get(1)));
    } else {
      System.out.println("ERROR: Please input 2 arguments to the 'process' command.");
    }
  }
  
  

}
