package edu.brown.cs.guihandlers;

import edu.brown.cs.soundpaint.GuiProcessor;
import spark.Request;
import spark.Response;
import spark.Route;

public class SendRenderHandler implements Route {
  
  private GuiProcessor guiProcessor;
  
  public SendRenderHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  

  @Override
  public Object handle(Request arg0, Response arg1) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

}
