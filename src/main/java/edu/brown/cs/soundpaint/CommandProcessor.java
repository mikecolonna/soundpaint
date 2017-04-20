package edu.brown.cs.soundpaint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Reads input from the command line, line-by-line.
 */
public class CommandProcessor {

  private Map<Pattern, Command> patternCommandMap = new HashMap<>();

  /**
   * Starts the REPL.
   */
  public void repl() {
    try (BufferedReader br =
             new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        dispatch(input);
      }
      System.exit(0);
    } catch (IOException e) {
      System.out.println("ERROR: Could not read input");
    }
  }

  /**
   * Adds commands from a command installer.
   * @param installer The installer from which to install commands.
   */
  public void addCommands(CommandInstaller installer) {
    patternCommandMap.putAll(installer.getPatternCommandMap());
  }

  /**
   * Dispatches a command from the passed in string.
   * @param input The input from which to dispatch.
   */
  public void dispatch(String input) {
    for (Pattern pattern : patternCommandMap.keySet()) {
      if (pattern.matcher(input).matches()) {
        patternCommandMap.get(pattern).execute(
            Arrays.asList(input.split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)")), input);
        return;
      }
    }
    System.out.println("ERROR: Unrecognized Command");
  }

}
