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
import com.google.gson.reflect.TypeToken;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Custom Gson serializer and deserializer for {@link Spectators}.
 *
 * @author Leonard Ganz
 */
public class SpectatorsTypeAdapter
    implements JsonSerializer<Spectators>, JsonDeserializer<Spectators> {
  private static final Object INSTANCE = new SpectatorsTypeAdapter();

  private SpectatorsTypeAdapter() {
  }

  /**
   * Get (de)serializer instance for the {@link Spectators} class. Add this as a {@link TypeAdapter}
   * to your {@link GsonBuilder}:
   *
   * <pre>{@code
   * GsonBuilder gsonBuilder = new GsonBuilder;
   * gsonBuilder.
   *     .registerTypeAdapter(Spectators.class,
   *     SpectatorsTypeAdapter.getAdapter());
   * Gson gson = gsonBuilder.create();
   * }</pre>
   *
   * @return a (de)serializer for spectators that implements {@link JsonSerializer} and {@link
   *     JsonDeserializer}
   */
  public static Object getAdapter() {
    return INSTANCE;
  }

  private static final String LOBBY_ID = "lobbyId";
  private static final String PLAYERS = "players";

  @Override
  public Spectators deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonSpectators = json.getAsJsonObject();
    int lobbyId = jsonSpectators.get(LOBBY_ID).getAsInt();
    JsonElement playersElement = jsonSpectators.get(PLAYERS);
    Set<Player> players =
        context.deserialize(playersElement, new TypeToken<Set<Player>>() {}.getType());
    return new Spectators(lobbyId, players);
  }

  @Override
  public JsonElement serialize(Spectators src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonSpectators = new JsonObject();
    jsonSpectators.addProperty(LOBBY_ID, src.getLobbyId());
    jsonSpectators.add(PLAYERS, context.serialize(src, new TypeToken<Set<Player>>() {}.getType()));
    return context.serialize(jsonSpectators);
  }
}
