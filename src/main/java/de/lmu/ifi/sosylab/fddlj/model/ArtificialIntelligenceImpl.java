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
  private final Heuristic heuristic;

  /**
   * Creates an AI with a given calculation depth.
   *
   * @param calculationDepth is the number of successive moves evaluated in the minmax.
   */
  public ArtificialIntelligenceImpl(int calculationDepth, Heuristic heuristic) {
    this.calculationDepth = calculationDepth;
    this.heuristic = heuristic;
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
      return evaluateSituationWithHeuristic(state, aiPlayer, depth);
    }
    Model game = new ModelImpl(state.makeCopy(), GameMode.HOTSEAT);
    game.placeDisk(new DiskImpl(state.getPlayerManagement().getCurrentPlayer()), toPlace);
    if (game.getState().getCurrentPhase().equals(Phase.FINISHED)) {
      return evaluateSituationWithHeuristic(game.getState(), aiPlayer, depth);
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
  private double evaluateSituationWithHeuristic(GameState state, Player aiPlayer, int depth) {
    return heuristic.evaluateSituationWithHeuristic(state, aiPlayer, depth);
  }
}
