package edu.brown.cs.database;

public class VideoDBDummy implements VideoDB {
  
  private String id;
  private String userId;
  private String filepath;
  private String thumbfilepath;
  private String pub;
  
  public VideoDBDummy(String vId, String vUserId, String vFilepath, String vThumb, String vPub) {
    id = vId;
    userId = vUserId;
    filepath = vFilepath;
    thumbfilepath = vThumb;
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
  public String getFilepath() {
    return filepath;
  }
  
  @Override
  public String isPublic() {
    return pub;
  }

  @Override
  public String getThumbFilepath() {
    return thumbfilepath;
  }

}
