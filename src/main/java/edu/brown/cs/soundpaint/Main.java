package edu.brown.cs.soundpaint;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

import freemarker.template.Configuration;
import spark.QueryParamsMap;


/**
* The Main class of our project. This is where execution begins.
* Command line and GUI interfaces are intialized viathis class.
*/
public final class Main {

 private static final int DEFAULT_PORT = 4567;

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

   // TODO: Process commands in a REPL
   
   try{
   	BufferedReader _reader = new BufferedReader(new InputStreamReader(System.in));
   	String comm_line;
   	try{
	    	while((comm_line = _reader.readLine()) != null){
	    		//make interpreter
	    	}
   	}finally{
   		_reader.close();
   	}
   }catch(IOException e){
   	System.out.println(e);
   }
 }
 

 private static FreeMarkerEngine createEngine() {
   Configuration config = new Configuration();
   File templates = new File("src/main/resources/spark/template/freemarker");
   try {
     config.setDirectoryForTemplateLoading(templates);
   } catch (IOException ioe) {
     System.out.printf("ERROR: Unable use %s for template loading.%n",
         templates);
     System.exit(1);
   }
   return new FreeMarkerEngine(config);
 }

 private void runSparkServer(int port) {
   Spark.port(port);
   Spark.externalStaticFileLocation("src/main/resources/static");
   Spark.exception(Exception.class, new ExceptionPrinter());

   FreeMarkerEngine freeMarker = createEngine();

   // Setup Spark Routes
   Spark.get("/projects", new FrontHandler(), freeMarker);
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
     return new ModelAndView(variables, "query.ftl");
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
