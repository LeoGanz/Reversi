package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeuristicImplTest {

  private final Player playerOne = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
  private final Player playerTwo = new AiPlayerImpl("Rhea", Color.ALICEBLUE);
  private Heuristic heuristic = new HeuristicImpl();

  private ModifiableGameState earlyGame_PlayerTwosTurn() {
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(3, 3), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(3, 4), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(3, 5), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(4, 3), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(4, 4), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(4, 5), new DiskImpl(manager.getPlayerTwo()));

    manager.switchCurrentPlayer();

    ModifiableGameState state = new GameStateImpl();
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(manager);
    return state;
  }

  private ModifiableGameState midGame_PlayerTwosTurn() {

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

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

  @Test
  public void testEvaluateSituationWithHeuristic_EarlyGame() {
    Assertions.assertEquals(
        3, heuristic.evaluateSituationWithHeuristic(earlyGame_PlayerTwosTurn(), playerTwo, 3));
  }

  @Test
    public void testEvaluateSituationWithHeuristic(){
      Assertions.assertEquals(14,heuristic.evaluateSituationWithHeuristic(midGame_PlayerTwosTurn(),playerTwo,3));
  }
}
