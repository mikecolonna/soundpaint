package edu.brown.cs.soundpaint;

import edu.brown.cs.sound.SoundParameter;
import edu.brown.cs.video.VideoParameter;

/**
 * Created by Tynan on 4/27/17.
 */
public class VideoSoundParameterMapping {

    private VideoParameter videoParameter;
    private SoundParameter soundParameter;
    private double sensitivity;

    public VideoSoundParameterMapping(VideoParameter videoParameter, SoundParameter soundParameter, double sensitivity) {
        this.videoParameter = videoParameter;
        this.soundParameter = soundParameter;
        this.sensitivity = sensitivity;
    }

    public VideoParameter getVideoParameter() {
        return videoParameter;
    }

    public SoundParameter getSoundParameter() {
        return soundParameter;
    }

    public double getSensitivity() {
        return sensitivity;
    }
 }
