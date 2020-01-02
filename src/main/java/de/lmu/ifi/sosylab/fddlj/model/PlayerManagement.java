package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

/**
 * PlayerManagement is used to store data about the {@link Player players} currently involved in a
 * game.
 *
 * @author Leonard Ganz
 */
public interface PlayerManagement {

  /**
   * Get the first {@link Player} playing the game.
   *
   * @return a reference to player one
   */
  Player getPlayerOne();

  /**
   * Get the second {@link Player} playing the game.
   *
   * @return a reference to player two
   */
  Player getPlayerTwo();

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
   * Returns a deep copy of the {@link PlayerManagement} this method is called for.
   *
   * @return a deep copy of the player management
   */
  PlayerManagement makeCopy();
}

