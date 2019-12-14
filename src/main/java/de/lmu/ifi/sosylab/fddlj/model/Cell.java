package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A Cell represents a single square on a rectangular game board. It can be identified by its column
 * and row number.
 *
 * @author Leonard Ganz, Josef Feger
 */
public interface Cell extends Comparable<Cell> {

  /**
   * Get the column of this cell as integer index, starting from 0.
   *
   * @return the column of this cell, as integer value
   */
  int getColumn();

  /**
   * Get the row of this cell as integer index, starting from 0.
   *
   * @return the row of this cell, as integer value
   */
  int getRow();
}
