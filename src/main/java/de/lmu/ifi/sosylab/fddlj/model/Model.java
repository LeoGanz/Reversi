package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * The main interface of the reversi model. It provides all necessary methods for accessing and
 * manipulating the data such that a game can be played successfully.
 *
 * <p>When something changes in the model, the model notifies its observers by firing a
 * {@link PropertyChangeEvent change-event}.
 *
 * @author Leonard Ganz, Josef Feger
 */
public interface Model {

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
   * Computes all possible moves for the selected player. A move is the placement of a disk. Reversi
   * rules apply.
   *
   * @param player the {@link Player player} whose moves shall be computed
   * @return a set of cells with all possible moves for the specified player
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
}
