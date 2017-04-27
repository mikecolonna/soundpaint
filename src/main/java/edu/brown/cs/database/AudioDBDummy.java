package edu.brown.cs.database;

public class AudioDBDummy implements AudioDB {
  
  private String id;
  private String vidId;
  private String src;
  private String ampMd;
  private String freqMd;
  private String pitchMd;
  
  public AudioDBDummy (String aId, String aVidId, String aSrc, 
      String aAmpMd, String aFreqMd, String aPitchMd) {
    id = aId;
    vidId = aVidId;
    src = aSrc;
    ampMd = aAmpMd;
    freqMd = aFreqMd;
    pitchMd = aPitchMd;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getVideoId() {
    return vidId;
  }

  @Override
  public String getSrcFilepath() {
    return src;
  }

  @Override
  public String getAmpMDFilepath() {
    return ampMd;
  }

  @Override
  public String getFreqMDFilepath() {
    return freqMd;
  }

  @Override
  public String getPitchMDFilepath() {
    return pitchMd;
  }

}
