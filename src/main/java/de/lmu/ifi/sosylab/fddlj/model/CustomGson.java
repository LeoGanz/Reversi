package de.lmu.ifi.sosylab.fddlj.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lmu.ifi.sosylab.fddlj.network.communication.InterfaceAdapter;
import de.lmu.ifi.sosylab.fddlj.network.communication.Message;
import de.lmu.ifi.sosylab.fddlj.network.communication.MessageJsonDeserializer;
import javafx.scene.paint.Color;
import org.hildan.fxgson.adapters.extras.ColorTypeAdapter;

/**
 * Provider of preconfigured {@link Gson}.
 *
 * @author Leonard Ganz
 */
public class CustomGson {
  private static Gson INSTANCE;

  /**
   * Create {@link Gson} that is configured to work with {@link Message} and the interfaces from the
   * model package needed for network communication.
   *
   * @return the configured gson
   */
  public static Gson createGson() {
    if (INSTANCE == null) {
      INSTANCE = new GsonBuilder()
          .registerTypeAdapter(Message.class, MessageJsonDeserializer.getContentJsonDeserializer())
          .registerTypeAdapter(Player.class, new InterfaceAdapter<>())
          .registerTypeAdapter(GameState.class, new InterfaceAdapter<>())
          .registerTypeAdapter(GameState.class, new InterfaceAdapter<>())
          .registerTypeAdapter(ModifiableGameState.class, new InterfaceAdapter<>())
          .registerTypeAdapter(Cell.class, new InterfaceAdapter<>())
          .registerTypeAdapter(Disk.class, new InterfaceAdapter<>())
          .registerTypeAdapter(GameField.class, new InterfaceAdapter<>())
          .registerTypeAdapter(ModifiableGameField.class, new InterfaceAdapter<>())
          .registerTypeAdapter(PlayerManagement.class, new InterfaceAdapter<>())
          .registerTypeAdapter(ModifiablePlayerManagement.class, new InterfaceAdapter<>())
          .registerTypeAdapter(Color.class, new ColorTypeAdapter())
          .create();
    }
    return INSTANCE;
  }
}
