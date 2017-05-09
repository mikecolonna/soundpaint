package edu.brown.cs.sound;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
//import org.json.*;

public interface JSONBuilder {

	public static JsonObject convert(List< ? > toConvert) {
		JsonObject jo = new JsonObject();

		for(int i = 0; i < toConvert.size();i++) {
			jo.add(Integer.toString(i),new JsonPrimitive(new Gson().toJson(toConvert.get(i))));
		}

		return jo;
	}
}
