package de.lmu.ifi.sosylab.fddlj.network;

import java.util.Map;

/**
 * This interface defines callback methods for a type that wants to be notified about changes
 * occurring in a server.
 *
 * @author Leonard Ganz
 */
public interface ServerListener {

  /** Called when the server is shutting down. */
  void serverShuttingDown();

  /**
   * Called when changes to the specified lobby occurred on the server.
   *
   * @param lobbyID identifier of the changed lobby
   * @param lobbyRepresentation representation of the lobby containing public information, <code>
   *     null</code> if lobby was removed/closed
   */
  void lobbyUpdated(int lobbyID, LobbyRepresentation lobbyRepresentation);

  /**
   * Called when a server wants to publish all current lobbies.
   *
   * @param lobbies collection of lobby representations that only contain public information about
   *     the lobbies
   */
  void allLobbies(Map<Integer, LobbyRepresentation> lobbies);
}
