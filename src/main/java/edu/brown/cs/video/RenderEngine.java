package edu.brown.cs.video;

import edu.brown.cs.filter.*;
import edu.brown.cs.sound.SoundData;
import edu.brown.cs.sound.SoundEngine;
import edu.brown.cs.sound.SoundParameter;
import edu.brown.cs.soundpaint.VideoSoundParameterMapping;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
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

        for (VideoSoundParameterMapping mapping : mappings) {

          SoundParameter soundParameter = mapping.getSoundParameter();
          VideoParameter videoParameter = mapping.getVideoParameter();

          double sensitivity = mapping.getSensitivity();
          double parameter = soundEngine.getMetaData(soundParameter).get(fn);

          switch (videoParameter) {
            case TINT:
              BufferedImageFilter tintFilter = new TintBufferedImageFilter(TintBufferedImageFilter.FilterColor.GREEN);
              currentImage = tintFilter.filter(currentImage, parameter, sensitivity);
              break;
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


}
