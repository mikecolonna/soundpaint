package edu.brown.cs.video;

import edu.brown.cs.filter.*;
import edu.brown.cs.sound.SoundEngine;
import edu.brown.cs.sound.SoundParameter;
import edu.brown.cs.soundpaint.VideoSoundParameterMapping;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by tynan on 4/21/17.
 */
public class RenderEngine {

  public static void renderVideo(List<VideoSoundParameterMapping> mappings, FrameGrabber frameGrabber, SoundEngine soundEngine, String outputPath) {

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
        if (currentImage != null) {
          for (VideoSoundParameterMapping mapping : mappings) {

            SoundParameter soundParameter = mapping.getSoundParameter();
            VideoParameter videoParameter = mapping.getVideoParameter();

            double sensitivity = mapping.getSensitivity();
            double parameter = 0;
            List<Double> metadata = soundEngine.getMetaData(soundParameter);
            int metadataSize = metadata.size();

            if (fn < metadataSize) {
              parameter = metadata.get(fn);
            }

            System.out.println("PARAMETER: " + parameter);

            switch (videoParameter) {
              case PUSH:
                BufferedImageFilter pushFilter = new PushBufferedImageFilter();
                currentImage = pushFilter.filter(currentImage, parameter, sensitivity);
                break;
              case BULGE:
                BufferedImageFilter bulgeFilter = new BulgeBufferedImageFilter();
                currentImage = bulgeFilter.filter(currentImage, parameter, sensitivity);
                break;
              case EMBOSS:
                BufferedImageFilter embossFilter = new EmbossBufferedImageFilter();
                currentImage = embossFilter.filter(currentImage, parameter, sensitivity);
                break;
              case RED_TINT:
                BufferedImageFilter redFilter = new TintBufferedImageFilter(TintBufferedImageFilter.FilterColor.RED);
                currentImage = redFilter.filter(currentImage, parameter, sensitivity);
                break;
              case GREEN_TINT:
                BufferedImageFilter greenFilter = new TintBufferedImageFilter(TintBufferedImageFilter.FilterColor.GREEN);
                currentImage = greenFilter.filter(currentImage, parameter, sensitivity);
                break;
              case BLUE_TINT:
                BufferedImageFilter blueFilter = new TintBufferedImageFilter(TintBufferedImageFilter.FilterColor.BLUE);
                currentImage = blueFilter.filter(currentImage, parameter, sensitivity);
                break;
              case BLUR:
                BufferedImageFilter blurFilter = new BlurBufferedImageFilter();
                currentImage = blurFilter.filter(currentImage, parameter, sensitivity);
                break;
              case PINCH:
                BufferedImageFilter pinchFilter = new PinchBufferedImageFilter();
                currentImage = pinchFilter.filter(currentImage, parameter, sensitivity);
                break;
              default:
                break;
            }
          }

          recorder.record(converter.getFrame(currentImage));
          System.out.println("Recorded frame number " + fn);
          fn++;

        }
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

  public static void saveThumbnail(String inputPath, String outputPath) {

    final int THUMBNAIL_WIDTH = 200;

    FrameGrabber fg = new FFmpegFrameGrabber(inputPath);

    try {
      fg.start();
      Frame f = fg.grabFrame();
      if (f == null) {
        System.out.println("ERROR: Cannot obtain a thumbnail from this video, it is empty.");
      } else {
        BufferedImage src = new Java2DFrameConverter().getBufferedImage(f);
        Image thumbnail = src.getScaledInstance(THUMBNAIL_WIDTH, -1, Image.SCALE_SMOOTH);
        BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null),
            thumbnail.getHeight(null),
            BufferedImage.TYPE_INT_RGB);
        bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
        try {
          ImageIO.write(bufferedThumbnail, "jpeg", new File(outputPath));
        } catch (IOException e) {
          System.out.println("ERROR: Could not save thumbnail to the given filepath.");
        }
      }
      fg.close();
    } catch (FFmpegFrameGrabber.Exception e) {
      System.out.println("ERROR: Could not access the provided video stream.");
    }
  }
}
