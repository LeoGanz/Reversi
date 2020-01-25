package de.lmu.ifi.sosylab.fddlj.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CellImpl}.
 *
 * @author Leonard Ganz
 */
public class CellImplTest {

  @Test
  public void testGetColumn_Regular() {
    testColumnHelper(5);
  }

  @Test
  public void testGetColumn_Zero() {
    testColumnHelper(0);
  }

  @Test
  public void testGetColumn_Negative() {
    testColumnHelper(-5);
  }

  @Test
  public void testGetColumn_MaxInt() {
    testColumnHelper(Integer.MAX_VALUE);
  }

  @Test
  public void testGetColumn_MinInt() {
    testColumnHelper(Integer.MIN_VALUE);
  }

  @Test
  public void testGetRow_Regular() {
    testRowHelper(5);
  }

  @Test
  public void testGetRow_Zero() {
    testRowHelper(0);
  }

  @Test
  public void testGetRow_Negative() {
    testRowHelper(-5);
  }

  @Test
  public void testGetRow_MaxInt() {
    testRowHelper(Integer.MAX_VALUE);
  }

  @Test
  public void testGetRow_MinInt() {
    testRowHelper(Integer.MIN_VALUE);
  }

  private void testColumnHelper(int column) {
    int row = 8;
    Cell cell = new CellImpl(column, row);
    int actual = cell.getColumn();
    Assertions.assertEquals(
        column,
        cell.getColumn(),
        "GetColumn() returned " + actual + " instead of expected " + column);
  }

  private void testRowHelper(int row) {
    int column = 8;
    Cell cell = new CellImpl(column, row);
    int actual = cell.getRow();
    Assertions.assertEquals(
        row, cell.getRow(), "GetRow() returned " + actual + " instead of expected " + row);
  }

  @Test
  public void testCompareTo_List() {
    final int negative = -12;
    final int zero = 0;
    final int positive = 5;
    List<Cell> expected = new ArrayList<>();
    expected.add(new CellImpl(Integer.MIN_VALUE, Integer.MIN_VALUE));
    expected.add(new CellImpl(Integer.MIN_VALUE, negative));
    expected.add(new CellImpl(Integer.MIN_VALUE, zero));
    expected.add(new CellImpl(Integer.MIN_VALUE, positive));
    expected.add(new CellImpl(Integer.MIN_VALUE, Integer.MAX_VALUE));
    expected.add(new CellImpl(negative, Integer.MIN_VALUE));
    expected.add(new CellImpl(negative, negative));
    expected.add(new CellImpl(negative, zero));
    expected.add(new CellImpl(negative, positive));
    expected.add(new CellImpl(negative, Integer.MAX_VALUE));
    expected.add(new CellImpl(zero, Integer.MIN_VALUE));
    expected.add(new CellImpl(zero, negative));
    expected.add(new CellImpl(zero, zero));
    expected.add(new CellImpl(zero, positive));
    expected.add(new CellImpl(zero, Integer.MAX_VALUE));
    expected.add(new CellImpl(positive, Integer.MIN_VALUE));
    expected.add(new CellImpl(positive, negative));
    expected.add(new CellImpl(positive, zero));
    expected.add(new CellImpl(positive, positive));
    expected.add(new CellImpl(positive, Integer.MAX_VALUE));
    expected.add(new CellImpl(Integer.MAX_VALUE, Integer.MIN_VALUE));
    expected.add(new CellImpl(Integer.MAX_VALUE, negative));
    expected.add(new CellImpl(Integer.MAX_VALUE, zero));
    expected.add(new CellImpl(Integer.MAX_VALUE, positive));
    expected.add(new CellImpl(Integer.MAX_VALUE, Integer.MAX_VALUE));

    ArrayList<Cell> sorted = new ArrayList<>(expected);
    Collections.sort(sorted);
    Assertions.assertIterableEquals(expected, sorted, "Cells were not ordered correctly");
  }

  @Test
  public void testCompareTo_Signum() {
    Cell x = new CellImpl(5, 12);
    Cell y = new CellImpl(12, 5);
    Assertions.assertEquals(
        Math.signum(x.compareTo(y)),
        -Math.signum(y.compareTo(x)),
        "x.compareTo(y) should have opposite signum of y.compareTo(x)");
  }

  @Test
  public void testCompareTo_Transitivity() {
    Cell x = new CellImpl(5, 12);
    Cell y = new CellImpl(5, 5);
    Cell z = new CellImpl(3, 5);
    Assertions.assertTrue(x.compareTo(y) > 0, "x should be before y");
    Assertions.assertTrue(y.compareTo(z) > 0, "y should be before z");
    Assertions.assertTrue(x.compareTo(z) > 0, "x should be before z, because of transitivity");
  }

  @Test
  public void testCompareTo_EqualCompareForEqualCells() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(5, 2 + 3);
    Cell z = new CellImpl(12, 5);
    Assertions.assertTrue(x.compareTo(y) == 0, "x should be equal y");
    Assertions.assertEquals(
        Math.signum(x.compareTo(z)),
        Math.signum(y.compareTo(z)),
        "x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z))");
  }

  @Test
  public void testCompareTo_Equals() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(5, 2 + 3);
    Assertions.assertTrue(x.compareTo(y) == 0, "x should be equal y");
    Assertions.assertTrue(x.equals(y), "x should be equal y if x.compareTo(y) == 0");
  }

  @Test
  public void testEquals_Equal() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(5, 2 + 3);
    Assertions.assertTrue(x.equals(y), x + " should be equal to " + y);
  }

  @Test
  public void testEquals_SameObject() {
    Cell cell = new CellImpl(1, 1);
    Assertions.assertTrue(cell.equals(cell));
  }

  @Test
  public void testEquals_Unequal() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(-5, 2 + 3);
    Assertions.assertFalse(x.equals(y), x + " should not be equal to " + y);
  }

  @Test
  public void testEquals_DifferentClass() {
    Cell cell = new CellImpl(1, 1);
    Assertions.assertFalse(cell.equals(new Object()));
  }

  @Test
  public void testHashCode_Equal() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(5, 2 + 3);
    Assertions.assertTrue(x.equals(y), x + " should be equal to " + y);
    Assertions.assertEquals(x.hashCode(), y.hashCode(), "HashCode should be equal for equal cells");
  }

  @Test
  public void testHashCode_Unequal() {
    Cell x = new CellImpl(5, 5);
    Cell y = new CellImpl(-5, 2 + 3);
    Assertions.assertFalse(x.equals(y), x + " should not be equal to " + y);
    Assertions.assertNotEquals(
        x.hashCode(), y.hashCode(), "HashCode should not be equal for unequal cells");
  }

  @Test
  public void testToString_Regular() {
    Assertions.assertEquals(
        "(5|-12)", new CellImpl(5, -12).toString(), "ToString did not display cell correctly");
  }

  @Test
  public void testToString_EdgeCase() {
    Assertions.assertEquals(
        "(" + Integer.MIN_VALUE + "|" + Integer.MAX_VALUE + ")",
        new CellImpl(Integer.MIN_VALUE, Integer.MAX_VALUE).toString(),
        "ToString did not display cell correctly");
  }
}
