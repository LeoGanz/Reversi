package de.lmu.ifi.sosylab.fddlj.network.communication;

import de.lmu.ifi.sosylab.fddlj.model.GameState;
import java.util.UUID;

/**
 * Combination of a {@link GameState} with a {@link UUID} that references the last {@link
 * DiskPlacement} made. Used for updates between server and client.
 *
 * @author Leonard Ganz
 */
public class GameStateWithLastPlacementUuid {

  private GameState gameState;
  private UUID lastPlacementUuid;

  /**
   * Create a new gamestate/uuid combo.
   *
   * @param gameState the gamestate with some placements already made
   * @param lastPlacementUuid id of the last placement executed
   */
  public GameStateWithLastPlacementUuid(GameState gameState, UUID lastPlacementUuid) {
    this.gameState = gameState;
    this.lastPlacementUuid = lastPlacementUuid;
  }

  /**
   * Get the {@link GameState}.
   *
   * @return the gamestate
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Get the Id of the last placement made.
   *
   * @return uuid of the last placement
   */
  public UUID getLastPlacementUuid() {
    return lastPlacementUuid;
  }
}
