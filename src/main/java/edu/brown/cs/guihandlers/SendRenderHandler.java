package edu.brown.cs.guihandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.bytedeco.javacv.FFmpegFrameGrabber;

import edu.brown.cs.database.AudioDB;
import edu.brown.cs.database.VideoDB;
import edu.brown.cs.soundpaint.GuiProcessor;
import edu.brown.cs.soundpaint.VideoSoundParameterMapping;
import edu.brown.cs.video.ExtractWav;
import edu.brown.cs.video.RenderEngine;
import edu.brown.cs.video.VideoParameter;
import edu.brown.cs.sound.SoundEngine;
import edu.brown.cs.sound.SoundParameter;
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
    
    // extract video and store in file system
    String outputVideoFilepath;
    try (InputStream is = req.raw().getPart("videoName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      
      String filepath = "./src/main/resources/static/users/" + username + "/" + videoId;
      if (!new File(filepath).exists()) {
        new File(filepath).mkdir();
      }
      
      outputVideoFilepath = filepath + "/output.mp4";
      videoFile = new File(filepath + "/src_video.mp4");
      OutputStream outStream = new FileOutputStream(videoFile);
      outStream.write(buffer);
    }
    
    String userId = guiProcessor.getSessionsToUsers().get(req.session().id());
    
    // put video in database
    VideoDB video = VideoDB.createVideo(videoId, userId, outputVideoFilepath, "true");
    
    // extract audio and store in file system
    String outputAudioFilepath;
    try (InputStream is = req.raw().getPart("audioName").getInputStream()) {
      byte[] buffer = new byte[is.available()];
      is.read(buffer);
      
      String filepath = "./src/main/resources/static/users/" + username + "/" + videoId + "/audio";
      if (!new File(filepath).exists()) {
        new File(filepath).mkdir();
      }
   
      outputAudioFilepath = filepath + "/extracted_wav_audio.wav";
      audioFile = new File(filepath + "/src_audio.wav");
      OutputStream outStream = new FileOutputStream(audioFile);
      outStream.write(buffer);
    }
    
    // TRANSCODE audio as a wav
    ExtractWav.extractWav(audioFile.getAbsolutePath(), outputAudioFilepath);
    
    // put TRANSCODED audio in database
    AudioDB audio = AudioDB.createAudio(
        audioId, video.getId(), outputAudioFilepath, null, null, null);

    // extract filters
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
    
    // map audio – video filters
    List<VideoSoundParameterMapping> mappings = new ArrayList<>();
    for (int i = 0; i < filters.size(); i += 2) {
      SoundParameter sp = null;
      switch (filters.get(i)) {
        case "Amplitude":
          sp = SoundParameter.GENERAL_AMPLITUDE;
          break;
        case "Frequency":
          sp = SoundParameter.FREQUENCY;
          break;
        case "Tempo":
          sp = SoundParameter.TEMPO;
          break;
      }
      
      VideoParameter vp = null;
      switch (filters.get(i + 1)) {
        case "Tint":
          vp = VideoParameter.TINT;
          break;
        case "Push":
          vp = VideoParameter.PUSH;
          break;
        case "Bulge":
          vp = VideoParameter.BULGE;
          break;
        case "Emboss":
          vp = VideoParameter.EMBOSS;
          break;
      }
      
      mappings.add(new VideoSoundParameterMapping(vp, sp, 0.5));
    }

    FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoFile.getAbsolutePath());
    SoundEngine soundEngine = new SoundEngine(audioFile.getAbsolutePath());
    RenderEngine.renderVideo(mappings, frameGrabber, soundEngine, outputVideoFilepath);

    JsonObject videoAudioInfo = new JsonObject();
    videoAudioInfo.addProperty("videoid", videoId);
    videoAudioInfo.addProperty("videofp", outputVideoFilepath.substring(28));
    videoAudioInfo.addProperty("audioid", audioId);
    videoAudioInfo.addProperty("audiofp", outputAudioFilepath.substring(28));
    
    return videoAudioInfo;
  }

}
