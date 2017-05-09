package edu.brown.cs.video;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Ty on 09/05/2017.
 */
public class TestFilterProcessor {

  @Test
  public void testInit() {
    FilterProcessor fp = new FilterProcessor("test_filter");
    assertNotNull(fp);
  }


}
