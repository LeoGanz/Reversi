package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.Gson;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Tests for {@link Message}.
 *
 * @author Leonard Ganz
 */
public class MessageTest {

  private final Player dummy = new PlayerImpl("Dummy", Color.BLUEVIOLET);
  private final Message<Player> dummyMessage = new Message<>(dummy);
  private final Message<Boolean> trueMessage = new Message<>(true);


  @Test
  public void testMessage_null() {
    try {
      @SuppressWarnings("unused")
      Message<Player> message = new Message<>(null);
      Assertions.fail("Class of null should not be found");
    } catch (@SuppressWarnings("unused") NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testDataClass_Player() {
    Class<PlayerImpl> type = PlayerImpl.class;

    try {
      Assertions.assertEquals(
          type,
          dummyMessage.getDataClass(),
          "getDataClass() returned "
              + dummyMessage.getDataClass()
              + " instead of expected "
              + type);
    } catch (@SuppressWarnings("unused") ClassNotFoundException e) {
      Assertions.fail("Data class should have been found");
    }
  }

  @Test
  public void testGetData() {
    Assertions.assertEquals(dummy, dummyMessage.getData());
  }

  @Test
  public void testJsonSerialization_Regular() {

    ArrayList<Message<?>> messages = new ArrayList<>();
    messages.add(trueMessage);
    messages.add(dummyMessage);

    for (Message<?> message : messages) {
      testToJason(message);
      String jsonMessage = message.toJson();

      Message<?> fromJson = Message.fromJson(jsonMessage);

      Assertions.assertEquals(message.getData(), fromJson.getData());
      Assertions.assertEquals(message, fromJson);
    }
  }

  private void testToJason(Message<?> message) {
    try {
      testToJason(message, message.getDataClass().getName(), false);
    } catch (@SuppressWarnings("unused") ClassNotFoundException e) {
      Assertions.fail("Data class should have been found");
    }
  }

  private void testToJason(Message<?> message, String dataClassName, boolean dataNull) {
    Gson gson = new Gson();
    String jsonString;
    if (dataNull) {
      jsonString = "{\"" + Message.DATA_CLASS_FIELD + "\":\"" + dataClassName + "\"}";
    } else {
      jsonString = "{\"" + Message.DATA_CLASS_FIELD + "\":\"" + dataClassName + "\","
        + "\"" + Message.DATA_FIELD + "\":" + gson.toJson(message.getData()) + "}";
    }
    String jsonMessage = message.toJson();

    Assertions.assertEquals(
        jsonString,
        jsonMessage,
        "toJson() returned " + jsonMessage + " instead of expected " + jsonString);
  }

  @Test
  public void testJsonSerialization_InvalidDataClass() {
    final String fakeClassString = "not.a.Class";
    try {
      Class<?> clazz = Message.class;
      Constructor<?> constructor = clazz.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object cc = constructor.newInstance();
      Field dataClassField = cc.getClass().getDeclaredField(Message.DATA_CLASS_FIELD);
      dataClassField.setAccessible(true);
      dataClassField.set(cc, fakeClassString);
      Message<?> invalidMessage = (Message<?>) cc;
      try {
        invalidMessage.getDataClass();
        Assertions.fail("No class should be found for " + fakeClassString);
      } catch (@SuppressWarnings("unused") ClassNotFoundException e) {
        // expected
      }

      testToJason(invalidMessage, fakeClassString, true);
      String jsonMessage = invalidMessage.toJson();
      Message<?> fromJson = Message.fromJson(jsonMessage);
      Assertions.assertEquals(
          invalidMessage,
          fromJson,
          "Invalid data class should have been set during deserialization");
      try {
        fromJson.getDataClass();
        Assertions.fail("No class should be found for " + fakeClassString);
      } catch (@SuppressWarnings("unused") ClassNotFoundException e) {
        // expected
      }
    } catch (@SuppressWarnings("unused")
        NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException
        | InstantiationException
        | InvocationTargetException
        | NoSuchMethodException e) {
      // e.printStackTrace();
      Assertions.fail("Reflection should not have failed when setting private field");
    }
  }

  @Test
  public void testPrettifyJson() {
    String dataClassName = String.class.getName();
    String data = "My Test Data";
    String rawJson = "{\"" + Message.DATA_CLASS_FIELD + "\":\"" + dataClassName + "\","
        + "\"" + Message.DATA_FIELD + "\":\"" + data + "\"}";
    String prettyJson = "{\n  \"" + Message.DATA_CLASS_FIELD + "\": \"" + dataClassName + "\",\n"
        + "  \"" + Message.DATA_FIELD + "\": \"" + data + "\"\n}";
    String prettifiedJson = Message.prettifyJson(rawJson);
    Assertions.assertEquals(prettyJson, prettifiedJson);
  }

  @Test
  public void testEqualsContract() {
    EqualsVerifier.forClass(Message.class).verify();
  }
}
