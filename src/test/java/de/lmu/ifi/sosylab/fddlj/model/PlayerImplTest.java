package de.lmu.ifi.sosylab.fddlj.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PlayerImpl}.
 * 
 * @author Josef Feger
 *
 */
public class PlayerImplTest {

  @Test
  public void testGetName_Single() {
    testGetNameHelper("Max");
  }

  @Test
  public void testGetName_WithSpaces() {
    testGetNameHelper("Max Mustermann");
  }

  private void testGetNameHelper(String name) {
    Color color = new Color(1.0, 1.0, 1.0, 1.0);

    Player player = new PlayerImpl(name, color);
    String actual = player.getName();

    Assertions.assertEquals(name, player.getName(),
        "getName() returned " + actual + " instead of expected " + name);
  }

  @Test
  public void testGetColor() {
    String name = "Max";
    Color color = new Color(0.5, 0.5, 0.5, 1.0);

    Player player = new PlayerImpl(name, color);
    Color actual = player.getColor();

    Assertions.assertEquals(color, player.getColor(),
        "getColor() returned " + actual + " instead of expected " + color);

  }

  @Test
  public void testCompareTo_List() {
    final String nameMax = "Max";
    final String nameMoritz = "Moritz";
    final String namePeter = "Peter";
    final Color colorOne = new Color(1.0, 1.0, 1.0, 1.0);
    final Color colorTwo = new Color(1.0, 0.0, 1.0, 0.5);
    final Color colorThree = new Color(0.5, 1.0, 0.2, 1.0);

    List<PlayerImpl> expected = new ArrayList<>();
    expected.add(new PlayerImpl(nameMax, colorOne));
    expected.add(new PlayerImpl(nameMoritz, colorOne));
    expected.add(new PlayerImpl(namePeter, colorOne));
    expected.add(new PlayerImpl(nameMax, colorTwo));
    expected.add(new PlayerImpl(nameMoritz, colorTwo));
    expected.add(new PlayerImpl(namePeter, colorTwo));
    expected.add(new PlayerImpl(nameMax, colorThree));
    expected.add(new PlayerImpl(nameMoritz, colorThree));
    expected.add(new PlayerImpl(namePeter, colorThree));

    ArrayList<PlayerImpl> sorted = new ArrayList<>(expected);
    Collections.sort(sorted);
    Assertions.assertIterableEquals(expected, sorted, "Players were not ordered correctly");
  }

  @Test
  public void testCompareTo_Transitivity() {
    PlayerImpl x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    PlayerImpl y = new PlayerImpl("Moritz", new Color(1.0, 1.0, 1.0, 1.0));
    PlayerImpl z = new PlayerImpl("Peter", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertTrue(x.compareTo(y) > 0, "x should be before y");
    Assertions.assertTrue(y.compareTo(z) > 0, "y should be before z");
    Assertions.assertTrue(z.compareTo(x) > 0, "z should be before x");
  }

  @Test
  public void testCompareTo_EqualCompareForEqualPlayers() {
    PlayerImpl x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    PlayerImpl y = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertTrue(x.compareTo(y) == 0, "x should be equal y");
  }

  @Test
  public void testCompareTo_Equals() {
    PlayerImpl x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    PlayerImpl y = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertTrue(x.compareTo(y) == 0, "x should be equal y");
    Assertions.assertTrue(x.equals(y), "x should be equal y if x.compareTo(y) == 0");
  }

  @Test
  public void testEquals_Equal() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Player y = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertTrue(x.equals(y), x + " should be equal to " + y);
  }

  @Test
  public void testEquals_Unequal() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Player y = new PlayerImpl("Moritz", new Color(1.0, 1.0, 1.0, 1.0));
    Player z = new PlayerImpl("Max", new Color(1.0, 1.0, 0.0, 1.0));
    Assertions.assertFalse(x.equals(y), "x should not be equal to y");
    Assertions.assertFalse(x.equals(z), "x should not be equal to y");
  }

  @Test
  public void testHashCode_Equal() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Player y = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertTrue(x.equals(y), x + " should be equal to " + y);
    Assertions.assertEquals(x.hashCode(), y.hashCode(),
        "HashCode should be equal for equal players");
  }

  @Test
  public void testHashCode_Unequal() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Player y = new PlayerImpl("Moritz", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertFalse(x.equals(y), x + " should not be equal to " + y);
    Assertions.assertNotEquals(x.hashCode(), y.hashCode(),
        "HashCode should not be equal for unequal players");
  }

  @Test
  public void testToString_Regular() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Assertions.assertEquals("Max", x.toString(), "toString() did not display player correctly");
  }

  @Test
  public void testMakeCopy() {
    Player x = new PlayerImpl("Max", new Color(1.0, 1.0, 1.0, 1.0));
    Player y = x.makeCopy();
    Assertions.assertEquals(y, x, "y should be equal x");
  }



}
