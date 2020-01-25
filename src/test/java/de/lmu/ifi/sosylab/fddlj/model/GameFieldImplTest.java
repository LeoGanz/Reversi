package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameFieldImplTest {

  @Test
  public void testIsWithinBounds_Cell00() {
    helperIsWithinBounds_correctCell(0, 0);
  }

  @Test
  public void testIsWithinBounds_Cell07() {
    helperIsWithinBounds_correctCell(0, 7);
  }

  @Test
  public void testIsWithinBounds_Cell15() {
    helperIsWithinBounds_correctCell(1, 5);
  }

  @Test
  public void testIsWithinBounds_Cell33() {
    helperIsWithinBounds_correctCell(3, 3);
  }

  @Test
  public void testIsWithinBounds_Cell76() {
    helperIsWithinBounds_correctCell(7, 6);
  }

  @Test
  public void testIsWithinBounds_Cell77() {
    helperIsWithinBounds_correctCell(7, 7);
  }

  @Test
  public void testIsWithinBounds_Cell50() {
    helperIsWithinBounds_correctCell(5, 0);
  }

  @Test
  public void testIsWithinBounds_Cell46() {
    helperIsWithinBounds_correctCell(4, 6);
  }

  @Test
  public void testIsWithinBounds_Cell53() {
    helperIsWithinBounds_correctCell(5, 3);
  }

  @Test
  public void testIsWithinBounds_Cell70() {
    helperIsWithinBounds_correctCell(7, 0);
  }

  @Test
  public void testIstWithinBounds_incorrectCell08() {
    helperIsWithinBounds_incorrectCell(0, 8);
  }

  @Test
  public void testIstWithinBounds_incorrectCell80() {
    helperIsWithinBounds_incorrectCell(8, 0);
  }

  @Test
  public void testIstWithinBounds_incorrectCell107() {
    helperIsWithinBounds_incorrectCell(10, 7);
  }

  @Test
  public void testIstWithinBounds_incorrectCell99() {
    helperIsWithinBounds_incorrectCell(9, 9);
  }

  @Test
  public void testIstWithinBounds_incorrectCellNeg62() {
    helperIsWithinBounds_incorrectCell(-6, 2);
  }

  @Test
  public void testIstWithinBounds_incorrectCell0Neg1() {
    helperIsWithinBounds_incorrectCell(0, -1);
  }

  @Test
  public void testIstWithinBounds_incorrectCellNeg5neg6() {
    helperIsWithinBounds_incorrectCell(-5, -6);
  }

  @Test
  public void testIstWithinBounds_incorrectCellNeg10() {
    helperIsWithinBounds_incorrectCell(-1, 0);
  }

  private void helperIsWithinBounds_correctCell(int column, int row) {
    GameFieldImpl field = new GameFieldImpl();
    Cell cell = new CellImpl(column, row);

    boolean test = field.isWithinBounds(cell);
    Assertions.assertTrue(test);
  }

  private void helperIsWithinBounds_incorrectCell(int column, int row) {
    GameFieldImpl field = new GameFieldImpl();
    Cell cell = new CellImpl(column, row);

    boolean test = field.isWithinBounds(cell);
    Assertions.assertFalse(test);
  }

  private PlayerManagementImpl manager =
      new PlayerManagementImpl(
          new PlayerImpl("One", Color.ALICEBLUE), new PlayerImpl("two", Color.ANTIQUEWHITE));

  private GameFieldImpl createTestGameField() {
    GameFieldImpl field = new GameFieldImpl();
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Cell cell4 = new CellImpl(3, 4);
    Cell cell5 = new CellImpl(3, 3);
    field.set(cell1, new DiskImpl(manager.getPlayerOne()));
    field.set(cell2, new DiskImpl(manager.getPlayerOne()));
    field.set(cell3, new DiskImpl(manager.getPlayerOne()));
    field.set(cell4, new DiskImpl(manager.getPlayerOne()));
    field.set(cell5, new DiskImpl(manager.getPlayerTwo()));
    return field;
  }

  @Test
  public void testIsCellOfPlayer_PlayerOneTrue() {
    helperIsCellOfPlayer_True(4, 2, manager.getPlayerOne());
  }

  @Test
  public void testIsCellOfPlayer_PlayerTwoTrue() {
    helperIsCellOfPlayer_True(3, 3, manager.getPlayerTwo());
  }

  @Test
  public void testIsCellOfPlayer_PlayerOneFalse() {
    helperIsCellOfPlayer_False(3, 3, manager.getPlayerOne());
  }

  @Test
  public void testIsCellOfPlayer_PlayerTwoFalse() {
    helperIsCellOfPlayer_False(4, 4, manager.getPlayerTwo());
  }

  @Test
  public void testIsCellOfPlayer_noPlayer() {
    helperIsCellOfPlayer_False(4, 1, manager.getPlayerOne());
  }

  private void helperIsCellOfPlayer_True(int column, int row, Player player) {
    Cell cell = new CellImpl(column, row);
    boolean test = createTestGameField().isCellOfPlayer(player, cell);
    Assertions.assertTrue(test);
  }

  private void helperIsCellOfPlayer_False(int column, int row, Player player) {
    Cell cell = new CellImpl(column, row);
    boolean test = createTestGameField().isCellOfPlayer(player, cell);
    Assertions.assertFalse(test);
  }

  @Test
  public void testGet_playerOne() {
    Cell cell = new CellImpl(4, 2);
    Optional<Disk> test = createTestGameField().get(cell);
    Assertions.assertEquals(test, Optional.of(new DiskImpl(manager.getPlayerOne())));
  }

  @Test
  public void testGet_playerTwo() {
    Cell cell = new CellImpl(3, 3);
    Optional<Disk> test = createTestGameField().get(cell);
    Assertions.assertEquals(test, Optional.of(new DiskImpl(manager.getPlayerTwo())));
  }

  @Test
  public void testGet_Empty() {
    Cell cell = new CellImpl(0, 0);
    Optional<Disk> test = createTestGameField().get(cell);
    Assertions.assertEquals(test, Optional.empty());
  }

  @Test
  public void testRemove_playerOne() {
    Cell cell = new CellImpl(4, 2);
    GameFieldImpl field = new GameFieldImpl();
    field.set(cell,new DiskImpl(manager.getPlayerOne()));
    Disk test = field.remove(cell);
    Assertions.assertEquals(new DiskImpl(manager.getPlayerOne()), test);
    Assertions.assertEquals(Optional.empty(),field.get(cell));
  }

  @Test
  public void testRemove_playerTwo() {
    Cell cell = new CellImpl(3, 3);
    GameFieldImpl field = new GameFieldImpl();
    field.set(cell,new DiskImpl(manager.getPlayerTwo()));
    Disk test = field.remove(cell);
    Assertions.assertEquals(new DiskImpl(manager.getPlayerTwo()), test);
    Assertions.assertEquals(Optional.empty(),field.get(cell));
  }

  @Test
  public void testRemove_Empty() {
    Cell cell = new CellImpl(1, 1);
    GameFieldImpl field = new GameFieldImpl();
    Disk test = field.remove(cell);
    Assertions.assertNull(test);
    Assertions.assertEquals(Optional.empty(),field.get(cell));
  }

  private void helperTestSet(Disk newValue, int column, int row) {
    CellImpl cell = new CellImpl(column, row);
    GameFieldImpl gameField = new GameFieldImpl();
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Cell cell4 = new CellImpl(3, 4);
    Cell cell5 = new CellImpl(3, 3);
    gameField.set(cell1, new DiskImpl(manager.getPlayerOne()));
    gameField.set(cell2, new DiskImpl(manager.getPlayerOne()));
    gameField.set(cell3, new DiskImpl(manager.getPlayerOne()));
    gameField.set(cell4, new DiskImpl(manager.getPlayerOne()));
    gameField.set(cell5, new DiskImpl(manager.getPlayerTwo()));
    gameField.set(cell, newValue);
    Assertions.assertEquals(
        Optional.of(newValue),
        gameField.get(cell),
        "Cell should have value: "
            + newValue
            + ", but has value: "
            + createTestGameField().get(cell));
  }

  @Test
  public void testSet_onEmptyCell00() {

    helperTestSet(new DiskImpl(manager.getPlayerOne()), 0, 0);
  }

  @Test
  public void testSet_onEmptyCell64() {
    helperTestSet(new DiskImpl(manager.getPlayerTwo()), 6, 4);
  }

  @Test
  public void testSet_onCellWithPlayerOneOne() {
    helperTestSet(new DiskImpl(manager.getPlayerOne()), 4, 2);
  }

  @Test
  public void testSet_onCellWithPlayerOneTwo() {
    helperTestSet(new DiskImpl(manager.getPlayerTwo()), 4, 4);
  }

  @Test
  public void testSet_onCellWithPlayerTwoOne() {
    helperTestSet(new DiskImpl(manager.getPlayerOne()), 3, 3);
  }

  @Test
  public void testSet_onCellWithPlayerTwoTwo() {
    helperTestSet(new DiskImpl(manager.getPlayerTwo()), 3, 3);
  }

  @Test
  public void testGetAllCellsForPlayer_playerOneV1() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Cell cell4 = new CellImpl(3, 4);
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerOne());
    list.remove(cell1);
    list.remove(cell2);
    list.remove(cell3);
    list.remove(cell4);
    Assertions.assertEquals(0, list.size());
  }

  @Test
  public void testGetAllCellsForPlayer_playerOneV2() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerOne());
    list.remove(cell1);
    list.remove(cell2);
    list.remove(cell3);
    Assertions.assertEquals(1, list.size());
  }

  @Test
  public void testGetAllCellsForPlayer_playerOneV3() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell4 = new CellImpl(3, 4);
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerOne());
    list.remove(cell1);
    list.remove(cell4);
    Assertions.assertEquals(2, list.size());
  }

  @Test
  public void testGetAllCellsForPlayer_playerOneV4() {
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerOne());
    Assertions.assertEquals(4, list.size());
  }

  @Test
  public void testGetAllCellsForPlayer_playerTwoV1() {
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerTwo());
    Assertions.assertEquals(1, list.size());
  }

  @Test
  public void testGetAllCellsForPlayer_playerTwoV2() {
    Cell cell = new CellImpl(3, 3);
    Set<Cell> list = createTestGameField().getAllCellsForPlayer(manager.getPlayerTwo());
    list.remove(cell);
    Assertions.assertEquals(0, list.size());
  }

  @Test
  public void testGetCellsOccupiedWithDisks_V1() {
    Map<Cell, Player> list = createTestGameField().getCellsOccupiedWithDisks();
    Assertions.assertEquals(5, list.size());
  }

  @Test
  public void testGetCellsOccupiedWithDisks_V2() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Cell cell4 = new CellImpl(3, 4);
    Cell cell5 = new CellImpl(3, 3);
    Map<Cell, Player> list = createTestGameField().getCellsOccupiedWithDisks();
    list.remove(cell1);
    list.remove(cell2);
    list.remove(cell3);
    list.remove(cell4);
    list.remove(cell5);
    Assertions.assertEquals(0, list.size());
  }

  @Test
  public void testGetCellsOccupiedWithDisks_V3() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Map<Cell, Player> list = createTestGameField().getCellsOccupiedWithDisks();
    list.remove(cell1);
    list.remove(cell2);
    list.remove(cell3);
    Assertions.assertEquals(2, list.size());
  }

  @Test
  public void testGetCellsOccupiedWithDisks_V4() {
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell4 = new CellImpl(3, 4);
    Cell cell5 = new CellImpl(3, 3);
    Map<Cell, Player> list = createTestGameField().getCellsOccupiedWithDisks();
    list.remove(cell1);
    list.remove(cell2);
    list.remove(cell4);
    list.remove(cell5);
    Assertions.assertEquals(1, list.size());
  }

  @Test
  public void testGetCellsOccupiedWithDisks_V5() {
    Cell cell2 = new CellImpl(4, 3);
    Map<Cell, Player> list = createTestGameField().getCellsOccupiedWithDisks();
    list.remove(cell2);
    Assertions.assertEquals(4, list.size());
  }

  @Test
  public void testMakeCopy_gameField() {
    ModifiableGameField field = createTestGameField().makeCopy();
    Assertions.assertEquals(
        createTestGameField().getCellsOccupiedWithDisks(), field.getCellsOccupiedWithDisks());
  }

  @Test
  public void testMakeCopy_testWithList() {
    ModifiableGameField field = createTestGameField().makeCopy();
    Map<Cell, Player> list = field.getCellsOccupiedWithDisks();
    Assertions.assertEquals(5, list.size(), "We expected 5 but got: " + list.size());
  }

  @Test
  public void testEquals_isEqual() {
    GameFieldImpl field1 = createTestGameField();
    GameFieldImpl field2 = createTestGameField();

    Assertions.assertEquals(field1, field2, "field1 and field2 should be equal!");
  }

  @Test
  public void testEquals_SameObject() {
    GameFieldImpl field = createTestGameField();
    
    Assertions.assertEquals(field, field);
  }

  @Test
  public void testEquals_NotEqual() {
    GameFieldImpl field1 = createTestGameField();
    GameFieldImpl field2 = new GameFieldImpl();

    Assertions.assertNotEquals(
        field1, field2, "field1 should not be equal to the empty GameField field2!");
  }

  @Test
  public void testEquals_DifferentClass() {
    GameField field = createTestGameField();
    
    Assertions.assertNotEquals(field, new Object());
  }

  @Test
  public void testEquals_DifferentFieldSize() {
    GameField field = createTestGameField();
    GameField field2 = new GameFieldImpl(10);
    
    Assertions.assertNotEquals(field, field2);
  }

  @Test
  public void testHashCode_isEqual() {
    GameFieldImpl field1 = createTestGameField();
    GameFieldImpl field2 = createTestGameField();

    Assertions.assertEquals(field1, field2);
    Assertions.assertEquals(
        field1.hashCode(),
        field2.hashCode(),
        "equal GameFieldImpl instances should have the same hashCode()!");
  }

  @Test
  public void testHashCode_NotEqual() {
    GameFieldImpl field1 = createTestGameField();
    GameFieldImpl field2 = new GameFieldImpl();

    Assertions.assertNotEquals(field1, field2);
    Assertions.assertNotEquals(
        field1.hashCode(),
        field2.hashCode(),
        "different GameFieldIml instances should have a different hashCode()!");
  }

  @Test
  public void testConstructor_WrongSizeTooSmall() {
    try {
      new GameFieldImpl(2);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof IllegalArgumentException);
      return;
    }
    Assertions.fail();
  }

  @Test
  public void testConstructor_WrongSizeNotDivisibleByTwo() {
    try {
      new GameFieldImpl(7);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof IllegalArgumentException);
      return;
    }
    Assertions.fail();
  }

  @Test
  public void testGetSize() {
    GameFieldImpl field = new GameFieldImpl(10);
    Assertions.assertEquals(10, field.getSize());
  }
}
