package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameState;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A GameLobby manages a game between two players on the server side. It is responsible for
 * validating moves received from the clients.
 *
 * @author Leonard Ganz
 */
public class GameLobby {

  private ClientConnection connOne;
  private ClientConnection connTwo;
  private int lobbyID;
  private Model masterGame;
  private Server server;

  /**
   * Create a new lobby for two connected players.
   *
   * @param connOne connection to player one
   * @param connTwo connection to player two
   * @param lobbyID integer used to identify the game lobby
   */
  public GameLobby(ClientConnection connOne, ClientConnection connTwo, int lobbyID, Server server) {
    this.connOne = connOne;
    this.connTwo = connTwo;
    this.lobbyID = lobbyID;
    this.server = server;

    connOne.setLobby(this);
    connTwo.setLobby(this);
  }

  /**
   * Get the id of the game lobby.
   *
   * @return an integer id
   */
  public int getLobbyID() {
    return lobbyID;
  }

  void tryGameUpdate(GameState newState) {}

  /**
   * Called to signal that a client has left the lobby. If leaving client was actively participating
   * in the game the lobby will be closed. If it was only spectating the game will continue.
   *
   * @param connectionID integer id used to reference the leaving client connection
   */
  public synchronized void leave(int connectionID) {
    ClientConnection partner;
    if (connOne.getConnectionID() == connectionID) {
      partner = connTwo;
    } else if (connTwo.getConnectionID() == connectionID) {
      partner = connOne;
    } else {
      partner = null;
      //try to remove spectator
    }

    if (partner != null) {
      partner.sendNotification(ServerNotification.PARTNER_LEFT);
      closeLobby();
    }


  }

  private void closeLobby() {
    Stream.of(connOne, connTwo) // add spectators
        .filter(Objects::nonNull)
        .forEach(conn -> conn.sendNotification(ServerNotification.LOBBY_CLOSED));
    server.lobbyClosed(lobbyID);
  }
}
