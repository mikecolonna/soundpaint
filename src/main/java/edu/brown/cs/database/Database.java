package edu.brown.cs.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class Database {
  
  private Database() { }
  
  private static String urlToDb;
  
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
  
  public static void resetCaches() {
    users = new HashMap<>();
    videos = new HashMap<>();
    audio = new HashMap<>();
  }
  
  /**
   * Stores a UserDB in the User cache.
   * @param id String ID of the User to store
   * @param actor User to store
   */
  public static void putUserInCache(String id, UserDB user) {
    users.putIfAbsent(id, user);
  }
  
  /**
   * Returns a UserDB from the User cache, specified by the given ID.
   * @param id String ID of the User to get from the cache
   * @return UserDB with corresponding ID; null if UserDB is not in cache
   */
  public static UserDB getUserFromCache(String id) {
    return users.getOrDefault(id, null);
  }
  
  public static void putVideoInCache(String id, VideoDB vid) {
    videos.putIfAbsent(id, vid);
  }
  
  public static VideoDB getVideoFromCache(String id) {
    return videos.getOrDefault(id, null);
  }
  
  public static void putAudioInCache(String id, AudioDB aud) {
    audio.putIfAbsent(id, aud);
  }
  
  public static AudioDB getAudioFromCache(String id) {
    return audio.getOrDefault(id, null);
  }

}
