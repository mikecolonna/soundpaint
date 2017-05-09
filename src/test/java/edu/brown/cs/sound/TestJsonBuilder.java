package edu.brown.cs.sound;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ty on 09/05/2017.
 */
public class TestJsonBuilder {

  @Test
  public void convertSize() {
    List<float []> test = new ArrayList<float []>();
    float [] f = {(float) 2.0,(float) 3.0};
    test.add(f);

    List<float []> test1 = new ArrayList<float []>();
    float [] f1 = {(float) 2.0,(float) 3.0};
    test1.add(f);
    test1.add(f1);
    JsonObject jo = JSONBuilder.convert(test);
    JsonObject jo1 = JSONBuilder.convert(test1);

    assertTrue(jo.size() == 1);
    assertTrue(jo1.size() == 2);
  }

  @Test
  public void convert() {
    List<float []> test = new ArrayList<float []>();
    float [] f = {(float) 2.0,(float) 3.0};
    test.add(f);

    List<float []> test1 = new ArrayList<float []>();
    float [] f1 = {(float) 2.0,(float) 3.0};
    test1.add(f);
    test1.add(f1);
    JsonObject jo = JSONBuilder.convert(test);
    JsonObject jo1 = JSONBuilder.convert(test1);

    JsonObject check = new JsonObject();
    JsonObject nest = new JsonObject();
    nest.add(Integer.toString(0), new JsonPrimitive((2.0)));
    nest.add(Integer.toString(1), new JsonPrimitive((3.0)));
    check.add(Integer.toString(0), nest);


    JsonObject check1 = new JsonObject();
    JsonObject nest1 = new JsonObject();
    nest1.add(Integer.toString(0), new JsonPrimitive((2.0)));
    nest1.add(Integer.toString(1), new JsonPrimitive((3.0)));
    check1.add(Integer.toString(0), nest);
    check1.add(Integer.toString(1), nest1);

    assertTrue(jo.equals(check) );
    assertTrue(jo1.equals(check1));
  }
}
