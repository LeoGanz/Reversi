package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The class GameFieldImpl implements GameField and ModifiableGameField. GameFieldImpl represents
 * the game field and stores the information about which disks are on the Cells of the game field.
 * Provides the equals and hashCode methods.
 *
 * @author Dora Pruteanu
 */
public class GameFieldImpl implements ModifiableGameField {

  private static final int STANDARD_SIZE = 8;
  private final int size;

  private Disk[][] field;

  /**
   * The constructor GameFieldImpl sets the game field to its initial state with the size {@value
   * #STANDARD_SIZE}.
   */
  public GameFieldImpl() {
    size = STANDARD_SIZE;
    field = new Disk[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        set(new CellImpl(i, j), null);
      }
    }
  }

  /**
   * The constructor GameFieldImpl sets the game field to its initial state with a given size.
   *
   * @param size of the GameFieldImpl which will be created. size must be divisible by 2 and at
   *     least be 4.
   */
  public GameFieldImpl(int size) {
    if (((size % 2) != 0) || size < 4) {
      throw new IllegalArgumentException("Size must be divisible by 2 and be at least 4");
    }
    this.size = size;
    field = new Disk[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        set(new CellImpl(i, j), null);
      }
    }
  }

  @Override
  public int getSize() {
    return size;
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
    for (int column = 0; column < size; column++) {
      for (int row = 0; row < size; row++) {
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
    for (int column = 0; column < size; column++) {
      for (int row = 0; row < size; row++) {
        if ((field[column][row] != null) && field[column][row].getPlayer().equals(player)) {
          set.add(new CellImpl(column, row));
        }
      }
    }
    return set;
  }

  @Override
  public boolean isCellOfPlayer(Player player, Cell cell) {
    if (field[cell.getColumn()][cell.getRow()] != null) {
      return field[cell.getColumn()][cell.getRow()].getPlayer().equals(player);
    } else {
      return false;
    }
  }

  @Override
  public boolean isWithinBounds(Cell cell) {
    return (cell.getColumn() >= 0)
        && (cell.getColumn() < size)
        && (cell.getRow() >= 0)
        && (cell.getRow() < size);
  }

  @Override
  public ModifiableGameField makeCopy() {
    GameFieldImpl newField = new GameFieldImpl(size);
    for (int column = 0; column < size; column++) {
      for (int row = 0; row < size; row++) {
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof GameFieldImpl)) {
      return false;
    }

    GameFieldImpl other = (GameFieldImpl) obj;
    if (other.size != size) {
      return false;
    }
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Cell toCheck = new CellImpl(i, j);
        if (!(get(toCheck).equals(other.get(toCheck)))) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(field);
  }
}
