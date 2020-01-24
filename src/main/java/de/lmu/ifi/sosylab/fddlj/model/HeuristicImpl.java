package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Set;

public class HeuristicImpl implements Heuristic {

  private static final int WEIGHT_GAME_FINISHED = 500;
  private static final int WEIGHT_CORNER_DISKS = 7;

  @Override
  public double evaluateSituationWithHeuristic(GameState state, Player aiPlayer, int depth) {
    if (state.getCurrentPhase().equals(Phase.FINISHED)
        && state.getPlayerManagement().getWinner().isPresent()) {
      if (state.getPlayerManagement().getWinner().get().equals(aiPlayer)) {
        return WEIGHT_GAME_FINISHED;
      } else {
        return -WEIGHT_GAME_FINISHED;
      }
    }
    return numberOfDisks(state, aiPlayer)
        + disksInCorner(state, aiPlayer) * WEIGHT_CORNER_DISKS
        - disksInCorner(state, state.getPlayerManagement().getOpponentPlayer(aiPlayer))
            * WEIGHT_CORNER_DISKS;
  }

  /**
   * Counts the number of disks of a player.
   *
   * @param state which will be evaluated
   * @param player for which the disks should be counted
   * @return the number of disks for the player
   */
  private double numberOfDisks(GameState state, Player player) {
    return state.getField().getAllCellsForPlayer(player).size();
  }

  /**
   * Counts the disks of a player that are in the corner.
   *
   * @param state which will be evaluated
   * @param player for which the disks should be counted
   * @return the number of disks for the player that are in the corner
   */
  private double disksInCorner(GameState state, Player player) {
    Set<Cell> disksOfPlayer = state.getField().getAllCellsForPlayer(player);
    double temp = 0;
    int size = state.getField().getSize();
    for (int column = 0; column < size; column = column + (size - 1)) {
      for (int row = 0; row < size; row = row + (size - 1)) {
        if (disksOfPlayer.contains(new CellImpl(column, row))) {
          temp++;
        }
      }
    }
    return temp;
  }
}
