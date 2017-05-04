package edu.brown.cs.database;

public class VideoDBDummy implements VideoDB {
  
  private String id;
  private String userId;
  private String filepath;
  private String pub;
  
  public VideoDBDummy(String vId, String vUserId, String vFilepath, String vPub) {
    id = vId;
    userId = vUserId;
    filepath = vFilepath;
    pub = vPub;
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
  
  @Override
  public String isPublic() {
    return pub;
  }

}
