package edu.brown.cs.video;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.filter.Filter;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.FrameGrabber.Exception;

public class FilterProcessor {

  // internal filter graph
  private FrameFilter filterer;
  
  public FilterProcessor(Filter filter) {
    filterer = new FFmpegFrameFilter(filter.getFilterString(), 0, 0);
  }

  public FilterProcessor(String filter) {
    filterer = new FFmpegFrameFilter(filter, 0, 0);
  }
  
  public void process(String inputPath, String outputPath) {
    try {
      FFmpegFrameGrabber videoGrabber 
        = new FFmpegFrameGrabber(inputPath);
      videoGrabber.start();
      int imgWidth = videoGrabber.getImageWidth();
      int imgHeight = videoGrabber.getImageHeight();
      double frameRate = videoGrabber.getFrameRate();
      int videoBitrate = videoGrabber.getVideoBitrate();
      int videoCodec = videoGrabber.getVideoCodec();
      
      filterer.setImageWidth(imgWidth);
      filterer.setImageHeight(imgHeight);
      
      FFmpegFrameRecorder recorder 
        = new FFmpegFrameRecorder(outputPath, imgWidth, imgHeight);
      recorder.setFormat("mp4");
      recorder.setFrameRate(frameRate);
      recorder.setVideoBitrate(videoBitrate);
      recorder.setVideoCodec(videoCodec);
      
      filterer.start();
      recorder.start();
      
      Frame curr = videoGrabber.grab();
      while (curr != null) {
        filterer.push(curr);
        recorder.record(filterer.pull());
        curr = videoGrabber.grab(); // get next frame
      }
      recorder.stop();
      filterer.stop();
      videoGrabber.stop();
      
      recorder.close();
      videoGrabber.close();
      
      System.out.println("Video has finished processing.");
    } catch (FrameGrabber.Exception fge) {
      System.out.println("ERROR: Could not retrieve frame from video file.");
    } catch (FrameFilter.Exception ffe) {
      System.out.println("ERROR: Could not apply filter to frame.");
    } catch (FrameRecorder.Exception fre) {
      System.out.println("ERROR: Could not export frame to output.");
    }
  }
}
