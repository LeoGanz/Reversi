package de.lmu.ifi.sosylab.fddlj.model;

import java.io.Serializable;
import java.util.Optional;

/**
 * Implementation of a data structure class that contains all necessary attributes in order to
 * successfully play a game.
 *
 * @author Josef Feger, Leonard Ganz, Dora Proteanu
 */
public interface GameState extends Serializable {

  /**
   * Return the current {@link Phase phase} of the game.
   *
   * @return the current phase
   */
  Phase getCurrentPhase();

  /**
   * Returns the {@link GameField game field} that stores the data for each cell of the game field.
   *
   * @return the current game field
   */
  GameField getField();

  /**
   * Return the player that is currently allowed to make a move.
   *
   * @return the current player
   */
  Player getCurrentPlayer();

  /**
   * Return the winner of the current game. This method may only be called if the current game is
   * finished.
   *
   * @return {@link Optional#empty()} if the game is a draw. Otherwise an optional that contains the
   *     winner
   */
  Optional<Player> getWinner();

  /**
   * Returns a deep copy of the {@link GameState} this method is called for.
   *
   * @return a deep copy of the game state
   */
  GameState makeCopy();
}
