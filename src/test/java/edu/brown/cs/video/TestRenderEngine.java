package edu.brown.cs.video;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Ty on 09/05/2017.
 */
public class TestRenderEngine {

  @Test
  public void testInit() {
    RenderEngine r = new RenderEngine();
    assertNotNull(r);
  }
}
