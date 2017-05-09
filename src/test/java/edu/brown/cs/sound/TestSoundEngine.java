package edu.brown.cs.sound;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Created by Ty on 09/05/2017.
 */
public class TestSoundEngine {
  String shortAudio = "resources/sound/fdr_speech.wav";
  SoundEngine se = new SoundEngine(shortAudio);
  /**
   * Tests if sound parameter values are within range of 0 -1
   */
  @Test
  public void withinRange() {
    se.setSoundReader(1.0/24.0);
    List<Double> freq = se.getMetaData(SoundParameter.FREQUENCY);
    for(double d: freq) {
      assertTrue((d >= 0) && (d <= 1));
    }
    List<Double> genAmp = se.getMetaData(SoundParameter.GENERAL_AMPLITUDE);
    for(double d: genAmp) {
      assertTrue((d >= 0) && (d <= 1));
    }
    List<Double> specAmp = se.getMetaData(SoundParameter.SPECIFIC_AMPLITUDE);
    for(double d: specAmp) {
      assertTrue((d >= 0) && (d <= 1));
    }
    List<Double> tempo = se.getMetaData(SoundParameter.TEMPO);
    for(double d: tempo) {
      assertTrue((d >= 0) && (d <= 1));
    }
  }
}
