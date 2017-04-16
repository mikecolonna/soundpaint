package edu.brown.cs.soundpaint;

import com.google.common.collect.ImmutableMap;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import org.opencv.core.Core;

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
        .build();
  }


  public void helpCommand(List<String> tokens, String cmd) {
    System.out.println("Welcome to SoundPaint's command line interface.");
    System.out.println("When commands are made available for use, they will be"
        + " listed here");
    System.out.println("OpenCV " + Core.VERSION);

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
