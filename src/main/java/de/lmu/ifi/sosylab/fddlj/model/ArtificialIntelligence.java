package de.lmu.ifi.sosylab.fddlj.model;

/**
 * Interface for all reversi artificial intelligence implementations.
 *
 * @author Daniel Leidreiter
 */
public interface ArtificialIntelligence {

  /**
   * Calculates the best move for the current Player.
   *
   * @param state for which the best move shall be calculated
   * @return {@link Cell} which is the best place for the current player to place a {@link Disk}
   */
  Cell calculateBestMove(GameState state);
}
