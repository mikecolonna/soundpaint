package edu.brown.cs.guihandlers;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class ErrorHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) throws Exception {
    Map<String, Object> variables = ImmutableMap.of(
        "title", "Soundpaint - CS32 Final Project",
        "message","Created by Brendan, Mike, Tymani, and Tynan",
        "error", "");
    return new ModelAndView(variables, "error.ftl");
  }
}
