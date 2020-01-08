package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A generic Implementation for JSON serializable network messages. This kind of
 * structure give us the benefit to split meta data and concrete data.
 *
 * @param <T> concrete data the message contains
 */
public class Message<T> {

  /**
   * The message type, automatically set to the class name of the given object.
   */
  private String type;

  /**
   * The class name of the data typed, which will be used for consistent serialization.
   */
  private Class<T> dataClass;

  /**
   * The concrete object which should be transferred by the message.
   */
  private T data;

  /**
   * Create a new message with the given type and data.
   * Message Type have to be defined clearly for all endpoints.
   *
   * @param type Type of the message as string.
   * @param data Data of the message
   */
  public Message(String type, T data, Class<T> dataClass) {
    this.type = type;
    this.dataClass = dataClass;
    this.data = data;
  }

  /**
   * Get the type of the message.
   *
   * @return Type of the message as string
   */
  public String getType() {
    return type;
  }

  /**
   * Concrete data class of the message.
   *
   * @return Data class of the message.
   */
  public Class<T> getDataClass() {
    return dataClass;
  }

  /**
   * Concrete data of the message.
   *
   * @return Data of the message.
   */
  public T getData() {
    return data;
  }

  /**
   * Converts this the message to a json object.
   *
   * @return
   */
  public String toJson() {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    return gson.toJson(this);
  }

  /**
   * Cuts off "class " in beginning of a class name to make it usable by
   * Class.forName().
   *
   * @param className Name of a class
   * @return Sanitized name of the class
   */
  public static final String sanitizeToStringFromGetClass(String className) {
    if (className.substring(0, 6).equals("class ")) {
      className = className.substring(6);
    }

    return className;
  }

  /**
   * Creates a {@link Message} from the given JSON string.
   *
   * @param json String which represented the message.
   * @return Message from the json
   */
  public static Message fromJson(String json) {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    System.out.println();
    System.out.println(json);
    System.out.println();

    Message jsonParsedMessage = gson.fromJson(json, Message.class);

    return jsonParsedMessage;
  }

}
