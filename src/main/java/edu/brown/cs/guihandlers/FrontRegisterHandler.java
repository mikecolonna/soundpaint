package edu.brown.cs.guihandlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.soundpaint.GuiProcessor;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class FrontRegisterHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public FrontRegisterHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  
  @Override
  public ModelAndView handle(Request req, Response res) {
    String seshId = req.session().id();
    if(guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      res.redirect("/");
      return null;
    }
    Map<String, Object> variables = ImmutableMap.of(
        "title", "Soundpaint - CS32 Final Project",
        "message","Created by Brendan, Mike, Tymani, and Tynan",
        "error", "");
    return new ModelAndView(variables, "register.ftl");
  }
}
