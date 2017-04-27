package edu.brown.cs.video;



import java.util.Queue;

import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameFilter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

/**
 * @author mike 4/17/17
 */
public class FilterProcessor {

  // internal filter graph
  private FrameFilter filterer;

  //private Queue<Filter> filters;

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
      //Java2DFrameConverter frameConverter = new Java2DFrameConverter();
      while (curr != null) {
        /*BufferedImage img = frameConverter.convert(curr);
        for (Filter f : filters) {
          BufferedImage out = f.filter(img);
          img = out;
        }
        recorder.record(frameConverter.convert(img));*/
        filterer.push(curr);
        Frame filtered = filterer.pull();
        
        // More calls to record() = slow-mo (more frames recorded)
        recorder.record(filtered);
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
