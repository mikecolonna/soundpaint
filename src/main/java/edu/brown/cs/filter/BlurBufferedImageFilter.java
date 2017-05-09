package edu.brown.cs.filter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * Created by Tynan on 5/9/17.
 */
public class BlurBufferedImageFilter implements BufferedImageFilter {

  public BufferedImage filter(BufferedImage input, double parameterValue, double sensitivityValue) {

    int[] filter = new int[225];

    for (int i = 0; i < 225; i++) {
      filter[i] = 10;
    }

    int filterWidth = 15;

    if (filter.length % filterWidth != 0) {
      throw new IllegalArgumentException("filter contains a incomplete row");
    }

    final int width = input.getWidth();
    final int height = input.getHeight();
    final int sum = IntStream.of(filter).sum();

    int[] inputArray = input.getRGB(0, 0, width, height, null, 0, width);

    int[] outputArray = new int[inputArray.length];

    final int pixelIndexOffset = width - filterWidth;
    final int centerOffsetX = filterWidth / 2;
    final int centerOffsetY = filter.length / filterWidth / 2;

    // apply filter
    for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int filterIndex = 0, pixelIndex = y * width + x;
             filterIndex < filter.length;
             pixelIndex += pixelIndexOffset) {
          for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
            int col = inputArray[pixelIndex];
            int factor = filter[filterIndex];

            // sum up color channels seperately
            r += ((col >>> 16) & 0xFF) * factor;
            g += ((col >>> 8) & 0xFF) * factor;
            b += (col & 0xFF) * factor;
          }
        }
        r /= sum;
        g /= sum;
        b /= sum;
        // combine channels with full opacity
        outputArray[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
      }
    }

    BufferedImage output = new BufferedImage(width, height, input.getType());
    output.setRGB(0, 0, width, height, outputArray, 0, width);

    BufferedImage combinedOutput = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    // paint both images, preserving the alpha channels
    Graphics2D g = combinedOutput.createGraphics();

    g.drawImage(input, 0, 0, null);

    float opacity = (float) (parameterValue * sensitivityValue);
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    g.drawImage(output, 0, 0, null);
    g.dispose();

    return combinedOutput;
  }
}
