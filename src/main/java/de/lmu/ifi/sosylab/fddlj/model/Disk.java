package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A game token that belongs to the specified {@link Player}.
 *
 * @author Leonard Ganz, Josef Feger
 */
public interface Disk {

  /**
   * Get the {@link Player} this disk belongs to.
   *
   * @return the disk's player
   */
  Player getPlayer();

}
