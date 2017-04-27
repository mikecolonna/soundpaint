package edu.brown.cs.guihandlers;

import java.sql.SQLException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.database.UserDB;
import edu.brown.cs.soundpaint.GuiProcessor;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class SendLoginHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public SendLoginHandler(GuiProcessor gp) {
    guiProcessor = gp; 
  }

  @Override
  public ModelAndView handle(Request req, Response response) throws Exception {
    // if user is already logged in -- take them to home
    String seshId = req.session().id();
    if (guiProcessor.getSessionsToUsers().containsKey(seshId)) {
      response.redirect("/");
      return null;
    }
    
    QueryParamsMap qm = req.queryMap();
    String email = qm.value("email");
    String password = qm.value("password");
    
    String userId;
    try {
      userId = UserDB.loginUser(email, password);
      guiProcessor.getSessionsToUsers().put(req.session().id(), userId);
	  UserDB user = UserDB.getById(userId);
	  String username = user.getUsername();
      req.session().attribute("username", username);
    } catch (SQLException sqle) {
      Map<String, Object> variables = ImmutableMap.of(
          "title", "Soundpaint - CS32 Final Project",
          "message","Created by Brendan, Mike, Tymani, and Tynan",
          "error", "Email and/or password is incorrect.");
      return new ModelAndView(variables, "login.ftl");
    }
    
    response.redirect("/");
    return null;
  }

}
