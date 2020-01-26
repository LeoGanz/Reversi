package de.lmu.ifi.sosylab.fddlj.network.communication;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Gson deserializer for {@link Message}. This deserializer can extract the data type from a message
 * and then apply it in order to deliver a message with the correct generic type.
 *
 * @param <T> generic type of the message data
 * @author Leonard Ganz
 */
public class MessageJsonDeserializer<T> implements JsonDeserializer<Message<T>> {

  private static final JsonDeserializer<?> INSTANCE = new MessageJsonDeserializer<>();

  private MessageJsonDeserializer() {
      
  }
  

  /**
   * Get deserializer instance for the message class. Add this as a {@link TypeAdapter} to your
   * {@link GsonBuilder}:
   *
   * <pre>{@code
   * GsonBuilder gsonBuilder = new GsonBuilder;
   * gsonBuilder.
   *     .registerTypeAdapter(Message.class,
   *     MessageJsonDeserializer.getDeserializer());
   * Gson gson = gsonBuilder.create();
   * }</pre>
   *
   * @param <T> generic type of the message data
   * @return a deserializer for messages with generic data types
   */
  @SuppressWarnings("unchecked")
  public static <T> JsonDeserializer<T> getDeserializer() {
    return (JsonDeserializer<T>) MessageJsonDeserializer.INSTANCE;
  }

  @Override
  public Message<T> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();

    if (type instanceof ParameterizedType) {
      // full type information available
      // build full message with data
      Type dataType = ((ParameterizedType) type).getActualTypeArguments()[0];
      T data = context.deserialize(jsonObject.get(Message.DATA_FIELD), dataType);
      return new Message<>(data);
    } else {
      // only raw type information available
      // only deserialize stored type information
      String dataClassString =
          jsonObject.getAsJsonPrimitive(Message.DATA_CLASS_FIELD).getAsString();
      try {
        Class<?> dataClass = Class.forName(dataClassString);
        Type dataType = TypeToken.getParameterized(Message.class, dataClass).getType();
        // as dataType now is a parameterized type full deserialization can be performed in
        // recursion
        return deserialize(json, dataType, context);
      } catch (@SuppressWarnings("unused") ClassNotFoundException e) {
        // could not find class of data
        // build Message without data but with unparsable class name as dataClass field
        try {
          Class<?> clazz = Message.class;
          Constructor<?> constructor = clazz.getDeclaredConstructor();
          constructor.setAccessible(true);
          Object cc = constructor.newInstance();
          Field dataClassField = cc.getClass().getDeclaredField(Message.DATA_CLASS_FIELD);
          dataClassField.setAccessible(true);
          dataClassField.set(cc, dataClassString);

          @SuppressWarnings("unchecked")
          Message<T> res = (Message<T>) cc;
          return res;
        } catch (@SuppressWarnings("unused")
            InstantiationException
            | IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException
            | NoSuchMethodException
            | SecurityException
            | NoSuchFieldException ex) {
          // reflection did not work, nothing left that can be done
          throw new JsonParseException(
              "Class of data not found. "
                  + "Trying to populate a message object with failure causing data did not work.");
        }
      }
    }
  }
}
