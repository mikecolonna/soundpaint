package edu.brown.cs.video;

/**
 * Created by tynan on 4/21/17.
 */
public class VideoFilterSpecification {
  private double sensitivity;
  private VideoParameter videoParameter;

  public VideoFilterSpecification(VideoParameter videoParameter, double sensitivity) {
    this.sensitivity = sensitivity;
    this.videoParameter = videoParameter;
  }

  public VideoParameter getVideoParameter() {
    return videoParameter;
  }

  public double getSensitivity() {
    return sensitivity;
  }

}
