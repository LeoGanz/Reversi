package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

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
   * Set the player that may make the next move.
   *
   * @param newPlayer the player that may make his move now.
   */
  void setCurrentPlayer(Player newPlayer);

  /**
   * Set a winner to the current game. If a game ends in a draw, then no winner should be set here.
   *
   * @param winner An {@link Optional} containing either the winning {@link Player}, or no player if
   *     the game ends in a draw.
   */
  void setWinner(Optional<Player> winner);

  /**
   * Set the given {@link GameField game field} as the game's game field.
   *
   * @param gameField the game field to set
   */
  void setGameField(GameField gameField);
}
