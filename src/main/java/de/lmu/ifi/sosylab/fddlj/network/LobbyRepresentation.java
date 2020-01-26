package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;

/**
 * Representation of a {@link GameLobby} containing only information intended for public use.
 *
 * @author Leonard Ganz
 */
public class LobbyRepresentation {
  private final int lobbyID;
  private final Player playerOne;
  private final Player playerTwo;
  private final Phase gamePhase;

  /**
   * Create a new {@link LobbyRepresentation} with the data provided.
   *
   * @param lobbyID identifier of the lobby
   * @param playerOne first player playing the game
   * @param playerTwo second player playing the game
   * @param gamePhase current {@link Phase} of the game played in this lobby
   */
  public LobbyRepresentation(int lobbyID, Player playerOne, Player playerTwo, Phase gamePhase) {
    this.lobbyID = lobbyID;
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    this.gamePhase = gamePhase;
  }

  /**
   * Get the Id of the lobby.
   *
   * @return the lobbyID
   */
  public int getLobbyID() {
    return lobbyID;
  }

  /**
   * Get {@link Player} one.
   *
   * @return the playerOne
   */
  public Player getPlayerOne() {
    return playerOne;
  }

  /**
   * Get {@link Player} two.
   *
   * @return the playerTwo
   */
  public Player getPlayerTwo() {
    return playerTwo;
  }

  /**
   * Get the current phase of the game.
   *
   * @return the gamePhase
   */
  public Phase getGamePhase() {
    return gamePhase;
  }
}
