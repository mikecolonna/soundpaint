package edu.brown.cs.sound;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;
//import org.json.*;

public interface JSONBuilder {

	public static JsonObject convert(List< float [] > toConvert) {
		JsonObject jo = new JsonObject();

		for(int i = 0; i < toConvert.size();i++) {
			JsonObject indexedFreqData = new JsonObject();
			float [] currentArray = toConvert.get(i);
			for(int j = 0; j < currentArray.length; j++) {
				indexedFreqData.add(Integer.toString(j), new JsonPrimitive(currentArray[j]));
			}
			jo.add(Integer.toString(i),indexedFreqData);
		}

		return jo;
	}
}
