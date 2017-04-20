package edu.brown.cs.database;

public interface UserDB {
  
  String getId();
  
  String getEmail();
  
  String getPassword();
  
  static UserDB createUser(String id, String email, String password) {
    return new UserDBProxy(id, email, password);
  }
  
  static UserDB get(String id) {
    return Database.getUserFromCache(id);
  }

}
