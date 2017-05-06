package edu.brown.cs.video;

import org.bytedeco.javacv.*;

/**
 * Created by tynan on 5/6/17.
 */
public class ExtractWav {

  public static void extractWav(String inputPath, String outputPath) {
    FFmpegFrameGrabber fg = new FFmpegFrameGrabber(inputPath);
    FFmpegFrameRecorder fr = new FFmpegFrameRecorder(outputPath, 2);
    try {
      fg.start();
      Frame cf = fg.grabSamples();
      fr.setFormat("wav");
      fr.setImageHeight(0);
      fr.start();

      while (cf != null) {
        if (cf.samples != null) {
          fr.record(cf);
        }
        cf = fg.grab();
      }

      fg.stop();
      fr.stop();
      System.out.println("Recorded wav.");
    } catch (FrameGrabber.Exception e) {
      e.printStackTrace();
    } catch (FrameRecorder.Exception e) {
      e.printStackTrace();
    }
  }
}
