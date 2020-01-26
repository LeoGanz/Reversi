package de.lmu.ifi.sosylab.fddlj.network.communication;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import java.lang.reflect.Type;

/**
 * Gson serializer and deserializer for interface types. This enables types using fields that are
 * specified as interfaces to be serialized if they have stored a concrete implementation of that
 * class in the field.
 *
 * @author Leonard Ganz
 * @param <T> type for which the serializer is being registered.
 */
public class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
  private static final Object INSTANCE = new InterfaceAdapter<>();

  private InterfaceAdapter() {
  }

  /**
   * Get (de)serializer instance for some interaface type class. Add this as a {@link TypeAdapter}
   * to your {@link GsonBuilder}:
   *
   * <pre>{@code
   * GsonBuilder gsonBuilder = new GsonBuilder;
   * gsonBuilder.
   *     .registerTypeAdapter(SomeInterface.class,
   *     InterfaceAdapter.getAdapter());
   * Gson gson = gsonBuilder.create();
   * }</pre>
   *
   * @return a (de)serializer for interface types that implements {@link JsonSerializer} and {@link
   *     JsonDeserializer}
   */
  public static Object getAdapter() {
    return INSTANCE;
  }

  private static final String CLASSNAME = "CLASSNAME";
  private static final String DATA = "DATA";

  @Override
  public T deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {

    JsonObject jsonObject = jsonElement.getAsJsonObject();
    String className = jsonObject.getAsJsonPrimitive(CLASSNAME).getAsString();
    Class<?> klass = getObjectClass(className);
    return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
  }

  @Override
  public JsonElement serialize(
      T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
    jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
    return jsonObject;
  }

  /** Helper method to get actual class for class name. */
  private Class<?> getObjectClass(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e.getMessage());
    }
  }
}
