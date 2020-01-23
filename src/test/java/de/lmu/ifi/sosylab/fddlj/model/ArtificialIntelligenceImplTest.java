package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArtificialIntelligenceImplTest {

  private final Player one = new PlayerImpl("Daenerys", Color.DARKRED);
  private final Player two = new PlayerImpl("WhiteWalker", Color.LIGHTBLUE);
  private final Disk diskOne = new DiskImpl(one);
  private final Disk diskTwo = new DiskImpl(two);

  private class DummyHeuristic implements Heuristic {

    @Override
    public double evaluateSituationWithHeuristic(GameState state, Player aiPlayer, int depth) {
      return state.getField().getAllCellsForPlayer(aiPlayer).size();
    }

    @SuppressWarnings("unused")
    public Player appeaseSpotbugs() {
      return one;
    }
  }

  private void helpPlaceDisk(Model game, Disk disk, int column, int row) {
    Assertions.assertTrue(game.placeDisk(disk, new CellImpl(column, row)));
  }

  @Test
  public void testCalculateBestMove_PlayerOneDepthOne() {
    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    ArtificialIntelligenceImpl ai = new ArtificialIntelligenceImpl(1, new DummyHeuristic());

    Assertions.assertEquals(new CellImpl(3, 3), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 3, 3);
    helpPlaceDisk(game, diskTwo, 4, 3);

    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 3, 4);
    helpPlaceDisk(game, diskTwo, 4, 4);

    Assertions.assertEquals(new CellImpl(5, 2), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 5, 2);
    helpPlaceDisk(game, diskTwo, 2, 2);

    Assertions.assertEquals(new CellImpl(2, 3), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 2, 3);
    helpPlaceDisk(game, diskTwo, 2, 4);

    Assertions.assertEquals(new CellImpl(1, 1), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 1, 1);
    helpPlaceDisk(game, diskTwo, 4, 2);

    Assertions.assertEquals(new CellImpl(2, 5), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 2, 5);
  }

  @Test
  public void testCalculateBestMove_PlayerTwoDepthOne() {
    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    ArtificialIntelligenceImpl ai = new ArtificialIntelligenceImpl(1, new DummyHeuristic());

    helpPlaceDisk(game, diskOne, 3, 3);
    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 3, 4);

    helpPlaceDisk(game, diskOne, 4, 4);
    Assertions.assertEquals(new CellImpl(4, 3), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 4, 3);

    helpPlaceDisk(game, diskOne, 4, 2);
    Assertions.assertEquals(new CellImpl(3, 2), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 3, 2);

    helpPlaceDisk(game, diskOne, 2, 2);
    Assertions.assertEquals(new CellImpl(3, 1), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 3, 1);

    helpPlaceDisk(game, diskOne, 2, 3);
    Assertions.assertEquals(new CellImpl(1, 2), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 1, 2);

    helpPlaceDisk(game, diskOne, 0, 2);
    Assertions.assertEquals(new CellImpl(5, 3), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 5, 3);
  }

  @Test
  public void testCalculateBestMove_PlayerOneDepthTwo() {
    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    ArtificialIntelligenceImpl ai = new ArtificialIntelligenceImpl(2, new DummyHeuristic());

    Assertions.assertEquals(new CellImpl(3, 3), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 3, 3);
    helpPlaceDisk(game, diskTwo, 4, 3);

    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 3, 4);
    helpPlaceDisk(game, diskTwo, 4, 4);

    Assertions.assertEquals(new CellImpl(5, 2), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 5, 2);
    helpPlaceDisk(game, diskTwo, 2, 2);

    Assertions.assertEquals(new CellImpl(4, 5), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 4, 5);
    helpPlaceDisk(game, diskTwo, 3, 5);

    Assertions.assertEquals(new CellImpl(1, 1), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskOne, 1, 1);
  }

  @Test
  public void testCalculateBestMove_PlayerTwoDepthTwo() {
    Model game = new ModelImpl(GameMode.HOTSEAT, one, two);
    ArtificialIntelligenceImpl ai = new ArtificialIntelligenceImpl(2, new DummyHeuristic());

    helpPlaceDisk(game, diskOne, 3, 3);
    Assertions.assertEquals(new CellImpl(3, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 3, 4);

    helpPlaceDisk(game, diskOne, 4, 3);
    Assertions.assertEquals(new CellImpl(4, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 4, 4);

    helpPlaceDisk(game, diskOne, 4, 5);
    Assertions.assertEquals(new CellImpl(5, 2), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 5, 2);

    helpPlaceDisk(game, diskOne, 3, 5);
    Assertions.assertEquals(new CellImpl(2, 5), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 2, 5);

    helpPlaceDisk(game, diskOne, 5, 3);
    Assertions.assertEquals(new CellImpl(5, 4), ai.calculateBestMove(game.getState()));
    helpPlaceDisk(game, diskTwo, 5, 4);
  }

  @Test
  public void testCalculateBestMove_NoMovePossible() {
    ModifiableGameState state = new GameStateImpl();
    state.setPlayerManagement(new PlayerManagementImpl(one, two));
    state.setCurrentPhase(Phase.RUNNING);
    ModifiableGameField field = new GameFieldImpl(8);
    for (int i = 0; i < field.getSize(); i++) {
      for (int j = 0; j < field.getSize(); j++) {
        field.set(new CellImpl(i, j), diskTwo);
      }
    }
    state.setGameField(field);

    Model game = new ModelImpl(state, GameMode.HOTSEAT);
    ArtificialIntelligence ai = new ArtificialIntelligenceImpl(2, new DummyHeuristic());

    Assertions.assertTrue(game.getPossibleMovesForPlayer(one).isEmpty());
    Assertions.assertNull(ai.calculateBestMove(game.getState()));
  }
}
