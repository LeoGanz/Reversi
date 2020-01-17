package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Set;
import java.util.TreeSet;

/**
 * Artificial intelligence using the minmax-algorithm.
 *
 * @author Daniel Leidreiter, Dora Pruteanu
 */
public class ArtificialIntelligenceImpl implements ArtificialIntelligence {

  private final int calculationDepth;

  /**
   * Creates an AI with a given calculation depth.
   *
   * @param calculationDepth is the number of successive moves evaluated in the minmax.
   */
  public ArtificialIntelligenceImpl(int calculationDepth) {
    this.calculationDepth = calculationDepth;
  }

  @Override
  public Cell calculateBestMove(GameState state) {
    Model game = new ModelImpl(state, GameMode.HOTSEAT);
    Player currentPlayer = state.getPlayerManagement().getCurrentPlayer();
    Set<Cell> possibleMoves = new TreeSet<>(game.getPossibleMovesForPlayer(currentPlayer));
    double moveValue = Double.NEGATIVE_INFINITY;
    Cell bestMove = null;

    for (Cell cell : possibleMoves) {
      double tempMoveValue = evaluateRecursively(state, cell, currentPlayer, 0);
      if (tempMoveValue > moveValue) {
        moveValue = tempMoveValue;
        bestMove = cell;
      }
    }
    return bestMove;
  }

  /**
   * Evaluates a given move recursively.
   *
   * @param state upon which the move is made and then recursively evaluated
   * @param toPlace is the move that will be made and then evaluated
   * @param aiPlayer is the player for whom the best move is searched
   * @param depth is the number of successive moves already evaluated
   * @return the {@code double} value of the evaluated move
   */
  private double evaluateRecursively(GameState state, Cell toPlace, Player aiPlayer, int depth) {
    if (depth >= calculationDepth) {
      return evaluateSituationWithHeuristic(state, aiPlayer);
    }
    Model game = new ModelImpl(state.makeCopy(), GameMode.HOTSEAT);
    game.placeDisk(new DiskImpl(state.getPlayerManagement().getCurrentPlayer()), toPlace);
    if (game.getState().getCurrentPhase().equals(Phase.FINISHED)) {
      return evaluateSituationWithHeuristic(game.getState(), aiPlayer);
    }

    Player currentPlayer = game.getState().getPlayerManagement().getCurrentPlayer();
    Set<Cell> possibleMoves = new TreeSet<>(game.getPossibleMovesForPlayer(currentPlayer));
    double moveValue;
    if (currentPlayer.equals(aiPlayer)) {
      moveValue = Double.NEGATIVE_INFINITY;
      for (Cell cell : possibleMoves) {
        double tempMoveValue = evaluateRecursively(game.getState(), cell, aiPlayer, depth + 1);
        if (tempMoveValue > moveValue) {
          moveValue = tempMoveValue;
        }
      }
    } else {
      moveValue = Double.POSITIVE_INFINITY;
      for (Cell cell : possibleMoves) {
        double tempMoveValue = evaluateRecursively(game.getState(), cell, aiPlayer, depth + 1);
        if (tempMoveValue < moveValue) {
          moveValue = tempMoveValue;
        }
      }
    }
    return moveValue;
  }

  /**
   * Evaluates the state.
   *
   * @param state which will be evaluated
   * @param aiPlayer from whose perspective it shall be evaluated
   * @return {@code double} value of the given state
   */
  private double evaluateSituationWithHeuristic(GameState state, Player aiPlayer) {
    if (state.getCurrentPhase().equals(Phase.FINISHED)
        && state.getPlayerManagement().getWinner().isPresent()) {
      if (state.getPlayerManagement().getWinner().get().equals(aiPlayer)) {
        return 500;
      } else {
        return -500;
      }
    }
    return numberOfDisks(state, aiPlayer)
        + disksInCorner(state, aiPlayer)
        - disksInCorner(state, state.getPlayerManagement().getOpponentPlayer(aiPlayer));
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
    for (int column = 0; column < 8; column = column + 7) {
      for (int row = 0; row < 8; row = row + 7) {
        if (disksOfPlayer.contains(new CellImpl(column, row))) {
          temp++;
        }
      }
    }
    return temp * 7;
  }
}
