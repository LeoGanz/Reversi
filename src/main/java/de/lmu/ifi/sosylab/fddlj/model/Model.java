package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * The main interface of the reversi model. It provides all necessary methods for accessing and
 * manipulating the data such that a game can be played successfully.
 *
 * <p>When something changes in the model, the model notifies its observers by firing a {@link
 * PropertyChangeEvent change-event}.
 *
 * @author Leonard Ganz, Josef Feger
 */
public interface Model {

  String STATE_CHANGED = "model changed state";
  String LISTENERS_CHANGED = "model changed listeners";

  /**
   * Place disk on a cell and deal with the consequences. Placing a disk only works if there is
   * currently a game running and if placing the disk on the specified cell is a valid reversi move.
   * After the placement of the disk, it will be the turn of the next player, unless the game is
   * over.
   *
   * @param disk the disk to place
   * @param cell the location on the game field on which to place the disk
   * @return {@code true} if the placement was successful, {@code false} otherwise
   */
  boolean placeDisk(Disk disk, Cell cell);

  /**
   * Computes all possible moves on the current game board for the selected player. A move is the
   * placement of a disk. Reversi rules apply.
   *
   * @param player the {@link Player player} whose moves shall be computed
   * @return a set of cells with all possible moves for the specified player
   * @throws IllegalArgumentException if player is not equal to {@link
   *     PlayerManagement#getPlayerOne() player one} or {@link PlayerManagement#getPlayerTwo()
   *     player two} which can be accessed via the {@link PlayerManagement} of the game's {@link
   *     #getState() state}
   */
  Set<Cell> getPossibleMovesForPlayer(Player player);

  /**
   * Add a {@link PropertyChangeListener} to the model that will be notified about the changes made
   * to the reversi board.
   *
   * @param pcl the class that implements the listener
   */
  void addListener(PropertyChangeListener pcl);

  /**
   * Remove a listener from the model, which will then no longer be notified about any events in the
   * model.
   *
   * @param pcl the class that then no longer receives notifications from the model
   */
  void removeListener(PropertyChangeListener pcl);

  /**
   * Return the {@link GameState} specific to this class.
   *
   * @return the {@code GameState}-object
   */
  GameState getState();

  /**
   * If the {@link Phase} is {@code Phase.WAITING} this method sets it to {@code Phase.RUNNING}.
   *
   * @return {@code true} if the operation was successful and {@code false} otherwise.
   */
  boolean unsetWaiting();

  /**
   * If the {@link Phase} is {@code Phase.RUNNING} this method sets it to {@code Phase.WAITING}.
   *
   * @return {@code true} if the operation was successful and {@code false} otherwise.
   */
  boolean setWaiting();

  /**
   * Substitute both players with new players without stopping the game.
   *
   * @param newPlayerOne new player to substitute the old player one with
   * @param newPlayerTwo new player to substitute the old player two with
   * @author Leonard Ganz
   */
  void substitutePlayersWith(Player newPlayerOne, Player newPlayerTwo);

  /** Causes the AI to calculate and do a move. */
  void triggerAiMove();
}
