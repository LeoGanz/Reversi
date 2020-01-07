package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.beans.PropertyChangeListener;
import javafx.stage.Stage;

public interface Controller extends PropertyChangeListener {

  /**
   * Initializes and starts the user interface.
   *
   * @param gameMode the game mode to use for the game
   */
  void startMainView(GameMode gameMode, Stage stage, Player playerOne, Player playerTwo);

  /** Reset a game such that the game is in its initial state afterwards. */
  void resetGame(GameMode gameMode);

  /**
   * Execute a disk placement on the board.
   *
   * @param on The {@link Cell target cell}.
   * @return <code>true</code> if the move was executed successfully; <code>false</code> otherwise.
   */
  boolean placeDisk(Cell on);
}
