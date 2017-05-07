package edu.brown.cs.filter;

import com.sun.org.apache.bcel.internal.generic.PUSH;

import java.awt.image.BufferedImage;

/**
 * Created by Tynan on 5/5/17.
 */
public class PushBufferedImageFilter implements BufferedImageFilter {

  int PUSH_VALUE = 100;

  public PushBufferedImageFilter() {

  }

  @Override
  public BufferedImage filter(BufferedImage input, double parameterValue, double sensitivityValue) {
    BufferedImageFilter.validateParameters(parameterValue, sensitivityValue);
    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    int adjustedPushValue = (int) (PUSH_VALUE * sensitivityValue);

    System.out.println("pushing");
    for (int x = 0; x < output.getWidth(); x++) {
      for (int y = 0; y < output.getHeight(); y++) {
        output.setRGB(x, y, output.getRGB(x, y) + (int) (adjustedPushValue * parameterValue));
      }
    }

    return output;
  }
}
