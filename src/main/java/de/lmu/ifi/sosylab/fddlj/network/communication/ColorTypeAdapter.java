package de.lmu.ifi.sosylab.fddlj.network.communication;

import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;

import javafx.scene.paint.Color;

/**
 * Custom Gson serializer and deserializer for Java FX {@link Color}s.
 *
 * @author Leonard
 *
 */
public class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
  private static final Object INSTANCE = new ColorTypeAdapter();

  private ColorTypeAdapter() {
  }

  /**
   * Get deserializer instance for the {@link Color} class. Add this as a
   * {@link TypeAdapter} to your {@link GsonBuilder}:
   *
   * <pre>
   * {@code
   * GsonBuilder gsonBuilder = new GsonBuilder;
   * gsonBuilder.
   *     .registerTypeAdapter(Color.class,
   *     ColorTypeAdapter.getContentJsonDeserializer());
   * Gson gson = gsonBuilder.create();
   * }
   * </pre>
   *
   * @return a (de)serializer for colors that implements {@link JsonSerializer}
   *         and {@link JsonDeserializer}
   */
  public static Object getAdapter() {
    return INSTANCE;
  }

  private static final String RED = "red";
  private static final String GREEN = "green";
  private static final String BLUE = "blue";
  private static final String OPACITY = "opacity";

  @Override
  public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonColor = json.getAsJsonObject();
    double red = jsonColor.get(RED).getAsDouble();
    double green = jsonColor.get(GREEN).getAsDouble();
    double blue = jsonColor.get(BLUE).getAsDouble();
    double opacity = jsonColor.get(OPACITY).getAsDouble();
    return new Color(red, green, blue, opacity);
  }

  @Override
  public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonColor = new JsonObject();
    jsonColor.addProperty(RED, src.getRed());
    jsonColor.addProperty(GREEN, src.getGreen());
    jsonColor.addProperty(BLUE, src.getBlue());
    jsonColor.addProperty(OPACITY, src.getOpacity());
    return context.serialize(jsonColor);
  }

}
