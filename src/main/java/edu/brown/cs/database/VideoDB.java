package edu.brown.cs.database;

public interface VideoDB {
  
  String getId();
  
  String getUserId();
  
  String getFilePath();
  
  static VideoDB createVideo(String id, String userId, String filepath) {
    return new VideoDBProxy(id, userId, filepath);
  }
  
  static VideoDB get(String id) {
    return Database.getVideo(id);
  }

}
