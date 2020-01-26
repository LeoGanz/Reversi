package de.lmu.ifi.sosylab.fddlj.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.lmu.ifi.sosylab.fddlj.network.communication.ColorTypeAdapter;
import de.lmu.ifi.sosylab.fddlj.network.communication.InterfaceAdapter;
import de.lmu.ifi.sosylab.fddlj.network.communication.Message;
import de.lmu.ifi.sosylab.fddlj.network.communication.MessageJsonDeserializer;
import javafx.scene.paint.Color;

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
      INSTANCE =
          new GsonBuilder()
              .registerTypeAdapter(Message.class, MessageJsonDeserializer.getDeserializer())
              .registerTypeAdapter(Player.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(GameState.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(GameState.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(ModifiableGameState.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(Cell.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(Disk.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(GameField.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(ModifiableGameField.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(PlayerManagement.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(ModifiablePlayerManagement.class, InterfaceAdapter.getAdapter())
              .registerTypeAdapter(Color.class, ColorTypeAdapter.getAdapter())
              .create();
    }
    return INSTANCE;
  }
}
