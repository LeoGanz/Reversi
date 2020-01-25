package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;
import javafx.scene.paint.Color;

/**
 * A concrete implementation of {@link Player}. In addition to the methods specified by the
 * interface PlayerImpl provides hashCode, equals, toString and compareTo methods.
 *
 * @author Josef Feger
 */
public class PlayerImpl implements Player, Comparable<Player> {

  private String name;
  private Color color;

  /**
   * Create a new player with the given name and color.
   *
   * @param name the player's name
   * @param color the color the player wishes to use
   */
  public PlayerImpl(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Player)) {
      return false;
    }

    Player other = (Player) obj;
    return Objects.equals(name, other.getName()) && Objects.equals(color, other.getColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, color);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(Player o) {
    return name.compareTo(o.getName());
  }

  @Override
  public Player makeCopy() {
    return new PlayerImpl(this.name, this.color);
  }
}
