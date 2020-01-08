package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Tests for {@link Message}.
 *
 * @author Florian Theimer
 */
public class MessageTest {

  @Test
  public void testGetType() {
    String type = "my-type";
    Message message = new Message<>(type, new Object(), Object.class);

    Assertions.assertEquals(
            type,
            message.getType(),
            "getType() returned " + message.getType() + " instead of expected " + type);
  }

  @Test
  public void testGetData() {
    Object object = new Object();
    Message message = new Message<>("my-type", object, Object.class);

    Assertions.assertEquals(
            object,
            message.getData(),
            "getData() returned " + message.getData() + " instead of expected " + object);
  }

  @Test
  public void testSanitizeToStringFromGetClass() {
    String[][] expectedAndActual = new String[][]{
      {"java.lang.Boolean",       "java.lang.String", "dd ava.lang.String"},
      {"class java.lang.Boolean", "java.lang.String", "dd ava.lang.String" }
    };

    for (int i = 0; i < expectedAndActual[0].length; i++) {
      String expected = expectedAndActual[0][i];
      String actual = Message.sanitizeToStringFromGetClass(expectedAndActual[1][i]);

      Assertions.assertEquals(
              expected,
              actual,
              "sanitizeToStringFromGetClass() returned "
                      + actual
                      + " instead of expected " + expected);
    }
  }

  /*
  @Test
  public void testJsonSerialization() {

    ArrayList<Message> messages = new ArrayList<>();
    messages.add(new Message<>("boolean", true, Boolean.class));
    messages.add(new Message<>("string", "Welcome!", String.class));
    messages.add(new Message<>("string",
            new PlayerImpl("Max", new Color(0, 0, 0, 1.0)), PlayerImpl.class)
    );

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    for (Message message : messages) {
      // Cut of "class " in beginning
      String dataClassName = Message.sanitizeToStringFromGetClass(
              message.getData().getClass().toString()
      );

      String jsonString =
              "{\"type\":\"" + message.getType() + "\","
              + "\"dataClass\":\"" + dataClassName + "\","
              + "\"data\":" + gson.toJson(message.getData()) + "}";
      String jsonMessage = message.toJson();

      Assertions.assertEquals(
              jsonString,
              jsonMessage,
              "toJson() returned " + jsonMessage + " instead of expected " + jsonString);

      Message fromJson = Message.fromJson(jsonMessage);

      Assertions.assertEquals(
              message.getData().getClass(),
              fromJson.getData().getClass()
      );
    }
    */

  }

}
