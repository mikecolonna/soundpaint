package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AudioDBProxy implements AudioDB {
  
  private String id;
  private String vidId;
  private String src;
  
  public AudioDBProxy(String aId, String aVidId, String aSrc) {
    id = aId;
    vidId = aVidId;
    src = aSrc;
    
    // put in database if not there
    // all info from <form> fields must be passed to AudioDBProxy
    try (Connection conn = Database.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(
          "INSERT OR ABORT INTO audio VALUES (?, ?, ?);")) {
        prep.setString(1, id);
        prep.setString(2, vidId);
        prep.setString(3, src);
        prep.addBatch();
        prep.executeBatch();
      }
    } catch (SQLException sqle) {
      System.out.println("ERROR: Could not read from database.");
    }
    
    Database.putAudioInCache(id, this);
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
