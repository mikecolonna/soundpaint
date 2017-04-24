package edu.brown.cs.soundpaint;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Configuration;
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

/**
 * Installs GUI routes.
 */
public class GuiProcessor {

  private FreeMarkerEngine fme = createEngine();
  
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
    Spark.get("/", new FrontHandler(), fme);
    Spark.get("/login", new FrontLoginHandler(), fme);
    Spark.get("/register", new FrontRegisterHandler(), fme);
    Spark.get("/workspace", new FrontWorkSpaceHandler(), fme);
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
   * Handle requests to the front page of the GUI.
   */
  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      System.out.println("Heere");
      Map<String, Object> variables = ImmutableMap.of("title",
          "Soundpaint - CS32 Final Project","message","Created by Brendan,"
              + " Mike, Tymani, and Tynan");
      return new ModelAndView(variables, "home_news.ftl");
    }
  }
  
  /**
   * Handle requests to the front page of the GUI.
   */
  private static class FrontLoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Soundpaint - CS32 Final Project","message","Created by Brendan,"
              + " Mike, Tymani, and Tynan");
      return new ModelAndView(variables, "login.ftl");
    }
  }
  
  /**
   * Handle requests to the front page of the GUI.
   */
  private static class FrontRegisterHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Soundpaint - CS32 Final Project","message","Created by Brendan,"
              + " Mike, Tymani, and Tynan");
      return new ModelAndView(variables, "register.ftl");
    }
  }
  
  /**
   * Handle requests to the front page of the GUI.
   */
  private static class FrontWorkSpaceHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Soundpaint - CS32 Final Project","message","Created by Brendan,"
              + " Mike, Tymani, and Tynan");
      return new ModelAndView(variables, "workspace.ftl");
    }
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
