package edu.brown.cs.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Tynan on 5/5/17.
 */
public class EmbossBufferedImageFilter implements BufferedImageFilter {

  public EmbossBufferedImageFilter() {

  }

  @Override
  public BufferedImage filter(BufferedImage input, double parameterValue, double sensitivityValue) {
    BufferedImageFilter.validateParameters(parameterValue, sensitivityValue);
    int width = input.getWidth();
    int height = input.getHeight();

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++) {
        int upperLeft = 0;
        int lowerRight = 0;

        if (i > 0 && j > 0)
          upperLeft = input.getRGB(j - 1, i - 1);

        if (i < height - 1 && j < width - 1)
          lowerRight = input.getRGB(j + 1, i + 1);

        int redDiff = ((lowerRight >> 16) & 255) - ((upperLeft >> 16) & 255);

        int greenDiff = (((lowerRight >> 8) & 255) - ((upperLeft >> 8) & 255));

        int blueDiff = (lowerRight & 255) - (upperLeft & 255);

        int diff = redDiff;
        if (Math.abs(greenDiff) > Math.abs(diff))
          diff = greenDiff;
        if (Math.abs(blueDiff) > Math.abs(diff))
          diff = blueDiff;

        int grayColor = 128 + diff;

        if (grayColor > 255)
          grayColor = 255;
        else if (grayColor < 0)
          grayColor = 0;

        int newColor = (grayColor << 16) + (grayColor << 8) + grayColor;

        output.setRGB(j, i, newColor);
      }


    System.out.println(input.getType());
    BufferedImage combinedOutput = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    // paint both images, preserving the alpha channels
    Graphics2D g = combinedOutput.createGraphics();

    g.drawImage(input, 0, 0, null);

    float opacity = (float) (parameterValue * sensitivityValue);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    g.drawImage(output, 0, 0, null);

    return combinedOutput;
  }
}
