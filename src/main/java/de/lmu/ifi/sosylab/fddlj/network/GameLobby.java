package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameState;
import de.lmu.ifi.sosylab.fddlj.model.Model;

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

  /**
   * Create a new lobby for two connected players.
   *
   * @param connOne connection to player one
   * @param connTwo connection to player two
   * @param lobbyID integer used to identify the game lobby
   */
  public GameLobby(ClientConnection connOne, ClientConnection connTwo, int lobbyID) {
    this.connOne = connOne;
    this.connTwo = connTwo;
    this.lobbyID = lobbyID;

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
}
