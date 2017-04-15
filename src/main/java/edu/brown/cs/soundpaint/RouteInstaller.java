package edu.brown.cs.soundpaint;

import spark.template.freemarker.FreeMarkerEngine;

/**
 * Functional interface for URL route installation.
 */
public interface RouteInstaller {
  /**
   * Installs routes.
   * @param fme The FreeMarkerEngine that some routes are installed to.
   */
  void installRoutes(FreeMarkerEngine fme);
}
