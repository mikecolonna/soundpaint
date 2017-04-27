package edu.brown.cs.database;

public class VideoDBDummy implements VideoDB {
  
  private String id;
  private String userId;
  private String filepath;
  
  public VideoDBDummy(String vId, String vUserId, String vFilepath) {
    id = vId;
    userId = vUserId;
    filepath = vFilepath;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getUserId() {
    return userId;
  }

  @Override
  public String getFilePath() {
    return filepath;
  }

}
