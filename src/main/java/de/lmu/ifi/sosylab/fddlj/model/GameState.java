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
   * Overwrites the current {@link Phase phase} of the game with the new one.
   *
   * @param phase the new phase
   */
  void setCurrentPhase(Phase phase);

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
   * Set the player that may make the next move.
   *
   * @param newPlayer the player that may make his move now.
   */
  void setCurrentPlayer(Player newPlayer);

  /**
   * Return the winner of the current game. This method may only be called if the current game is
   * finished.
   *
   * @return {@link Optional#empty()} if the game is a draw. Otherwise an optional that contains the
   *     winner
   */
  Optional<Player> getWinner();

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

  /**
   * Returns a deep copy of the specified {@link GameState game state}.
   * 
   * @return a deep copy of the given game state
   */
  GameState makeCopy(GameState gameState);



}
