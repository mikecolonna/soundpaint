package edu.brown.cs.video;

import edu.brown.cs.filter.ColorChannelMixerFilter;
import edu.brown.cs.filter.Filter;
import edu.brown.cs.sound.SoundData;
import edu.brown.cs.sound.SoundEngine;
import edu.brown.cs.sound.SoundParameter;
import edu.brown.cs.soundpaint.VideoSoundParameterMapping;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Created by tynan on 4/21/17.
 */
public class RenderEngine {

  public static void renderVideo(List<VideoSoundParameterMapping> mappings, FrameGrabber frameGrabber, SoundEngine soundEngine, String outputPath) {
    List<SoundData> frequencyData;
    try {
      frameGrabber.start();

      int imgWidth = frameGrabber.getImageWidth();
      int imgHeight = frameGrabber.getImageHeight();
      double frameRate = frameGrabber.getFrameRate();

      soundEngine.setSoundReader(1/frameRate);

      int videoBitrate = frameGrabber.getVideoBitrate();
      int videoCodec = frameGrabber.getVideoCodec();

      FFmpegFrameRecorder recorder
          = new FFmpegFrameRecorder(outputPath, imgWidth, imgHeight);
      recorder.setFormat("mp4");
      recorder.setFrameRate(frameRate);
      recorder.setVideoBitrate(videoBitrate);
      recorder.setVideoCodec(videoCodec);

      Java2DFrameConverter converter = new Java2DFrameConverter();

      recorder.start();

      Frame currentFrame = frameGrabber.grab();
      int fn = 0;

      while (currentFrame != null) {

        BufferedImage currentImage = converter.getBufferedImage(currentFrame);

        for (VideoSoundParameterMapping mapping : mappings) {
          System.out.println("entry");
          SoundParameter soundParameter = mapping.getSoundParameter();
          VideoParameter videoParameter = mapping.getVideoParameter();
          double sensitivity = mapping.getSensitivity();
          switch (videoParameter) {
            case TINT:
              currentImage = tintFilter(currentImage,  soundEngine.getMetaData(mapping.getSoundParameter()).get(fn));
              break;
            case PUSH:
              currentImage = pushFilter(currentImage,  soundEngine.getMetaData(mapping.getSoundParameter()).get(fn));
              break;
            case BULGE:
              currentImage = bulgeFilter(currentImage, 100.0, soundEngine.getMetaData(mapping.getSoundParameter()).get(fn));
              break;
            case EMBOSS:
              currentImage = embossFilter(currentImage, soundEngine.getMetaData(mapping.getSoundParameter()).get(fn));
              break;
            default:
              break;
          }


        }

        recorder.record(converter.getFrame(currentImage));
        System.out.println("Recorded frame number " + fn);
        fn++;
        currentFrame = frameGrabber.grab();
      }
      System.out.println("All frames have been processed!");
      recorder.stop();
      frameGrabber.stop();

      recorder.close();
      frameGrabber.close();

      System.out.println("Video rendered");

    } catch (FrameGrabber.Exception fge) {
      System.out.println("ERROR: Could not retrieve frame from video file.");
    } catch (FrameRecorder.Exception fre) {
      System.out.println("ERROR: Could not export frame to output.");
    }
  }

  private static BufferedImage tintFilter(BufferedImage input, double value) {

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    double greenVal = value;

    for (int x = 0; x < input.getWidth(); x++) {
      for (int y = 0; y < input.getHeight(); y++) {

        Color color = new Color(input.getRGB(x, y));

        int g = (int) (color.getGreen() * greenVal);

        output.setRGB(x, y, new Color(color.getRed(), g, color.getBlue()).getRGB());
      }
    }

    return output;
  }

  private static BufferedImage pushFilter(BufferedImage input, double value) {

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    System.out.println("pushing");
    for (int x = 0; x < output.getWidth(); x++) {
      for (int y = 0; y < output.getHeight(); y++) {
        output.setRGB(x, y, output.getRGB(x, y) + (int) (100 * value));
      }
    }

    return output;
  }

  private static BufferedImage bulgeFilter(BufferedImage input, double bulgeRadius, double bulgeStrength) {

    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

    int w = input.getWidth();
    int h = input.getHeight();
    for(int x = 0; x < w; x++) {
      for(int y = 0; y < h; y++) {
        int dx = x-w/2;
        int dy = y-h/2;
        double distanceSquared = Math.pow(dx, 2) + Math.pow(dy, 2);
        int sx = x;
        int sy = y;
        if (distanceSquared < Math.pow(bulgeRadius, 2)) {
          double distance = Math.sqrt(distanceSquared);
          double dirX = dx / distance;
          double dirY = dy / distance;
          double alpha = distance / bulgeRadius;
          double distortionFactor =
              distance * Math.pow(1-alpha, 1.0 / bulgeStrength);
          sx -= distortionFactor * dirX;
          sy -= distortionFactor * dirY;
        }

        if (sx >= 0 && sx < w && sy >= 0 && sy < h) {
          int rgb = input.getRGB(sx, sy);
          output.setRGB(x, y, rgb);
        }
      }
    }

    return output;
  }

  public static BufferedImage embossFilter(BufferedImage input, double value) {
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

        int greenDiff = (int) ((((lowerRight >> 8) & 255) - ((upperLeft >> 8) & 255)) * value);

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

    return output;
  }
}
