package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ArrayList;
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
    Message message = new Message<>(type, new Object());

    Assertions.assertEquals(
            type,
            message.getType(),
            "getType() returned " + message.getType() + " instead of expected " + type);
  }

  @Test
  public void testGetData() {
    Object object = new Object();
    Message message = new Message<>("my-type", object);

    Assertions.assertEquals(
            object,
            message.getData(),
            "getData() returned " + message.getData() + " instead of expected " + object);
  }

  @Test
  public void testJsonSerialization() {

    ArrayList<Message> messages = new ArrayList<>();
    messages.add(new Message<>("boolean", true));
    messages.add(new Message<>("string", "Welcome!"));
    messages.add(new Message<>("string", new PlayerImpl("Max", new Color(0, 0, 0, 1.0))));

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    for (Message message : messages) {
      String jsonString =
              "{\"type\":\"" + message.getType() + "\","
              + "\"data\":" + gson.toJson(message.getData()) + "}";

      System.out.println(jsonString);

      Assertions.assertEquals(
              jsonString,
              message.toJson(),
              "getData() returned " + message.toJson() + " instead of expected " + jsonString);
    }

  }

}
