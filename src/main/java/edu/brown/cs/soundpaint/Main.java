package edu.brown.cs.soundpaint;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;

import com.google.common.collect.ImmutableMap;

/**
* The Main class of our project. This is where execution begins.
* Command line and GUI interfaces are intialized viathis class.
*/
public final class Main {

 private static final int DEFAULT_PORT = 4567;
 private CommandProcessor commandProcessor = new CommandProcessor();
 private GuiProcessor guiProcessor = new GuiProcessor();
 private Manager manager = new Manager();

 /**
  * The initial method called when execution begins.
  *
  * @param args
  *          An array of command line arguments
* @throws IOException 
* @throws SQLException 
* @throws ClassNotFoundException 
  */
 public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
   new Main(args).run();
 }

 private String[] args;

 private Main(String[] args) {
   this.args = args;
 }

 private void run() throws ClassNotFoundException, SQLException  {
   // Parse command line arguments
   OptionParser parser = new OptionParser();
   parser.accepts("gui");
   parser.accepts("port").withRequiredArg().ofType(Integer.class)
   .defaultsTo(DEFAULT_PORT);
   OptionSet options = parser.parse(args);

   if (options.has("gui")) {
     runSparkServer((int) options.valueOf("port"));
   }

   commandProcessor.addCommands(manager::getPatternCommandMap);
   commandProcessor.repl();
 }

 private void runSparkServer(int port) {
   Spark.port(port);
   Spark.externalStaticFileLocation("src/main/resources/static");
   Spark.exception(Exception.class, new ExceptionPrinter());
   guiProcessor.setRoutes(manager::installRoutes);
 }
 

 /**
  * Handle requests to the front page of our Stars website.
  *
  * @author jj
  */
 private static class FrontHandler implements TemplateViewRoute {
   @Override
   public ModelAndView handle(Request req, Response res) {
     Map<String, Object> variables = ImmutableMap.of("title",
         "CS0320 - Projects","message","");
     return new ModelAndView(variables, "main.ftl");
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
