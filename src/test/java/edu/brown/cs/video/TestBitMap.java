package edu.brown.cs.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * Created by Ty on 09/05/2017.
 */
public class TestBitMap {

  @Test
  public void testInit() {
    BitmapSequence b = new BitmapSequence();
    assertNotNull(b);

  }

  @Test
  public void testFrameToBufferedHeight() {
    FFmpegFrameGrabber fg = new FFmpegFrameGrabber("resources/video/bunny.mp4");
    try {
      fg.start();
    } catch (FrameGrabber.Exception e) {
     // e.printStackTrace();
    }

    Frame f  = null;
    try {
      f = fg.grab();
    } catch (FrameGrabber.Exception e) {
     // e.printStackTrace();
    }
    BufferedImage bi = BitmapSequence.FrameToBufferedImage(f);



    assertTrue(bi.getHeight() == new Java2DFrameConverter().getBufferedImage(f,1).getHeight());
  }

  @Test
  public void testFrameToBufferedColor() {
    FFmpegFrameGrabber fg = new FFmpegFrameGrabber("resources/video/bunny.mp4");
    try {
      fg.start();
    } catch (FrameGrabber.Exception e) {
      // e.printStackTrace();
    }

    Frame f  = null;
    try {
      f = fg.grab();
    } catch (FrameGrabber.Exception e) {
      // e.printStackTrace();
    }
    BufferedImage bi = BitmapSequence.FrameToBufferedImage(f);



    assertTrue(bi.getColorModel().toString().equals(new Java2DFrameConverter().getBufferedImage(f,1).getColorModel().toString()));
  }

  @Test
  public void testFrameToBufferedColorMinX() {
    FFmpegFrameGrabber fg = new FFmpegFrameGrabber("resources/video/bunny.mp4");
    try {
      fg.start();
    } catch (FrameGrabber.Exception e) {
      // e.printStackTrace();
    }

    Frame f  = null;
    try {
      f = fg.grab();
    } catch (FrameGrabber.Exception e) {
      // e.printStackTrace();
    }
    BufferedImage bi = BitmapSequence.FrameToBufferedImage(f);



    assertTrue(bi.getMinX() == new Java2DFrameConverter().getBufferedImage(f,1).getMinX());
  }
}
