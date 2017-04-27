package edu.brown.cs.video;

import edu.brown.cs.filter.ColorChannelMixerFilter;
import edu.brown.cs.filter.Filter;
import edu.brown.cs.sound.SoundData;
import edu.brown.cs.sound.SoundParameter;
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

  public static void renderVideo(Map<String, VideoFilterSpecification> filterMap, FrameGrabber frameGrabber) {
    List<SoundData> frequencyData;
    try {
      frameGrabber.start();

      int imgWidth = frameGrabber.getImageWidth();
      int imgHeight = frameGrabber.getImageHeight();
      double frameRate = frameGrabber.getFrameRate();
      int videoBitrate = frameGrabber.getVideoBitrate();
      int videoCodec = frameGrabber.getVideoCodec();

      String outputPath = "./testRender.mp4";

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

        for (Map.Entry<String, VideoFilterSpecification> mapping : filterMap.entrySet()) {
          System.out.println("entry");
//          SoundParameter soundParameter = mapping.getKey();
          VideoFilterSpecification videoFilterSpecification = mapping.getValue();
          VideoParameter videoParameter = videoFilterSpecification.getVideoParameter();

          switch (videoParameter) {
            case TINT:
              tintFilter(currentImage);
              break;
            case PUSH:
              pushFilter(currentImage);
              break;
            case BULGE:
              BufferedImage outputImage = new BufferedImage(currentImage.getWidth(), currentImage.getHeight(), currentImage.getType());
              bulgeFilter(currentImage, Math.random(), 100.0, outputImage);
              currentImage = outputImage;
              break;
            case EMBOSS:
              currentImage = embossFilter(currentImage);
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

  private static Filter colorFilterFromSound(double sensitivity) {
    return new ColorChannelMixerFilter.Builder()
        .rr(Math.random() * sensitivity)
        .gg(Math.random() * sensitivity)
        .bb(Math.random() * sensitivity)
        .build();
  }

  private static void tintFilter(BufferedImage image) {
    System.out.println("tinting");
    double redVal = Math.random();
    double greenVal = Math.random();
    double blueVal = Math.random();
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {

        Color color = new Color(image.getRGB(x, y));

        int r = (int) (color.getRed() * redVal);
        int g = (int) (color.getGreen() * greenVal);
        int b = (int) (color.getBlue() * blueVal);

//        System.out.println("Original " + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + " new " + r + "," + g + "," + b);

        image.setRGB(x, y, new Color(r, g, b).getRGB());
      }
    }
  }

  private static void pushFilter(BufferedImage image) {
    System.out.println("pushing");
    double randVal = Math.random();
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        image.setRGB(x, y, image.getRGB(x, y) + (int) (100 * randVal));
      }
    }
  }

  private static void bulgeFilter(BufferedImage input, double bulgeStrength, double bulgeRadius,
    BufferedImage output) {
    System.out.println("bulging");
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
  }

  public static BufferedImage embossFilter(BufferedImage src) {
    int width = src.getWidth();
    int height = src.getHeight();

    BufferedImage dst;
    dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < height; i++)
      for (int j = 0; j < width; j++) {
        int upperLeft = 0;
        int lowerRight = 0;

        if (i > 0 && j > 0)
          upperLeft = src.getRGB(j - 1, i - 1);

        if (i < height - 1 && j < width - 1)
          lowerRight = src.getRGB(j + 1, i + 1);

        int redDiff = ((lowerRight >> 16) & 255) - ((upperLeft >> 16) & 255);

        int greenDiff = ((lowerRight >> 8) & 255) - ((upperLeft >> 8) & 255);

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

        dst.setRGB(j, i, newColor);
      }

    return dst;
  }
}
