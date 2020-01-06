package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A {@link GameField} whose board can be modified. A ModifiableGameField can only be used inside of
 * its containing package to ensure it cannot be manipulated outside of the classes that make up the
 * game's model.
 *
 * @author Leonard Ganz
 */
interface ModifiableGameField extends GameField {

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
   * Remove {@link Disk disk} from the given {@link Cell cell}.
   *
   * @param cell cell to remove any disk from
   * @return the disk that was removed, or {@code null} if there was no disk on the cell
   * @throws IllegalArgumentException if given cell is out of field bounds
   */
  Disk remove(Cell cell);

  @Override
  ModifiableGameField makeCopy();
}
