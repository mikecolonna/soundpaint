package edu.brown.cs.soundpaint;

import java.io.File;
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
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.database.Database;
import freemarker.template.Configuration;

/**
* The Main class of our project. This is where execution begins.
* Command line and GUI interfaces are initialized via this class.
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
     guiProcessor.runSparkServer((int) options.valueOf("port"));
   }
   
   Database.setPath("jdbc:sqlite:test.db");
   Database.resetCaches();

   commandProcessor.addCommands(manager::getPatternCommandMap);
   commandProcessor.repl();
 }

 
}
