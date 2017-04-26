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
    System.out.println(req.session().id());
    req.session().attribute("username", "Brendan");
    String a = req.session().attribute("username");
    System.out.println(a);
    
    guiProcessor.getSessionsToUsers();
    Map<String, Object> variables = ImmutableMap.of(
        "title", "Soundpaint - CS32 Final Project",
        "message","Created by Brendan, Mike, Tymani, and Tynan");
    return new ModelAndView(variables, "home_news.ftl");
  }
}
