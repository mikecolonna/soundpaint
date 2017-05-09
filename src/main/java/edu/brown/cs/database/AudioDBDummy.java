package edu.brown.cs.database;

public class AudioDBDummy implements AudioDB {
  
  private String id;
  private String vidId;
  private String src;
  
  public AudioDBDummy (String aId, String aVidId, String aSrc) {
    id = aId;
    vidId = aVidId;
    src = aSrc;
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

}
