package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;

/**
 * A concrete implementation of {@link Cell}. In addition to the methods specified by the interface
 * CellImpl provides hashCode, equals and toString methods.
 *
 * @author Leonard Ganz
 */
public class CellImpl implements Cell {

  private int column;
  private int row;

  /**
   * Create a new cell at the given coordinates.
   *
   * @param column index of the cell's column
   * @param row index of the cell's row
   */
  public CellImpl(int column, int row) {
    this.column = column;
    this.row = row;
  }

  @Override
  public int getColumn() {
    return column;
  }

  @Override
  public int getRow() {
    return row;
  }

  @Override
  public int compareTo(Cell other) {
    int colDiff = column - other.getColumn();
    if (colDiff == 0) {
      return row - other.getRow();
    }
    return colDiff;
  }

  @Override
  public int hashCode() {
    return Objects.hash(column, row);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Cell)) {
      return false;
    }

    Cell other = (Cell) obj;
    return Objects.equals(column, other.getColumn()) && Objects.equals(row, other.getRow());
  }

  @Override
  public String toString() {
    return "(" + column + "|" + row + ")";
  }
}
