package edu.brown.cs.guihandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.VideoDB;
import edu.brown.cs.soundpaint.GuiProcessor;
import edu.brown.cs.sound.SoundRead;
import spark.Request;
import spark.Response;
import spark.Route;

import com.google.gson.*;

public class SendRenderHandler implements Route {
  
  private GuiProcessor guiProcessor;
  private Gson gson = new Gson();
  
  public SendRenderHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object handle(Request req, Response response) throws Exception {

    String username = req.session().attribute("username");
    
    File audioFile;
    String audioId = AudioDB.generateId();
    File videoFile;
    String videoId = VideoDB.generateId();
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    
    try (InputStream is = req.raw().getPart("videoName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      
      String filepath = "./src/main/resources/users/" + username + "/" + videoId;
      if (!new File(filepath).exists()) {
        new File(filepath).mkdir();
      }
      
      videoFile = new File(filepath + "/test_video.mp4");
      OutputStream outStream = new FileOutputStream(videoFile);
      outStream.write(buffer);
    }
    
    String userId = guiProcessor.getSessionsToUsers().get(req.session().id());
    
    // put video in database
    VideoDB video = VideoDB.createVideo(videoId, userId, videoFile.getAbsolutePath());
    
    try (InputStream is = req.raw().getPart("audioName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      
      String filepath = "./src/main/resources/users/" + username + "/" + videoId + "/audio";
      if (!new File(filepath).exists()) {
        new File(filepath).mkdir();
      }
   
      audioFile = new File(filepath + "/test_audio.wav");
      OutputStream outStream = new FileOutputStream(audioFile);
      outStream.write(buffer);
    }
    
    // put audio in database
    AudioDB audio = AudioDB.createAudio(
        audioId, video.getId(), audioFile.getAbsolutePath(), null, null, null);
    
    List<String> filters;
    try (InputStream is = req.raw().getPart("filters").getInputStream()) {
      final int bufferSize = 1024;
      final char[] buffer = new char[bufferSize];
      final StringBuilder out = new StringBuilder();
      Reader in = new InputStreamReader(is, "UTF-8");
      while (true) {
        int rsz = in.read(buffer, 0, buffer.length);
        if (rsz < 0) {
            break;
        }
        out.append(buffer, 0, rsz);
      }

      filters = gson.fromJson(out.toString(), List.class);
    }
    
    for (int i = 0; i < filters.size(); i += 2) {
      
      
    }
    
    
    
    
    
    return null;
  }

}
