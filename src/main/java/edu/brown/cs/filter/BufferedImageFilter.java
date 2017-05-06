package edu.brown.cs.filter;

import java.awt.image.BufferedImage;

/**
 * Created by Tynan on 5/5/17.
 */
public interface BufferedImageFilter {
  BufferedImage filter(BufferedImage image,  double parameterValue, double sensitivityValue);
}
