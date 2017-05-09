package edu.brown.cs.guihandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.database.Database;
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
  private Gson gson;
  
  public FrontHandler(GuiProcessor gp) {
    guiProcessor = gp;
    gson = new Gson();
  }
  
  @Override
  public ModelAndView handle(Request req, Response res) {
    if (!Database.isConnected()) {
      res.redirect("/error");
      return null;
    }
    
    String logged = "false";
    String seshId = req.session().id();
    String username = "";
    if (guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      logged = "true";
      username = req.session().attribute("username");
    }
    
    List<ThumbnailData> thumbData = Database.getPublicThumbnailFilepaths();
    
    Map<String, Object> variables = ImmutableMap.of(
            "title", "Soundpaint - CS32 Final Project",
            "error", "", "logged", logged, "name", username,
            "thumbs", thumbData);
    return new ModelAndView(variables, "home_news.ftl");
  }
}
