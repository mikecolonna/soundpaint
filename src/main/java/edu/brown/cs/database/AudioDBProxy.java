package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AudioDBProxy implements AudioDB {
  
  private String id;
  private String vidId;
  private String src;
  private String ampMd;
  private String freqMd;
  private String tempoMd;
  
  public AudioDBProxy(String aId, String aVidId, String aSrc, 
      String aAmpMd, String aFreqMd, String aTempoMd) {
    id = aId;
    vidId = aVidId;
    src = aSrc;
    ampMd = aAmpMd;
    freqMd = aFreqMd;
    tempoMd = aTempoMd;
    
    // put in database if not there
    // all info from <form> fields must be passed to AudioDBProxy
    try (Connection conn = Database.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(
          "INSERT OR IGNORE INTO \"audio\" VALUES (?, ?, ?, ?, ?, ?);")) {
        prep.setString(1, id);
        prep.setString(2, vidId);
        prep.setString(3, src);
        prep.setString(4, ampMd);
        prep.setString(5, freqMd);
        prep.setString(6, tempoMd);
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

  @Override
  public String getAmpMDFilepath() {
    return ampMd;
  }

  @Override
  public String getFreqMDFilepath() {
    return freqMd;
  }

  @Override
  public String getTempoMDFilepath() {
    return tempoMd;
  }

}
