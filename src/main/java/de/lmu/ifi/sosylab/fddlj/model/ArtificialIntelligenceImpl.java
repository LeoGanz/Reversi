package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Set;

public class ArtificialIntelligenceImpl implements ArtificialIntelligence {

  private final int calculationDepth;

  public ArtificialIntelligenceImpl(int calculationDepth) {
    this.calculationDepth = calculationDepth;
  }

  @Override
  public Cell calculateBestMove(GameState state) {
    Model game = new ModelImpl(state, GameMode.HOTSEAT);
    Player currentPlayer = state.getPlayerManagement().getCurrentPlayer();
    Set<Cell> possibleMoves = game.getPossibleMovesForPlayer(currentPlayer);
    double moveValue = Double.NEGATIVE_INFINITY;
    Cell bestMove = null;

    for (Cell cell : possibleMoves) {
      double tempMoveValue = evaluateRecursively(state, cell, currentPlayer, 1);
      if (tempMoveValue > moveValue) {
        moveValue = tempMoveValue;
        bestMove = cell;
      }
    }
    return bestMove;
  }

  private double evaluateRecursively(GameState state, Cell toPlace, Player aiPlayer, int depth) {
    if (depth >= calculationDepth) {
      // TODO deep copy falls notwendig
      return evaluateSituationWithHeuristic(state, aiPlayer, depth);
    }
    Model game = new ModelImpl(state.makeCopy(), GameMode.HOTSEAT);
    game.placeDisk(new DiskImpl(state.getPlayerManagement().getCurrentPlayer()), toPlace);
    if (game.getState().getCurrentPhase().equals(Phase.FINISHED)) {
      // TODO deep copy falls notwendig
      return evaluateSituationWithHeuristic(game.getState(), aiPlayer, depth);
    }

    Player currentPlayer = game.getState().getPlayerManagement().getCurrentPlayer();
    Set<Cell> possibleMoves = game.getPossibleMovesForPlayer(currentPlayer);
    double moveValue;
    if (currentPlayer.equals(aiPlayer)) {
      moveValue = Double.NEGATIVE_INFINITY;
      for (Cell cell : possibleMoves) {
        // TODO vielleicht heuristik aufaddieren
        double tempMoveValue = evaluateRecursively(game.getState(), cell, aiPlayer, depth + 1);
        if (tempMoveValue > moveValue) {
          moveValue = tempMoveValue;
        }
      }
    } else {
      moveValue = Double.POSITIVE_INFINITY;
      for (Cell cell : possibleMoves) {
        // TODO vielleicht heuristik aufaddieren
        double tempMoveValue = evaluateRecursively(game.getState(), cell, aiPlayer, depth + 1);
        if (tempMoveValue < moveValue) {
          moveValue = tempMoveValue;
        }
      }
    }
    return moveValue;
  }

  private double evaluateSituationWithHeuristic(GameState state, Player aiPlayer, int depth) {
    return state.getField().getAllCellsForPlayer(aiPlayer).size();
  }
}
