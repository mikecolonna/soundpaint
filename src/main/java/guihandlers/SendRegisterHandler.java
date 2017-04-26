package guihandlers;

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
    QueryParamsMap qm = req.queryMap();
    String username = qm.value("username");
    String email = qm.value("email");
    String password = qm.value("password");
    String password2 = qm.value("password2");
    
    if (!password.equals(password2)) {
      // return to /register with an error
    }

    UserDB user = null;
    try {
      user = UserDB.createUser(UserDB.generateId(), username, email, password);
    } catch (SQLException sqle) {
      // TODO: determine what constraint was violated ...
      // return to /register with an appropriate error
    }
    
    // log user in after completing registration
    guiProcessor.getSessionsToUsers().put(req.session().id(), user.getId());
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Soundpaint - CS32 Final Project","message","Created by Brendan,"
            + " Mike, Tymani, and Tynan");
    return new ModelAndView(variables, "home-news.ftl");
  }

}
