package de.lmu.ifi.sosylab.fddlj.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The class GameFieldModel implements GameField and ModifiableGameField. GameFieldModel represents
 * the game field and stores the information about which disks are on the Cells of the game field.
 *
 * @author Dora Pruteanu
 */
public class GameFieldImpl implements ModifiableGameField {
  public static final int SIZE = 8;

  private Disk[][] field = new Disk[SIZE][SIZE];

  /** The constructor GameFieldImpl sets the game field to its initial state. */
  public GameFieldImpl() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        set(new CellImpl(i, j), null);
      }
    }
  }

  @Override
  public void set(Cell cell, Disk newValue) {
    throwErrorWhenOutOfBounds(cell);
    field[cell.getColumn()][cell.getRow()] = newValue;
  }

  @Override
  public Disk remove(Cell cell) {
    throwErrorWhenOutOfBounds(cell);
    Disk disk = field[cell.getColumn()][cell.getRow()];
    if (disk != null) {
      field[cell.getColumn()][cell.getRow()] = null;
      return disk;
    }
    return null;
  }

  @Override
  public Optional<Disk> get(Cell cell) {
    throwErrorWhenOutOfBounds(cell);
    Disk disk = field[cell.getColumn()][cell.getRow()];
    if (disk != null) {
      return Optional.of(disk);
    }
    return Optional.empty();
  }

  @Override
  public Map<Cell, Player> getCellsOccupiedWithDisks() {
    Map<Cell, Player> map = new HashMap<>();
    for (int column = 0; column < SIZE; column++) {
      for (int row = 0; row < SIZE; row++) {
        if (field[column][row] != null) {
          map.put(new CellImpl(column, row), field[column][row].getPlayer());
        }
      }
    }
    return map;
  }

  @Override
  public Set<Cell> getAllCellsForPlayer(Player player) {
    Set<Cell> set = new HashSet<>();
    for (int column = 0; column < SIZE; column++) {
      for (int row = 0; row < SIZE; row++) {
        if (field[column][row] != null && field[column][row].getPlayer().equals(player)) {
          set.add(new CellImpl(column, row));
        }
      }
    }
    return set;
  }

  @Override
  public boolean isCellOfPlayer(Player player, Cell cell) {
    if (field[cell.getColumn()][cell.getRow()] != null) {
      return field[cell.getColumn()][cell.getRow()].getPlayer() == player;
    } else {
      return false;
    }
  }

  @Override
  public boolean isWithinBounds(Cell cell) {
    return cell.getColumn() >= 0
        && cell.getColumn() < SIZE
        && cell.getRow() >= 0
        && cell.getRow() < SIZE;
  }

  @Override
  public GameFieldImpl makeCopy() {
    GameFieldImpl newField = new GameFieldImpl();
    for (int column = 0; column < SIZE; column++) {
      for (int row = 0; row < SIZE; row++) {
        if (field[column][row] != null) {
          newField.set(
              new CellImpl(column, row), new DiskImpl(field[column][row].getPlayer().makeCopy()));
        }
      }
    }
    return newField;
  }

  /**
   * The method throwErrorWhenOutOfBounds checks if the given cell is within the game field. If the
   * cell isn't within the game field an IllegalArgumentException is thrown.
   */
  private void throwErrorWhenOutOfBounds(Cell cell) {
    if (!isWithinBounds(cell)) {
      throw new IllegalArgumentException(
          "The coordinates of the cell: " + cell + " are out of bounds");
    }
  }
}
