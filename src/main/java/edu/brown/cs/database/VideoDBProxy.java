package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VideoDBProxy implements VideoDB {
  
  private String id;
  private String userId;
  private String filepath;
  private String pub;
  
  public VideoDBProxy(String vId, String vUserId, String vFilepath, String vPub) {
    id = vId;
    userId = vUserId;
    filepath = vFilepath;
    pub = vPub;
    
    // put in database if not there
    // all info from <form> fields must be passed to VideoDBProxy
    try (Connection conn = Database.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(
          "INSERT OR IGNORE INTO \"video\" VALUES (?, ?, ?, ?);")) {
        prep.setString(1, id);
        prep.setString(2, userId);
        prep.setString(3, filepath);
        prep.setString(4, pub);
        prep.addBatch();
        prep.executeBatch();
      }
    } catch (SQLException sqle) {
      System.out.println("ERROR: Could not read from database.");
    }
    
    Database.putVideoInCache(id, this);
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
