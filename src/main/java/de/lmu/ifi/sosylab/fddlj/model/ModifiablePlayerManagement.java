package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

/**
 * A {@link PlayerManagement} whose data can be modified with setters. A ModifiablePlayerManagement
 * can only be used inside of its containing package to ensure it cannot be manipulated outside of
 * the classes that make up the game's model.
 *
 * @author Leonard Ganz
 */
interface ModifiablePlayerManagement extends PlayerManagement {

  /**
   * Set the first {@link Player} playing the game.
   *
   * @param playerOne a reference to player one
   * @throws IllegalArgumentException if playerOne is equal to {@link
   *     PlayerManagement#getPlayerTwo() player two}.
   */
  void setPlayerOne(Player playerOne);

  /**
   * Set the second {@link Player} playing the game.
   *
   * @param playerTwo a reference to player two
   * @throws IllegalArgumentException if playerTwo is equal to {@link
   *     PlayerManagement#getPlayerOne() player one}.
   */
  void setPlayerTwo(Player playerTwo);

  /**
   * Switch the player that may make the next move. If previously {@link
   * PlayerManagement#getPlayerOne() player one} was current player, {@link
   * PlayerManagement#getPlayerTwo() player two} will be set as the new current player and vice
   * versa.
   *
   * @throws IllegalStateException if either {@link PlayerManagement#getPlayerOne() player one} or
   *     {@link PlayerManagement#getPlayerTwo() player two} is not set
   */
  void switchCurrentPlayer();

  /**
   * Set a winner to the current game. If a game ends in a draw, {@link Optional#empty()} shall be
   * set.
   *
   * @param winner an {@link Optional} containing either the winning {@link Player}, or no player if
   *     the game ends in a draw
   * @throws IllegalArgumentException if contained player is not equal to {@link
   *     PlayerManagement#getPlayerOne() player one} or {@link PlayerManagement#getPlayerTwo()
   *     player two}
   */
  void setWinner(Optional<Player> winner);
}
