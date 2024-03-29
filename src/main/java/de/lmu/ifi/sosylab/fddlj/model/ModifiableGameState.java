package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A {@link GameState} whose attributes can be modified with setters. A ModifiableGameState can only
 * be used inside of its containing package to ensure it cannot be manipulated outside of the
 * classes that make up the game's model.
 *
 * @author Leonard Ganz
 */
interface ModifiableGameState extends GameState {

  /**
   * Overwrites the current {@link Phase phase} of the game with the new one.
   *
   * @param phase the new phase
   */
  void setCurrentPhase(Phase phase);

  /**
   * Set the given ModifiableGameField as the game's game field.
   *
   * @param gameField the game field to set
   */
  void setGameField(ModifiableGameField gameField);

  /**
   * Set the given ModifiablePlayerManagement as the game's new data manager for information about
   * the involved players.
   *
   * @param playerManagement the new player management
   */
  void setPlayerManagement(ModifiablePlayerManagement playerManagement);

  @Override
  ModifiableGameField getField();

  @Override
  ModifiablePlayerManagement getPlayerManagement();

  @Override
  ModifiableGameState makeCopy();
}
