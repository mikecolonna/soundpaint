package edu.brown.cs.soundpaint;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class FrontProjectsHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public FrontProjectsHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    String seshId = req.session().id();
    if (guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      res.redirect("/");
      return null;
    }
    Map<String, Object> variables = ImmutableMap.of(
        "title", "Soundpaint - CS32 Final Project",
        "message","Created by Brendan, Mike, Tymani, and Tynan",
        "error", "");
    return new ModelAndView(variables, "projects.ftl");
  }
  
  

}
