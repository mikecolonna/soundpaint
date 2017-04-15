package edu.brown.cs.soundpaint;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Installs GUI routes.
 */
public class GuiProcessor {

  private FreeMarkerEngine fme = createEngine();

  /**
   * Sets routes from the passed in installer.
   * @param installer The installer from which to install routes.
   */
  public void setRoutes(RouteInstaller installer) {
    installer.installRoutes(fme);
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

  private static class FrontHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "CS0320 - Projects","message","");
      return new ModelAndView(variables, "query.ftl");
    }
  }

}
