package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import javafx.stage.Stage;

/**
 * The main controller interface of the chess game. It takes the actions from the user and handles
 * them accordingly. This is by either invoking the necessary model-methods, or by directly telling
 * the view to change its graphical user-interface.
 */
public interface Controller {

  /**
   * Initializes and starts the user interface.
   *
   * @param gameMode the game mode to use for the game
   * @param stage the stage to use for the GUI
   * @param playerOne the first player
   * @param playerTwo the second player
   * @param gameFieldSize the size of the game field
   */
  void startMainView(
      GameMode gameMode, Stage stage, Player playerOne, Player playerTwo, int gameFieldSize);

  /**
   * Resets the game to the specified game mode with the two given players.
   *
   * @param gameMode the new game mode
   * @param playerOne the new player one
   * @param playerTwo the new player two
   * @param gameFieldSize the size of the game field
   */
  void resetGame(GameMode gameMode, Player playerOne, Player playerTwo, int gameFieldSize);

  /**
   * Execute a disk placement on the board.
   *
   * @param on The {@link Cell target cell}.
   */
  void placeDisk(Cell on);

  /**
   * Returns the game's current game mode.
   *
   * @return the game's current game mode
   */
  GameMode getCurrentGameMode();

  /** Starts a server instance for multiplayer games. */
  void startServer();
}
