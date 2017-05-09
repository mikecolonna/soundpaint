package edu.brown.cs.sound;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
//import org.json.*;

public interface JSONBuilder {

	public static JsonObject convert(List< float [] > toConvert) {
		JsonObject jo = new JsonObject();
		double min = Double.MAX_VALUE;
		double max = -Double.MAX_VALUE;
		for(int i = 0; i < toConvert.size();i++) {
			JsonObject indexedFreqData = new JsonObject();
			float [] currentArray = toConvert.get(i);

			for(int j = 0; j < 30; j++) {
				System.out.println(j);
				if(currentArray[j] > max) {
					max = currentArray[j];
				}
				if(currentArray[j] < min) {
					min = currentArray[j];
				}
				indexedFreqData.add(Integer.toString(j), new JsonPrimitive(currentArray[j]));
			}
			jo.add(Integer.toString(i),indexedFreqData);
		}
		System.out.println("NUMBER OF SOUND FRAMEs: " + toConvert.size());
		System.out.println("MAX: " + max + " MIN: " + min);
		return jo;
	}


}
