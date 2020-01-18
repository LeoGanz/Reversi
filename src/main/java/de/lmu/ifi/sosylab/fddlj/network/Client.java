package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.Model;

/**
 * Represents a connected user to a server. The client is able to connect
 * to a server and communicate with it. The client listens to ProbertyChangeEvents,
 * so the actual {@link Model} needs to be provided.
 */
public interface Client {

  /**
   * Starts a connection to the server.
   */
  void startClient();

  /**
   * Closes the connection to the server.
   */
  void terminate();

  /**
   * Joins any random, public available lobby.
   */
  void joinAnyRandomPublicLobby();

  /**
   * Joins a specific lobby by its id.
   *
   * @param asSpectator Decide to join as spectator or not
   * @param lobbyId ID of the lobby to join
   */
  void joinSpecificLobby(boolean asSpectator, int lobbyId);

  /**
   * Create a new private lobby, where other player can join by the lobby id.
   */
  void createNewPrivateLobby();

  /**
   * Send a request to the connected server to place a disk.
   *
   * @param disk
   * @param cell
   */
  void placeDisk(Disk disk, Cell cell);

  /**
   * Request a restart of the game. The Server will ask the opponent player, whether he will
   * accept a restart or not.
   */
  void requestGameRestart();


  /**
   * Accept a restart of the game, after the opponent player asked for a restart.
   */
  void acceptGameRestart();

}
