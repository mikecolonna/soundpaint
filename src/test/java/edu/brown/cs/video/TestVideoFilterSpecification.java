package edu.brown.cs.video;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Ty on 09/05/2017.
 */
public class TestVideoFilterSpecification {
  @Test
  public void testInit() {
    VideoFilterSpecification v = new VideoFilterSpecification(VideoParameter.BLUE_TINT,8.0);
    assertNotNull(v);
  }
}
