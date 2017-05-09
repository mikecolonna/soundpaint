package edu.brown.cs.sound;


import org.junit.Test;
import static org.junit.Assert.*;


public class TestSoundRead extends SoundRead{
  String shortAudio = "resources/sound/fdr_speech.wav";

  /**
   * Tests whether SoundRead reads
   * correct amount of frames for frameRate
   */
  @Test
  public void readFramesTest() {
    SoundRead sr = new SoundRead(1.0/24.0);
    sr.read(shortAudio);
    assertTrue(sr.soundFramesPerVideoFrame == 1838);

  }


}
