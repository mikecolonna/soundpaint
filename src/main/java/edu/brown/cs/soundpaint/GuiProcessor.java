package edu.brown.cs.soundpaint;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Configuration;
import guihandlers.FrontHandler;
import guihandlers.FrontLoginHandler;
import guihandlers.FrontRegisterHandler;
import guihandlers.FrontWorkspaceHandler;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Installs GUI routes.
 */
public class GuiProcessor {

  private FreeMarkerEngine fme = createEngine();
  private Map<String, String> sessionsToUsers = new ConcurrentHashMap<>();
  
  public void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    
    this.setRoutes();
  }

  /**
   * Sets routes from the passed in installer.
   * @param installer The installer from which to install routes.
   */
  private void setRoutes() {
    Spark.get("/", new FrontHandler(this), fme);
    Spark.get("/login", new FrontLoginHandler(this), fme);
    Spark.get("/register", new FrontRegisterHandler(this), fme);
    Spark.get("/workspace", new FrontWorkspaceHandler(this), fme);
    
  }
  
  public Map<String, String> getSessionsToUsers() {
    return sessionsToUsers;
  }

  /**
   * Creates the freemarker engine.
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates =
        new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }
  
  /**
   * Display an error page when an exception occurs in the server.
   *
   * @author jj
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
