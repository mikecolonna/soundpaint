package edu.brown.cs.soundpaint;

import edu.brown.cs.database.Database;
import edu.brown.cs.sound.SoundEngine;
import edu.brown.cs.sound.SoundParameter;

import edu.brown.cs.video.*;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.sound.SoundRead;

import org.bytedeco.javacv.*;

import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FrameGrabber.Exception;
import java.nio.Buffer;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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
        .put(Pattern.compile("db\\s+(.+)"), this::dbCommand)
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

      sequence = BitmapSequence.getBitmapSequenceFromFrameGrabber(new FFmpegFrameGrabber(tokens.get(1)));

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
		//  SoundRead sr = new SoundRead((1.0/24.0));
		  //sr.read(tokens.get(1));
		  
		  SoundEngine se = new SoundEngine(tokens.get(1));
		  se.setSoundReader((1.0/24.0));
		 

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
      System.out.printf("FFmpegFilter set to %s.\n", filters);
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

    List<VideoSoundParameterMapping> mappings = new ArrayList<>();

///    mappings.add(new VideoSoundParameterMapping(VideoParameter.PUSH, SoundParameter.AMPLITUDE,1.0));

//    mappings.add(new VideoSoundParameterMapping(VideoParameter.EMBOSS, SoundParameter.AMPLITUDE,1.0));
    mappings.add(new VideoSoundParameterMapping(VideoParameter.TINT, SoundParameter.AMPLITUDE,1.0));
        mappings.add(new VideoSoundParameterMapping(VideoParameter.BULGE, SoundParameter.AMPLITUDE,1.0));

    if (tokens.size() == 3) {
      RenderEngine.renderVideo(mappings, new FFmpegFrameGrabber(tokens.get(1)), new SoundEngine(tokens.get(2)), "./testRender.mp4");
    } else {
      System.out.println("ERROR: Please input 2 arguments to the 'process' command.");
    }
  }
  
  public void dbCommand(List<String> tokens, String cmd) {
    String dbName;
    try {
      dbName = tokens.get(1);
    } catch (ArrayIndexOutOfBoundsException aioobe) {
      System.out.println("ERROR: No database specified.");
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException cnfe) {
      System.out.println("ERROR: Class not found.");
      return;
    }
    String urlToDb = "jdbc:sqlite:" + dbName;

    //set path to database
    Database.setPath(urlToDb);
    try (Connection conn = Database.getConnection()) {
      Statement stat = null;
      stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    } catch (SQLException sqle) {
      System.out.println("ERROR: Could not connect to database.");
      return;
    }
    
    Database.createTables();
    Database.resetCaches();
    Database.connected(true);
    
    System.out.println("db set to " + dbName);
  }
}
