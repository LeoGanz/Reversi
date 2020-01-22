package de.lmu.ifi.sosylab.fddlj.network.communication;

import de.lmu.ifi.sosylab.fddlj.network.Server;
import java.util.UUID;

/**
 * A {@link Server}'s response when a requested {@link DiskPlacement} is denied.
 *
 * @author Leonard Ganz
 */
public class RejectedPlacement {
  private UUID rejectedPlacementID;
  private Reason reason;

  /**
   * Create a new {@link RejectedPlacement} with a {@link Reason}.
   *
   * @param rejectedPlacementID {@link UUID} of the disk placement that was rejected
   * @param reason the reason why the disk placement was rejected
   */
  public RejectedPlacement(UUID rejectedPlacementID, Reason reason) {
    this.rejectedPlacementID = rejectedPlacementID;
    this.reason = reason;
  }

  /**
   * Get the {@link UUID} of the disk placement that was rejected.
   *
   * @return the uuid of the rejected disk placement
   */
  public UUID getRejectedPlacementID() {
    return rejectedPlacementID;
  }

  /**
   * Get an explanation why the {@link Server} rejected the {@link DiskPlacement}.
   *
   * @return a {@link Reason} for the rejection
   */
  public Reason getReason() {
    return reason;
  }

  /**
   * The reason a server will give why it rejected the {@link DiskPlacement}.
   *
   * @author Leonard Ganz
   */
  public enum Reason {
    NO_LOBBY_JOINED,
    GAME_NOT_RUNNING,
    INVALID_PREVIOUS_UUID,
    NOT_YOUR_TURN,
    INVALID_PLACEMENT
  }
}
