package edu.brown.cs.guihandlers;

import edu.brown.cs.soundpaint.GuiProcessor;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class LogoutHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public LogoutHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }

  @Override
  public ModelAndView handle(Request req, Response response) throws Exception {
    String seshId = req.session().id();
    guiProcessor.getSessionsToUsers().remove(seshId);
    response.redirect("/");
    return null;
  }

}
