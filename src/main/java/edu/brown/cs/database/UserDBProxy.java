package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDBProxy implements UserDB {
  
  private String id;
  private String username;
  private String email;
  private String password;
  
  public UserDBProxy(String uId, String uUsername, String uEmail, String uPassword) throws SQLException {
    id = uId;
    username = uUsername;
    email = uEmail;
    password = uPassword;
    
    // put in database if not there
    // all info from <form> fields must be passed to UserDBProxy
    try (Connection conn = Database.getConnection()) {
      try (PreparedStatement prep = conn.prepareStatement(
          "INSERT OR ABORT INTO user VALUES (?, ?, ?, ?);")) {
        prep.setString(1, id);
        prep.setString(2, username);
        prep.setString(3, email);
        prep.setString(4, password);
        prep.addBatch();
        prep.executeBatch();
      }
    }
    
    Database.putUserInCache(id, this);
  }

  @Override
  public String getId() {
    return id;
  }
  
  @Override
  public String getUsername() {
    return username;
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
