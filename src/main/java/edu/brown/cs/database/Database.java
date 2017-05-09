package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public final class Database {
  
  private Database() { }
  
  private static String urlToDb;
  private static boolean isConnected = false;
  
  // caches
  private static Map<String, UserDB> users;
  private static Map<String, VideoDB> videos;
  private static Map<String, AudioDB> audio;
  
  /**
   * Sets the current path to the Database.
   * @param url the String URL of the current Database
   */
  public static void setPath(String url) {
    urlToDb = url;
  }
  
  /**
   * Returns a closable Connection to the current Database.
   * @return a closable Connection to the current Database.
   * @throws SQLException if connection cannot be made
   */
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(urlToDb);
  }
  
  public static void connected(boolean bool) {
    isConnected = bool;
  }
  
  public static boolean isConnected() {
    return isConnected;
  }
  
  public static void createTables() {
    try (Connection conn = DriverManager.getConnection(urlToDb)) {
      try (PreparedStatement prep1 = conn.prepareStatement(
          "CREATE TABLE IF NOT EXISTS user("
          + "id TEXT PRIMARY KEY,"
          + "username TEXT UNIQUE,"
          + "email TEXT UNIQUE,"
          + "password TEXT NOT NULL);")) {
        prep1.executeUpdate();
      }
      
      try (PreparedStatement prep2 = conn.prepareStatement(
          "CREATE TABLE IF NOT EXISTS video("
          + "id TEXT PRIMARY KEY,"
          + "user_id TEXT,"
          + "filepath TEXT NOT NULL UNIQUE,"
          + "thumb TEXT,"
          + "public TEXT,"
          + "FOREIGN KEY (user_id) REFERENCES user (id));")) {
        prep2.executeUpdate();
      }
      
      try (PreparedStatement prep3 = conn.prepareStatement(
          "CREATE TABLE IF NOT EXISTS audio("
          + "id TEXT PRIMARY KEY,"
          + "video_id TEXT,"
          + "src TEXT,"
          + "FOREIGN KEY (video_id) REFERENCES video (id));")) {
        prep3.executeUpdate();
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
  
  public static void resetCaches() {
    users = new HashMap<>();
    videos = new HashMap<>();
    audio = new HashMap<>();
  }
  
  /**
   * Stores a UserDB in the User cache.
   * @param id String ID of the User to store
   * @param user User to store
   */
  public static void putUserInCache(String id, UserDB user) {
    users.putIfAbsent(id, user);
  }
  
  /**
   * Returns a UserDB from the User cache, specified by the given ID.
   * @param id String ID of the User to get from the cache
   * @return UserDB with corresponding ID; null if UserDB is not in cache
   */
  public static UserDB getUserById(String id) {
    // check cache first
    UserDB user = users.get(id);
    
    // if user is not in cache
    if (user == null) {
      try (Connection conn = DriverManager.getConnection(urlToDb)) {
        try (PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM user WHERE user.id = ?")) {
          prep.setString(1, id);
          try (ResultSet rs = prep.executeQuery()) {
            if (rs.next()) {
              user = UserDB.createDummy(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
              users.put(id, user);
            }
          }
        } 
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    return user;
  }
  
  /**
   * Retrieves User from database with specified email and password.
   * @param email String email given by the User
   * @param password String password given by the User
   * @return String ID of corresponding User; null if User is not in the database
   */
  public static String loginUser(String email, String password) throws SQLException {
    String userId = null;
    try (Connection conn = DriverManager.getConnection(urlToDb)) {
      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT id FROM user WHERE user.email = ? AND user.password = ?")) {
        prep.setString(1, email);
        prep.setString(2, password);
        try (ResultSet rs = prep.executeQuery()) {
          userId = rs.getString(1);
        }
      } 
    }
   
    return userId;
  }
  
  public static void putVideoInCache(String id, VideoDB vid) {
    videos.putIfAbsent(id, vid);
  }
  
  public static VideoDB getVideo(String id) {
    // check cache first 
    VideoDB video = videos.get(id);
    
    // if video is not in cache
    if (video == null) {
      try (Connection conn = DriverManager.getConnection(urlToDb)) {
        try (PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM video WHERE video.id = ?")) {
          prep.setString(1, id);
          try (ResultSet rs = prep.executeQuery()) {
            if (rs.next()) {
              video = VideoDB.createVideo(rs.getString(1), rs.getString(2), 
                  rs.getString(3), rs.getString(4), rs.getString(5));
              videos.put(id, video);
            }
          }
        } 
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    return video;
  }
  
  public static void putAudioInCache(String id, AudioDB aud) {
    audio.putIfAbsent(id, aud);
  }
  
  public static AudioDB getAudio(String id) {
    // check cache first
    AudioDB aud = audio.get(id);
    
    // if audio is not in cache
    if (aud == null) {
      try (Connection conn = DriverManager.getConnection(urlToDb)) {
        try (PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM audio WHERE audio.id = ?")) {
          prep.setString(1, id);
          try (ResultSet rs = prep.executeQuery()) {
            if (rs.next()) {
              aud = AudioDB.createAudio(rs.getString(1),
                  rs.getString(2), rs.getString(3));
              audio.put(id, aud);
            }
          }
        } 
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    
    return aud;
  }
  
  public static List<List<String>> getPublicThumbnailFilepaths() {
    List<List<String>> thumbList = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(urlToDb)) {
      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT video.thumb, video.id, user.username "
          + "FROM video, user "
          + "WHERE video.user_id = user.id "
          + "AND video.public = 'true'")) {
        try (ResultSet rs = prep.executeQuery()) {
          System.out.println("GOT RESULT SET");
          List<String> currList;
          while (rs.next()) {
            currList = new ArrayList<>();
            currList.add(rs.getString(1).substring(28));
            currList.add(rs.getString(2));
            currList.add(rs.getString(3));
            thumbList.add(currList);
          }
        }
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
    return thumbList;
  }
  
  public static List<List<String>> getUserThumbnailFilepaths(String userId) {
    List<List<String>> thumbList = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(urlToDb)) {
      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT video.thumb, video.id "
          + "FROM video "
          + "WHERE video.user_id = ?")) {
        prep.setString(1, userId);
        try (ResultSet rs = prep.executeQuery()) {
          List<String> currList;
          while (rs.next()) {
            currList = new ArrayList<>();
            currList.add(rs.getString(1).substring(28));
            currList.add(rs.getString(2));
            currList.add("");
            thumbList.add(currList);
          }
        }
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    return thumbList;
  }
  
  public static List<String> getVideoInfo(String vid) {
    List<String> videoInfo = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(urlToDb)) {
      try (PreparedStatement prep = conn.prepareStatement(
          "SELECT * from video, audio"
          + "WHERE video.id = audio.video_id"
          + "AND video.id = ?")) {
        prep.setString(1, vid);
        try (ResultSet rs = prep.executeQuery()) {
          // video fp
          videoInfo.add(rs.getString(3).substring(28));
          // audio src fp
          videoInfo.add(rs.getString(8).substring(28));
        }
      }
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    return videoInfo;
  }

}
