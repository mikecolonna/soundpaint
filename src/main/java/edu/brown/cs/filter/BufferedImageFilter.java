package edu.brown.cs.filter;

import java.awt.image.BufferedImage;

/**
 * Created by Tynan on 5/5/17.
 */
public interface BufferedImageFilter {
  BufferedImage filter(BufferedImage image,  double parameterValue, double sensitivityValue);

  public static void validateParameters(double parameterValue, double sensitivityValue) {
    if (parameterValue < 0 || parameterValue > 1) {
      throw new IllegalArgumentException("ERROR: parameterValue must be between 0 and 1 inclusive.");
    }

    if (sensitivityValue < 0 || sensitivityValue > 1) {
      throw new IllegalArgumentException("ERROR: sensitivityValue must be between 0 and 1 inclusive.");
    }
  }
}
