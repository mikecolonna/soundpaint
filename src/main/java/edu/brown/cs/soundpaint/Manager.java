package edu.brown.cs.soundpaint;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.video.BitmapSequence;
import org.bytedeco.javacv.*;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.bytedeco.javacv.FrameGrabber.Exception;


import java.nio.Buffer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by tynan on 4/15/17.
 */
public class Manager {

  /** Installs all Spark routes.
   * @param fme the FreeMarkerEngine that some routes bind to.
   */
  public void installRoutes(FreeMarkerEngine fme) {
    Spark.get("/home", new FrontHandler(), fme);
  }

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

      List<BufferedImage> sequence = BitmapSequence.getBitmapSequenceFromPath(tokens.get(1));



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
  /**
   * Handle requests to the front page of the GUI.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Soundpaint - CS32 Final Project","message","Created by Brendan,"
              + " Mike, Tymani, and Tynan");
      return new ModelAndView(variables, "main.ftl");
    }
  }

}
