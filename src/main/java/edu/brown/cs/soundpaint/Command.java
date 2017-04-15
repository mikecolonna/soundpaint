package edu.brown.cs.soundpaint;

import java.util.List;

/**
 * Functional interface for executable command.
 */
@FunctionalInterface
public interface Command {
  /**
   * Executes the passed in command.
   * @param tokens The list of tokens in the command.
   * @param cmd The Command string.
   */
  void execute(List<String> tokens, String cmd);
}
