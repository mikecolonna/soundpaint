package edu.brown.cs.guihandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.MultipartConfigElement;

import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.VideoDB;
import edu.brown.cs.soundpaint.GuiProcessor;
import edu.brown.cs.tratchfo.SoundRead;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

public class SendRenderHandler implements Route {
  
  private GuiProcessor guiProcessor;
  
  public SendRenderHandler(GuiProcessor gp) {
    guiProcessor = gp;
  }
  
  @Override
  public Object handle(Request req, Response response) throws Exception {
    File audioFile;
    File videoFile;
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    
    try (InputStream is = req.raw().getPart("videoName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
   
      videoFile = new File("src/main/resources/test/video/test_video.mp4");
      OutputStream outStream = new FileOutputStream(videoFile);
      outStream.write(buffer);
    }
    
    String userId = guiProcessor.getSessionsToUsers().get(req.session().id());
    
    // put video in database
    VideoDB video = VideoDB.createVideo(VideoDB.generateId(), userId, videoFile.getAbsolutePath());
    
    try (InputStream is = req.raw().getPart("audioName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
   
      audioFile = new File("src/main/resources/test/audio/test_audio.wav");
      OutputStream outStream = new FileOutputStream(audioFile);
      outStream.write(buffer);
    }
    
    // put audio in database
    AudioDB audio = AudioDB.createAudio(
        AudioDB.generateId(), video.getId(), audioFile.getAbsolutePath(), null, null, null);
    
    
    
    
    
    return null;
  }

}
