package edu.brown.cs.video;

import edu.brown.cs.filter.ColorChannelMixerFilter;
import edu.brown.cs.filter.Filter;
import edu.brown.cs.tratchfo.SoundData;
import edu.brown.cs.tratchfo.SoundParameter;
import org.bytedeco.javacv.*;

import java.util.List;
import java.util.Map;

/**
 * Created by tynan on 4/21/17.
 */
public class RenderEngine {

  public static void renderVideo(Map<SoundParameter, VideoFilterSpecification> filterMap, FrameGrabber frameGrabber) {
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

      recorder.start();

      Frame curr = frameGrabber.grab();
      //Java2DFrameConverter frameConverter = new Java2DFrameConverter();
      while (curr != null) {
        /*BufferedImage img = frameConverter.convert(curr);
        for (Filter f : filters) {
          BufferedImage out = f.filter(img);
          img = out;
        }
        recorder.record(frameConverter.convert(img));*/

        StringBuilder filterStringBuilder = new StringBuilder();

        for (Map.Entry<SoundParameter, VideoFilterSpecification> mapping : filterMap.entrySet()) {
          SoundParameter soundParameter = mapping.getKey();
          VideoFilterSpecification videoFilterSpecification = mapping.getValue();
          VideoParameter videoParameter = videoFilterSpecification.getVideoParameter();

          switch (videoParameter) {
            case COLOR:
              filterStringBuilder.append(colorFilterFromSound(videoFilterSpecification.getSensitivity()).getFilterString());
            default:

          }
        }

        FrameFilter frameFilter = new FFmpegFrameFilter(filterStringBuilder.toString(), imgWidth, imgHeight);
        frameFilter.start();
        frameFilter.push(curr);
        Frame filteredFrame = frameFilter.pull();

        // More calls to record() = slow-mo (more frames recorded)
        recorder.record(filteredFrame);
        curr = frameGrabber.grab(); // get next frame
        frameFilter.stop();
      }
      recorder.stop();
      frameGrabber.stop();

      recorder.close();
      frameGrabber.close();

      System.out.println("Video rendered");

    } catch (FrameGrabber.Exception fge) {
      System.out.println("ERROR: Could not retrieve frame from video file.");
    } catch (FrameFilter.Exception ffe) {
      System.out.println("ERROR: Could not apply filter to frame.");
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
}
