package edu.brown.cs.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tynan on 4/16/17.
 */
public class BitmapSequence {

    public static List<BufferedImage> getBitmapSequenceFromPath(String path) {
        List<BufferedImage> sequence = new ArrayList<>();
        try {
            FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(path);
            frameGrabber.start();
            Frame frame;
            for(int i=0; i<frameGrabber.getLengthInFrames(); i++){
                frame = frameGrabber.grab();
                sequence.add(FrameToBufferedImage(frame));
            }
            frameGrabber.stop();
        } catch (FrameGrabber.Exception e) {
            System.out.println("ERROR: Could not retrieve frames from video file.");
        }

        return sequence;
    }

    public static BufferedImage FrameToBufferedImage(Frame src) {
        Java2DFrameConverter paintConverter = new Java2DFrameConverter();
        return paintConverter.getBufferedImage(src,1);
    }

}
