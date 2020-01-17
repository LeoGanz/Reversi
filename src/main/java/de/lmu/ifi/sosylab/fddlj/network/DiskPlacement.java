package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import java.util.Objects;
import java.util.UUID;

/**
 * Data storage type to combine {@link Disk} and {@link Cell} objects. For integrity and ordering
 * purposes a DiskPlacement stores its own (generated) {@link UUID} and the UUID of its predecessor.
 *
 * @author Leonard Ganz
 */
public class DiskPlacement {

  private UUID previous;
  private UUID uuid;
  private Disk disk;
  private Cell location;

  /**
   * Create a new {@link DiskPlacement}. A {@link UUID} will be generated automatically.
   *
   * @param previous UUID of the disk placement's predecessor. Can be <code>null</code> to indicate
   *     that there was no previous placement
   * @param disk {@link Disk} to be placed
   * @param location {@link Cell} on which the disk shall be placed
   * @throws NullPointerException if disk or location are <code>null</code>
   */
  public DiskPlacement(UUID previous, Disk disk, Cell location) {
    this.previous = previous;
    this.disk = Objects.requireNonNull(disk);
    this.location = Objects.requireNonNull(location);
    uuid = UUID.randomUUID();
  }

  /**
   * The ID of the predecessor can be used to ensure the correct order is maintained.
   *
   * @return the {@link UUID} of this disk placements predecessor.
   */
  public UUID getPrevious() {
    return previous;
  }

  /**
   * Get the {@link UUID}.
   *
   * @return the {@link UUID} of this disk placement
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Get the {@link Disk}.
   *
   * @return the {@link Disk} to place
   */
  public Disk getDisk() {
    return disk;
  }

  /**
   * Get the location.
   *
   * @return the {@link Cell} on which the {@link Disk} shall be placed
   */
  public Cell getLocation() {
    return location;
  }

  @Override
  public int hashCode() {
    return Objects.hash(disk, location, previous, uuid);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof DiskPlacement)) {
      return false;
    }
    DiskPlacement other = (DiskPlacement) obj;
    return Objects.equals(disk, other.disk)
        && Objects.equals(location, other.location)
        && Objects.equals(previous, other.previous)
        && Objects.equals(uuid, other.uuid);
  }
}
