package edu.brown.cs.database;

import java.util.UUID;

public interface VideoDB {
  
  String getId();
  
  String getUserId();
  
  String getFilepath();
  
  String getThumbFilepath();
  
  String isPublic();
  
  static VideoDB createVideo(String id, String userId, String filepath, String thumbfilepath, String pub) {
    return new VideoDBProxy(id, userId, filepath, thumbfilepath, pub);
  }
  
  static VideoDB createDummy(String id, String userId, String filepath, String thumbfilepath, String pub) {
    return new VideoDBDummy(id, userId, filepath, thumbfilepath, pub);
  }
  
  static VideoDB get(String id) {
    return Database.getVideo(id);
  }
  
  static String generateId() {
    String uid = "v_" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
