package edu.brown.cs.database;

public class UserDBDummy implements UserDB {
  
  private String id;
  private String username;
  private String email;
  private String password;
  
  public UserDBDummy (String uId, String uUsername, String uEmail, String uPassword) {
    id = uId;
    username = uUsername;
    email = uEmail;
    password = uPassword;
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
