package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;

/**
 * A concrete implementation of {@link Disk}. In addition to the methods specified by the interface
 * DiskImpl provides hashCode, equals and toString methods.
 *
 * @author Florian Theimer
 */
public class DiskImpl implements Disk {

  private Player player;

  /**
   * Create a new Disk with the given player.
   *
   * @param player disk's player
   */
  public DiskImpl(Player player) {
    this.player = player;
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.player);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Disk)) {
      return false;
    }

    Disk other = (Disk) obj;
    return Objects.equals(this.player, other.getPlayer());
  }

  @Override
  public String toString() {
    return "Disk with player " + this.player.toString();
  }

}
