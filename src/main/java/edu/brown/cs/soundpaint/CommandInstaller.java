package edu.brown.cs.soundpaint;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Functional interface for command installation.
 */
@FunctionalInterface
public interface CommandInstaller {
  /**
   * Gets a map from regex patterns to commands.
   * @return The map from regex patterns to commands.
   */
  Map<Pattern, Command> getPatternCommandMap();

}
