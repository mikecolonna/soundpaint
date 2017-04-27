package edu.brown.cs.database;

import java.util.UUID;

public interface AudioDB {
  
  String getId();
  
  String getVideoId();
  
  String getSrcFilepath();
  
  String getAmpMDFilepath();
  
  String getFreqMDFilepath();
  
  String getPitchMDFilepath();
  
  static AudioDB createAudio(String id, String videoId, String srcFilepath,
      String ampFilepath, String freqFilepath, String pitchFilepath) {
    return new AudioDBProxy(id, videoId, srcFilepath, 
        ampFilepath, freqFilepath, pitchFilepath);
  }
  
  static AudioDB createDummy(String id, String videoId, String srcFilepath,
      String ampFilepath, String freqFilepath, String pitchFilepath) {
    return new AudioDBDummy(id, videoId, srcFilepath, 
        ampFilepath, freqFilepath, pitchFilepath);
  }
  
  static AudioDB get(String id) {
    return Database.getAudio(id);
  }
  
  static String generateId() {
    String uid = "/a/" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
