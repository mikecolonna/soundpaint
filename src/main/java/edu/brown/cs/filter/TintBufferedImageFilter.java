package edu.brown.cs.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tynan on 5/5/17.
 */
public class TintBufferedImageFilter implements BufferedImageFilter {

  public enum FilterColor {
    RED,
    GREEN,
    BLUE
  }

  private FilterColor filterColor;

  public TintBufferedImageFilter(FilterColor color) {
    this.filterColor = color;
  }

  @Override
  public BufferedImage filter(BufferedImage input, double parameterValue, double sensitivityValue) {
    BufferedImageFilter.validateParameters(parameterValue, sensitivityValue);
    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    for (int x = 0; x < input.getWidth(); x++) {
      for (int y = 0; y < input.getHeight(); y++) {

        Color color = new Color(input.getRGB(x, y));

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        switch (filterColor) {
          case RED:
            r = (int) (r * (1 - parameterValue * sensitivityValue));
            break;

          case GREEN:
            g = (int) (g * (1 - parameterValue * sensitivityValue));
            break;

          case BLUE:
            b = (int) (b * (1 - parameterValue * sensitivityValue));
            break;

          default:
            break;

        }
        output.setRGB(x, y, new Color(r, g, b).getRGB());
      }
    }

    return output;
  }
}
