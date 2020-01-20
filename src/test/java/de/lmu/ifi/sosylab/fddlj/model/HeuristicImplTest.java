package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class HeuristicImplTest {

  private final Player playerOne = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
  private final Player playerTwo = new AiPlayerImpl("Rhea", Color.ALICEBLUE);
  private final ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
  private Heuristic heuristic = new HeuristicImpl();
  private Disk diskOne = new DiskImpl(playerOne);
  private Disk diskTwo = new DiskImpl(playerTwo);

  private ModifiableGameState earlyGame_PlayerTwosTurn() {
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(3, 3), diskOne);
    field.set(new CellImpl(3, 4), diskOne);
    field.set(new CellImpl(3, 5), diskOne);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(4, 4), diskTwo);
    field.set(new CellImpl(4, 5), diskTwo);

    manager.switchCurrentPlayer();

    ModifiableGameState state = new GameStateImpl();
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(manager);
    return state;
  }

  private ModifiableGameState midGame_PlayerTwosTurn() {

    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();

    field.set(new CellImpl(3, 1), diskTwo);
    field.set(new CellImpl(2, 2), diskTwo);
    field.set(new CellImpl(3, 2), diskTwo);
    field.set(new CellImpl(1, 3), diskTwo);
    field.set(new CellImpl(2, 3), diskTwo);
    field.set(new CellImpl(3, 3), diskTwo);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(5, 3), diskTwo);
    field.set(new CellImpl(1, 4), diskOne);
    field.set(new CellImpl(2, 4), diskOne);
    field.set(new CellImpl(3, 4), diskOne);
    field.set(new CellImpl(4, 4), diskTwo);
    field.set(new CellImpl(5, 4), diskTwo);
    field.set(new CellImpl(2, 5), diskOne);
    field.set(new CellImpl(3, 5), diskTwo);
    field.set(new CellImpl(4, 5), diskTwo);
    field.set(new CellImpl(5, 5), diskTwo);
    field.set(new CellImpl(3, 6), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState veryLateGame_PlayerTwosTurn() {

    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 8; j++) {
        field.set(new CellImpl(i, j), diskOne);
      }
    }
    for (int i = 5; i < 7; i++) {
      for (int j = 2; j < 8; j++) {
        field.set(new CellImpl(i, j), diskOne);
      }
    }
    field.set(new CellImpl(5, 0), diskTwo);
    field.set(new CellImpl(5, 1), diskTwo);
    field.set(new CellImpl(7, 2), diskOne);
    field.set(new CellImpl(7, 4), diskTwo);
    field.set(new CellImpl(7, 5), diskTwo);
    field.set(new CellImpl(7, 6), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midToLateGame_PlayerTwosTurn() {

    manager.switchCurrentPlayer();
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(0, 7), diskOne);
    field.set(new CellImpl(0, 6), diskOne);
    field.set(new CellImpl(0, 5), diskOne);
    field.set(new CellImpl(1, 7), diskOne);
    field.set(new CellImpl(1, 6), diskOne);
    field.set(new CellImpl(1, 5), diskOne);
    field.set(new CellImpl(1, 4), diskOne);
    field.set(new CellImpl(1, 3), diskOne);
    field.set(new CellImpl(2, 4), diskOne);
    field.set(new CellImpl(3, 3), diskOne);
    field.set(new CellImpl(4, 4), diskOne);
    field.set(new CellImpl(4, 3), diskOne);
    field.set(new CellImpl(4, 2), diskOne);
    field.set(new CellImpl(0, 1), diskTwo);
    field.set(new CellImpl(1, 2), diskTwo);
    field.set(new CellImpl(2, 2), diskTwo);
    field.set(new CellImpl(2, 3), diskTwo);
    field.set(new CellImpl(3, 1), diskTwo);
    field.set(new CellImpl(3, 2), diskTwo);
    field.set(new CellImpl(2, 5), diskTwo);
    field.set(new CellImpl(2, 6), diskTwo);
    field.set(new CellImpl(3, 4), diskTwo);
    field.set(new CellImpl(3, 5), diskTwo);
    field.set(new CellImpl(3, 6), diskTwo);
    field.set(new CellImpl(4, 5), diskTwo);
    field.set(new CellImpl(4, 6), diskTwo);
    field.set(new CellImpl(5, 3), diskTwo);
    field.set(new CellImpl(5, 4), diskTwo);
    field.set(new CellImpl(5, 5), diskTwo);
    field.set(new CellImpl(6, 4), diskTwo);
    field.set(new CellImpl(6, 5), diskTwo);
    field.set(new CellImpl(7, 4), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState lastMove06Game_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(0, row), diskOne);
    }

    for (int column = 1; column < 3; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), diskOne);
      }
    }
    for (int column = 2; column < 8; column++) {
      field.set(new CellImpl(column, 0), diskOne);
    }
    for (int row = 1; row < 7; row++) {
      field.set(new CellImpl(7, row), diskOne);
    }
    field.set(new CellImpl(3, 1), diskOne);
    field.set(new CellImpl(4, 1), diskOne);
    field.set(new CellImpl(6, 1), diskOne);
    field.set(new CellImpl(2, 2), diskOne);
    field.set(new CellImpl(5, 2), diskOne);
    field.set(new CellImpl(5, 3), diskOne);
    field.set(new CellImpl(3, 4), diskOne);
    field.set(new CellImpl(4, 4), diskOne);
    field.set(new CellImpl(5, 5), diskOne);
    field.set(new CellImpl(6, 6), diskOne);
    field.set(new CellImpl(6, 7), diskOne);
    field.set(new CellImpl(0, 7), diskOne);

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(1, row), diskTwo);
    }
    for (int column = 0; column < 5; column++) {
      field.set(new CellImpl(column, 5), diskTwo);
    }
    for (int column = 3; column < 6; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), diskTwo);
      }
    }
    for (int row = 2; row < 6; row++) {
      field.set(new CellImpl(6, row), diskTwo);
    }
    field.set(new CellImpl(2, 1), diskTwo);
    field.set(new CellImpl(2, 3), diskTwo);
    field.set(new CellImpl(2, 4), diskTwo);
    field.set(new CellImpl(3, 2), diskTwo);
    field.set(new CellImpl(3, 3), diskTwo);
    field.set(new CellImpl(4, 2), diskTwo);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(5, 1), diskTwo);
    field.set(new CellImpl(5, 4), diskTwo);
    field.set(new CellImpl(7, 7), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState finishedGame_PlayerTwoWins() {
    ModifiableGameField field = new GameFieldImpl();
    for (int column = 0; column < 7; column++) {
      for (int row = 0; row < 8; row++) {
        field.set(new CellImpl(column, row), diskTwo);
      }
    }
    for (int row = 0; row < 8; row++) {
      field.set(new CellImpl(7, row), diskOne);
    }

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.FINISHED);
    state.setGameField(field);
    state.setPlayerManagement(manager);
    state.getPlayerManagement().setWinner(Optional.of(playerTwo));

    return state;
  }

  private ModifiableGameState playerTwoDisksInCorners() {
    ModifiableGameField field = new GameFieldImpl();

    for (int row = 1; row < 5; row++) {
      field.set(new CellImpl(0, row), diskOne);
    }

    field.set(new CellImpl(0, 6), diskOne);
    field.set(new CellImpl(1, 2), diskOne);
    field.set(new CellImpl(1, 4), diskOne);
    field.set(new CellImpl(1, 5), diskOne);
    field.set(new CellImpl(3, 6), diskOne);
    field.set(new CellImpl(5, 6), diskOne);
    field.set(new CellImpl(6, 7), diskOne);

    for (int column = 2; column < 5; column++) {
      for (int row = 2; row < 6; row++) {
        field.set(new CellImpl(column, row), diskOne);
      }
    }

    for (int column = 5; column < 8; column++) {
      for (int row = 0; row < 6; row++) {
        field.set(new CellImpl(column, row), diskTwo);
      }
    }

    field.set(new CellImpl(4, 0), diskTwo);
    field.set(new CellImpl(4, 1), diskTwo);
    field.set(new CellImpl(1, 3), diskTwo);
    field.set(new CellImpl(6, 6), diskTwo);
    field.set(new CellImpl(7, 6), diskTwo);
    field.set(new CellImpl(7, 7), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);
    manager.switchCurrentPlayer();

    return state;
  }

  @Test
  public void testEvaluateSituationWithHeuristic_EarlyGame() {
    Assertions.assertEquals(
        3, heuristic.evaluateSituationWithHeuristic(earlyGame_PlayerTwosTurn(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_midGame() {
    Assertions.assertEquals(
        14, heuristic.evaluateSituationWithHeuristic(midGame_PlayerTwosTurn(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_veryLateGame() {
    Assertions.assertEquals(
        -9, heuristic.evaluateSituationWithHeuristic(veryLateGame_PlayerTwosTurn(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_MidToLateGame() {
    Assertions.assertEquals(
        12, heuristic.evaluateSituationWithHeuristic(midToLateGame_PlayerTwosTurn(), playerTwo, 3));
  }

  @Test
  public void testEvaluatedSituationWithHeuristic_LastMove() {
    Model model = new ModelImpl(lastMove06Game_PlayerOnesTurn(), GameMode.HOTSEAT);
    model.placeDisk(new DiskImpl(playerOne), new CellImpl(0, 6));
    Assertions.assertEquals(
        -500, heuristic.evaluateSituationWithHeuristic(model.getState(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_FinishedGameAIWins() {
    Assertions.assertEquals(
        500, heuristic.evaluateSituationWithHeuristic(finishedGame_PlayerTwoWins(), playerTwo, 3));
  }

  @Test
  public void testEvaluateSituationWithHeuristic_PlayerTwoDisksInCorners() {
    Assertions.assertEquals(
        38, heuristic.evaluateSituationWithHeuristic(playerTwoDisksInCorners(), playerTwo, 3));
  }
}
