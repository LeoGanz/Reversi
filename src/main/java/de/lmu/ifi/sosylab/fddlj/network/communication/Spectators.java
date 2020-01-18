package de.lmu.ifi.sosylab.fddlj.network.communication;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Set} of {@link Player}s spectating a specified game lobby.
 *
 * @author Leonard Ganz
 */
public class Spectators extends HashSet<Player> {

  private static final long serialVersionUID = 1L;
  private int lobbyId;

  /**
   * Create a new summary of spectators.
   *
   * @param lobbyId integer to identify the lobby being spectated
   */
  public Spectators(int lobbyId) {
    super();
    this.lobbyId = lobbyId;
  }

  /**
   * Create a new summary of spectators.
   *
   * @param lobbyId integer to identify the lobby being spectated
   * @param c collection with players that are spectating
   */
  public Spectators(int lobbyId, Collection<? extends Player> c) {
    super(c);
    this.lobbyId = lobbyId;
  }

  /**
   * Create a new summary of spectators.
   *
   * @param lobbyId integer to identify the lobby being spectated
   * @param initialCapacity the initial capacity of the map
   * @param loadFactor the load factor of the map
   */
  public Spectators(int lobbyId, int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
    this.lobbyId = lobbyId;
  }

  /**
   * Create a new summary of spectators.
   *
   * @param lobbyId integer to identify the lobby being spectated
   * @param initialCapacity the initial capacity of the map
   */
  public Spectators(int lobbyId, int initialCapacity) {
    super(initialCapacity);
    this.lobbyId = lobbyId;
  }

  /**
   * Get the ID of the lobby that is spectated.
   *
   * @return integer to identify the lobby being spectated
   */
  public int getLobbyId() {
    return lobbyId;
  }
}
