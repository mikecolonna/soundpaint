package edu.brown.cs.database;

import java.sql.SQLException;
import java.util.UUID;

public interface UserDB {
  
  String getId();
  
  String getUsername();
  
  String getEmail();
  
  String getPassword();
  
  static UserDB createUser(String id, String username, String email, String password) throws SQLException {
    return new UserDBProxy(id, username, email, password);
  }
  
  static UserDB get(String id) {
    return Database.getUser(id);
  }
  
  static String generateId() {
    String uid = "/u/" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
