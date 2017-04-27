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

public class SendRegisterHandler implements TemplateViewRoute {
  
  private GuiProcessor guiProcessor;
  
  public SendRegisterHandler(GuiProcessor gp) {
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
    String username = qm.value("username");
    String email = qm.value("email");
    String password = qm.value("password");
    String password2 = qm.value("password2");
    /*System.out.println("username : " + username);
    System.out.println("email : " + email);
    System.out.println("password : " + password);
    System.out.println("password2 : " + password2);*/
    
    if (!password.equals(password2)) {
      // return to /register with an error
      Map<String, Object> variables = ImmutableMap.of(
          "title", "Soundpaint - CS32 Final Project",
          "message","Created by Brendan, Mike, Tymani, and Tynan",
          "error", "Passwords do not match!");
      return new ModelAndView(variables, "register.ftl");
    }

    UserDB user = null;
    try {
      user = UserDB.createUser(UserDB.generateId(), username, email, password);
    } catch (SQLException sqle) {
      // return to /register with an appropriate error
      String error = sqle.toString();
      String constraint = error.substring(error.lastIndexOf('.') + 1, error.length() - 1);
      String message = constraint + " is already taken.";
      
      Map<String, Object> variables = ImmutableMap.of(
          "title", "Soundpaint - CS32 Final Project",
          "message","Created by Brendan, Mike, Tymani, and Tynan",
          "error", message);
      return new ModelAndView(variables, "register.ftl");
    }
    
    // log user in after completing registration
    guiProcessor.getSessionsToUsers().put(req.session().id(), user.getId());
    req.session().attribute("username", username);
    response.redirect("/");
    return null;
  }

}
