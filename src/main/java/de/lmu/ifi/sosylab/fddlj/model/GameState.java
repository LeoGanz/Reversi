package de.lmu.ifi.sosylab.fddlj.model;

/**
 * Implementation of a data structure class that contains all necessary attributes in order to
 * successfully play a game.
 *
 * @author Josef Feger, Leonard Ganz, Dora Proteanu
 */
public interface GameState {

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
   * Returns the {@link PlayerManagement} that stores information about the players that are
   * involved in the current game.
   *
   * @return the current player management
   */
  PlayerManagement getPlayerManagement();

  /**
   * Returns a deep copy of the {@link GameState} this method is called for.
   *
   * @return a deep copy of the game state
   */
  GameState makeCopy();
}
