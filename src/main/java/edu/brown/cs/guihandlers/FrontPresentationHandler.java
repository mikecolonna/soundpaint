package edu.brown.cs.guihandlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.database.Database;
import edu.brown.cs.soundpaint.GuiProcessor;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class FrontPresentationHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public FrontPresentationHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    String logged = "false";
    String seshId = req.session().id();
    String username = "";
    if (guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      logged = "true";
      username = req.session().attribute("username");
    }
   String vid = req.params(":id");
   //use video id to query to video and its associated sound filepaths
    Map<String, Object> variables = ImmutableMap.of(
        "title", "Soundpaint - CS32 Final Project",
        "message","Created by Brendan, Mike, Tymani, and Tynan",
        "error", "", "name", username, "logged", logged);
    return new ModelAndView(variables, "presentation.ftl");
  }

}
