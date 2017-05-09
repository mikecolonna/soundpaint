package edu.brown.cs.database;

import java.util.UUID;

public interface AudioDB {
  
  String getId();
  
  String getVideoId();
  
  String getSrcFilepath();
  
  static AudioDB createAudio(String id, String videoId, String srcFilepath) {
    return new AudioDBProxy(id, videoId, srcFilepath);
  }
  
  static AudioDB createDummy(String id, String videoId, String srcFilepath) {
    return new AudioDBDummy(id, videoId, srcFilepath);
  }
  
  static AudioDB get(String id) {
    return Database.getAudio(id);
  }
  
  static String generateId() {
    String uid = "a_" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
