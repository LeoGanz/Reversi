package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Player;

/**
 * The main interface used by the game for online games. It takes the actions from the user and
 * handles them accordingly. This is by either invoking the necessary model-methods, or by directly
 * telling the view to change its graphical user-interface.
 *
 * @author Josef Feger
 */
public interface MultiplayerController extends Controller {

  /**
   * Initialises a game in which the opponent player won't be on the same machine but instead
   * somewhere else in the world.
   *
   * @param ownPlayer the reference to the player instance representing the player on this machine
   * @param serverAddress the address of the server you want to connect to
   * @param lobbyID the lobbie's ID you want to join
   * @param createPrivateLobby indicates whether the player wants to join a private lobby
   */
  void startOnlineGame(
      Player ownPlayer, String serverAddress, int lobbyID, boolean createPrivateLobby);

  /** Sends a request to the opponent player to reset the current game. */
  void requestGameReset();

  /**
   * Returns a reference to the player playing on this machine.
   *
   * @return the a reference to the machine's player
   */
  Player getOwnPlayer();
}
