package edu.brown.cs.guihandlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.soundpaint.GuiProcessor;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Handle requests to the front page of the GUI.
 */
public class FrontHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public FrontHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  
  @Override
  public ModelAndView handle(Request req, Response res) {
    String logged = "false";
    String seshId = req.session().id();
    String username = "";
    if(guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      logged = "true";
      username = req.session().attribute("username");
    }
    Map<String, Object> variables = ImmutableMap.of(
            "title", "Soundpaint - CS32 Final Project",
            "message","Created by Brendan, Mike, Tymani, and Tynan",
            "error", "", "logged", logged, "name", username);
    return new ModelAndView(variables, "home_news.ftl");
  }
}
