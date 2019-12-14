package de.lmu.ifi.sosylab.fddlj.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * A game field represents the game field object in which the game stores the information about
 * which disks are on the different @{link Cell cells} on the game field.
 * 
 * @author Josef Feger, Leonard Ganz
 *
 */
public interface GameField extends Serializable {

  /**
   * Return an {@link Optional} that may contain a {@link Disk}, depending on if there is one
   * positioned on the cell.
   *
   * @param cell the cell to be checked
   * @return an {@link Optional optional} containing the respective disk in case of success
   */
  Optional<Disk> get(Cell cell);

  /**
   * Returns all {@link Cell cells} that are currently occupied by a disk.
   *
   * @return a map with all cells that have a disk on them
   */
  Map<Cell, Player> getCellsOccupiedWithDisks();

  /**
   * Collect the {@link Cell cells} the specified {@link Player player} has a {@link Disk disk} on.
   * 
   * @param player the player whose disks shall be retrieved
   * @return a set with the cells the specified player has a disk on
   */
  Set<Cell> getAllCellsForPlayer(Player player);

  /**
   * Set a disk on the given {@link Cell cell}. Any {@link Disk disk} already on that cell will be
   * overridden.
   *
   * @param cell cell to set disk on
   * @param newValue new value (disk) to set on the cell
   * @throws IllegalArgumentException if given cell is out of field bounds
   */
  void set(Cell cell, Disk newValue);

  /**
   * Checks a {@link Cell cell} whether it is occupied by a disk and in case of success, whether it
   * belongs to the respective {@link Player player}.
   *
   * @param player the player to be checked
   * @param cell the cell that can contain a pawn
   * @return {@code true} if the player has a disk on the cell, {@code false} otherwise.
   */
  boolean isCellOfPlayer(Player player, Cell cell);

  /**
   * Checks a {@link Cell} if its column and row value is within the bounds. The valid range is from
   * 0 to 7 for each.
   *
   * @param cell the cell to be checked
   * @return {@link true} if within the bounds, {@link false} otherwise.
   */
  boolean isWithinBounds(Cell cell);

  /**
   * Returns a deep copy of the specified {@link GameField game field}.
   * 
   * @return a deep copy of the given game field
   */
  GameField makeCopy(GameField gameField);

}
