package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeuristicImplTest {

  private final Player playerOne = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
  private final Player playerTwo = new AiPlayerImpl("Rhea", Color.ALICEBLUE);
  private Heuristic heuristic = new HeuristicImpl();

  @Test
  public void testEvaluateSituationWithHeuristic_EarlyGame() {
    Assertions.assertEquals(
        3,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_midGame() {
    Assertions.assertEquals(
        14,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_veryLateGame() {
    Assertions.assertEquals(
        -9,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.veryLateGame_PlayerTwosTurn(playerOne, playerTwo), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_MidToLateGame() {
    Assertions.assertEquals(
        12,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.midToLateGame_PlayerTwosTurn(playerOne, playerTwo), playerTwo, 3));
  }

  @Test
  public void testEvaluatedSituationWithHeuristic_LastMove() {
    Model model =
        new ModelImpl(
            GameStateUtility.lastMove06Game_PlayerOnesTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    model.placeDisk(new DiskImpl(playerOne), new CellImpl(0, 6));
    Assertions.assertEquals(
        -500, heuristic.evaluateSituationWithHeuristic(model.getState(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_FinishedGameAiWins() {
    Assertions.assertEquals(
        500,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.finishedGame_PlayerTwoWins(playerOne, playerTwo), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_PlayerTwoDisksInCorners() {
    Assertions.assertEquals(
        38,
        heuristic.evaluateSituationWithHeuristic(
            GameStateUtility.playerTwoDisksInCorners(playerOne, playerTwo), playerTwo, 3));
  }
}
