package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.beans.PropertyChangeListener;
import javafx.stage.Stage;

/**
 * The main controller interface of the chess game. It takes the actions from the user and handles
 * them accordingly. This is by either invoking the necessary model-methods, or by directly telling
 * the view to change its graphical user-interface.
 */
public interface Controller extends PropertyChangeListener {

  /**
   * Initializes and starts the user interface.
   *
   * @param gameMode the game mode to use for the game
   */
  void startMainView(GameMode gameMode, Stage stage, Player playerOne, Player playerTwo);

  /**
   * Resets the game to the specified game mode with the two given players.
   *
   * @param gameMode the new game mode
   * @param playerOne the new player one
   * @param playerTwo the new player two
   */
  void resetGame(GameMode gameMode, Player playerOne, Player playerTwo);

  /**
   * Execute a disk placement on the board.
   *
   * @param on The {@link Cell target cell}.
   * @return <code>true</code> if the move was executed successfully; <code>false</code> otherwise.
   */
  boolean placeDisk(Cell on);

  /**
   * Returns the game's current game mode.
   *
   * @return the game's current game mode
   */
  GameMode getCurrentGameMode();
}
