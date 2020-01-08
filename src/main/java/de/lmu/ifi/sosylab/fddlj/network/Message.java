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
  public Message(String type, T data) {
    this.type = type;
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

}
