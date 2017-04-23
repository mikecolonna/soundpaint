package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDBProxy implements UserDB {
  
  private String id;
  private String email;
  private String password;
  
  public UserDBProxy(String uId, String uEmail, String uPassword) {
    id = uId;
    email = uEmail;
    password = uPassword;
    
    // put in database if not there
    // all info from <form> fields must be passed to UserDBProxy
    try (Connection conn = Database.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(
          "INSERT OR IGNORE INTO \"user\" VALUES (?, ?, ?);")) {
        prep.setString(1, id);
        prep.setString(2, email);
        prep.setString(3, password);
        prep.addBatch();
        prep.executeBatch();
      }
    } catch (SQLException sqle) {
      System.out.println("ERROR: Could not read from database.");
    }
    
    Database.putUserInCache(id, this);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }
}
