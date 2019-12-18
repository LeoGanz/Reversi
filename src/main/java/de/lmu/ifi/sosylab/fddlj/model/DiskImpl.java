package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A concrete implementation of {@link Disk}.
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

}
