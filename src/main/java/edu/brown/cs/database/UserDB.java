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
  
  static UserDB createDummy(String id, String username, String email, String password) {
    return new UserDBDummy(id, username, email, password);
  }
  
  static UserDB getById(String id) {
    return Database.getUserById(id);
  }
  
  static String loginUser(String email, String password) throws SQLException {
    return Database.loginUser(email, password);
  }
  
  static String generateId() {
    String uid = "u_" + UUID.randomUUID().toString().replace("-", "");
    return uid;
  }

}
