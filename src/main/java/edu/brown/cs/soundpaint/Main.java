package edu.brown.cs.soundpaint;


import java.io.IOException;

import java.sql.SQLException;


import joptsimple.OptionParser;
import joptsimple.OptionSet;


import edu.brown.cs.database.Database;


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
