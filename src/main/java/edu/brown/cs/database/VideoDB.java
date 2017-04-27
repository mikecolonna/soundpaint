package edu.brown.cs.database;

import java.util.UUID;

public interface VideoDB {
  
  String getId();
  
  String getUserId();
  
  String getFilePath();
  
  static VideoDB createVideo(String id, String userId, String filepath) {
    return new VideoDBProxy(id, userId, filepath);
  }
  
  static VideoDB createDummy(String id, String userId, String filepath) {
    return new VideoDBDummy(id, userId, filepath);
  }
  
  static VideoDB get(String id) {
    return Database.getVideo(id);
  }
  
  static String generateId() {
    String uid = "v_" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
