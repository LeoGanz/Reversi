package de.lmu.ifi.sosylab.fddlj.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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

  private GameFieldImpl createTestGameField() {
    GameFieldImpl field = new GameFieldImpl();
    Cell cell1 = new CellImpl(4, 2);
    Cell cell2 = new CellImpl(4, 3);
    Cell cell3 = new CellImpl(4, 4);
    Cell cell4 = new CellImpl(3, 4);
    Cell cell5 = new CellImpl(3, 3);
    field.set(cell1, PlayerManagement.playerOne);
    field.set(cell2, PlayerManagement.playerOne);
    field.set(cell3, PlayerManagement.playerOne);
    field.set(cell4, PlayerManagement.playerOne);
    field.set(cell5, PlayerManagement.playerTwo);
    return field;
  }

  @Test
  public void testIsCellOfPlayer_PlayerOneTrue() {
    helperIsCellOfPlayer_True(4, 2, Player.playerOne);
  }

  @Test
  public void testIsCellOfPlayer_PlayerTwoTrue() {
    helperIsCellOfPlayer_True(3, 3, Player.playerTwo);
  }

  @Test
  public void testIsCellOfPlayer_PlayerOneFalse() {
    helperIsCellOfPlayer_False(3, 3, Player.playerOne);
  }

  @Test
  public void testIsCellOfPlayer_PlayerTwoFalse() {
    helperIsCellOfPlayer_False(4, 4, Player.PlayerTwo);
  }

  @Test
  public void testIsCellOfPlayer_noPlayer() {
    helperIsCellOfPlayer_False(4, 2, Player.playerOne);
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
    Assertions.assertEquals(test, Disk.playerOne);
  }

  @Test
  public void testGet_playerTwo() {
    Cell cell = new CellImpl(3, 3);
    Optional<Disk> test = createTestGameField().get(cell);
    Assertions.assertEquals(test, Disk.playerTwo);
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
    Disk test = createTestGameField().remove(cell);
    Assertions.assertNull(test);
  }

  @Test
  public void testRemove_playerTwo() {
    Cell cell = new CellImpl(3, 3);
    Disk test = createTestGameField().remove(cell);
    Assertions.assertNull(test);
  }

  @Test
  public void testRemove_Empty() {
    Cell cell = new CellImpl(1, 1);
    Disk test = createTestGameField().remove(cell);
    Assertions.assertNull(test);
  }

  private void helperTestSet(Disk newValue, int column, int row) {
    Cell cell = new CellImpl(column, row);
    createTestGameField().set(cell, newValue);
  }
}
