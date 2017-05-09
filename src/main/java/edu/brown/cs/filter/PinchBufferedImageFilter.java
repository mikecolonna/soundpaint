package edu.brown.cs.filter;

import com.jhlabs.image.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.nio.Buffer;

/**
 * Created by Tynan on 5/9/17.
 */
public class PinchBufferedImageFilter implements BufferedImageFilter {

  public BufferedImage filter(BufferedImage input, double parameterValue, double sensitivityValue)
  {

    PinchFilter pinchFilter = new PinchFilter();

    pinchFilter.setAmount((float) (parameterValue * sensitivityValue));
    pinchFilter.setRadius(input.getWidth() / 5);

    BufferedImage effectImage = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
    pinchFilter.filter(input, effectImage);

    BufferedImage convert =  new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

    Graphics2D graphics = convert.createGraphics();

    graphics.drawImage(effectImage, null, 0, 0);

    graphics.dispose();

    return convert;
  }
}