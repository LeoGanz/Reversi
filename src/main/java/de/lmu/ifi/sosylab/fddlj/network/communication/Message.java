package de.lmu.ifi.sosylab.fddlj.network.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import de.lmu.ifi.sosylab.fddlj.model.CustomGson;

/**
 * A generic implementation for JSON serializable network messages. This kind of structure give us
 * the benefit to split meta data and concrete data.
 *
 * @param <T> concrete data type the message contains
 * @author Leonard Ganz
 * @author Florian Theimer (prototype)
 */
public class Message<T> {

  public static final String DATA_CLASS_FIELD = "dataClass";
  public static final String DATA_FIELD = "data";

  /**
   * The fully qualified class name of the data typed, which will be used for consistent
   * serialization.
   */
  @SerializedName(DATA_CLASS_FIELD)
  private final String dataClass;

  /** The concrete object which should be transferred by the message. */
  @SerializedName(DATA_FIELD)
  private final T data;

  /**
   * Create a new message with the given data.
   *
   * @param data data of the message
   * @throws NullPointerException if data is null
   */
  public Message(T data) {
    this.dataClass = data.getClass().getName();
    this.data = data;
  }

  /** Needed for reflection. */
  @SuppressWarnings("unused")
  private Message() {
    dataClass = null;
    data = null;
  }

  /**
   * Concrete data class of the message.
   *
   * @return data class of the message.
   * @throws ClassNotFoundException if class of the message data field is not resolvable
   */
  public Class<?> getDataClass() throws ClassNotFoundException {
    return Class.forName(dataClass);
  }

  /**
   * Concrete data of the message.
   *
   * @return data of the message.
   */
  public T getData() {
    return data;
  }

  /**
   * Converts this message to its json representation.
   *
   * @return a string containing this message encoded with json
   */
  public String toJson() {
    return CustomGson.createGson().toJson(this);
  }

  /**
   * Creates a {@link Message} from the given JSON string.
   *
   * @param json String which represents the message.
   * @return Message decoded from the json string
   */
  public static Message<?> fromJson(String json) {
    return CustomGson.createGson().fromJson(json, Message.class);
  }



  /**
   * Convert a JSON string to pretty print version.
   *
   * @param jsonString json to be prettified
   * @return a prettified version of the json string
   */
  public static String prettifyJson(String jsonString) {
    JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    return gson.toJson(json);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((data == null) ? 0 : data.hashCode());
    result = (prime * result) + ((dataClass == null) ? 0 : dataClass.hashCode());
    return result;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Message)) {
      return false;
    }
    Message<?> other = (Message<?>) obj;
    if (data == null) {
      if (other.data != null) {
        return false;
      }
    } else if (!data.equals(other.data)) {
      return false;
    }
    if (dataClass == null) {
      if (other.dataClass != null) {
        return false;
      }
    } else if (!dataClass.equals(other.dataClass)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder
        .append("Message [dataClass=")
        .append(dataClass)
        .append(", data=")
        .append(data)
        .append("]");
    return builder.toString();
  }
}
