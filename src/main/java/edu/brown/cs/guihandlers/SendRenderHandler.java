package edu.brown.cs.guihandlers;

import edu.brown.cs.soundpaint.GuiProcessor;
import edu.brown.cs.tratchfo.SoundRead;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class SendRenderHandler implements Route {
  
  private GuiProcessor guiProcessor;
  
  public SendRenderHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  
  @Override
  public Object handle(Request req, Response response) throws Exception {
    //String audio = req.raw().getParameter("fileName");
    //System.out.println(audio);
    //SoundRead soundReader = new SoundRead();
    //soundReader.read(filename);
    
    System.out.println("HERE");
    
    System.out.println(req.queryParams("audioFile"));
    return null;
  }

}
