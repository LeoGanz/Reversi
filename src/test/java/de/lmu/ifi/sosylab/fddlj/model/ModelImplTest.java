package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelImplTest {

  private final Player playerOne = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
  private final Player playerTwo = new PlayerImpl("Rhea", Color.ALICEBLUE);

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

  private ModifiableGameState veryLateGame_PlayerTwosTurn() {

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

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
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(0, 7), new DiskImpl(playerOne));
    field.set(new CellImpl(0, 6), new DiskImpl(playerOne));
    field.set(new CellImpl(0, 5), new DiskImpl(playerOne));
    field.set(new CellImpl(1, 7), new DiskImpl(playerOne));
    field.set(new CellImpl(1, 6), new DiskImpl(playerOne));
    field.set(new CellImpl(1, 5), new DiskImpl(playerOne));
    field.set(new CellImpl(1, 4), new DiskImpl(playerOne));
    field.set(new CellImpl(1, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(2, 4), new DiskImpl(playerOne));
    field.set(new CellImpl(3, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 4), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 2), new DiskImpl(playerOne));
    field.set(new CellImpl(0, 1), new DiskImpl(playerTwo));
    field.set(new CellImpl(1, 2), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 2), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 1), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 2), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 5), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 6), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 4), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 5), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 6), new DiskImpl(playerTwo));
    field.set(new CellImpl(4, 5), new DiskImpl(playerTwo));
    field.set(new CellImpl(4, 6), new DiskImpl(playerTwo));
    field.set(new CellImpl(5, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(5, 4), new DiskImpl(playerTwo));
    field.set(new CellImpl(5, 5), new DiskImpl(playerTwo));
    field.set(new CellImpl(6, 4), new DiskImpl(playerTwo));
    field.set(new CellImpl(6, 5), new DiskImpl(playerTwo));
    field.set(new CellImpl(7, 4), new DiskImpl(playerTwo));

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState lastMove06Game_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(0, row), new DiskImpl(playerOne));
    }

    for (int column = 1; column < 3; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), new DiskImpl(playerOne));
      }
    }
    for (int column = 2; column < 8; column++) {
      field.set(new CellImpl(column, 0), new DiskImpl(playerOne));
    }
    for (int row = 1; row < 7; row++) {
      field.set(new CellImpl(7, row), new DiskImpl(playerOne));
    }
    field.set(new CellImpl(3, 1), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 1), new DiskImpl(playerOne));
    field.set(new CellImpl(6, 1), new DiskImpl(playerOne));
    field.set(new CellImpl(2, 2), new DiskImpl(playerOne));
    field.set(new CellImpl(5, 2), new DiskImpl(playerOne));
    field.set(new CellImpl(5, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(3, 4), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 4), new DiskImpl(playerOne));
    field.set(new CellImpl(5, 5), new DiskImpl(playerOne));
    field.set(new CellImpl(6, 6), new DiskImpl(playerOne));
    field.set(new CellImpl(6, 7), new DiskImpl(playerOne));
    field.set(new CellImpl(0, 7), new DiskImpl(playerOne));

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(1, row), new DiskImpl(playerTwo));
    }
    for (int column = 0; column < 5; column++) {
      field.set(new CellImpl(column, 5), new DiskImpl(playerTwo));
    }
    for (int column = 3; column < 6; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), new DiskImpl(playerTwo));
      }
    }
    for (int row = 2; row < 6; row++) {
      field.set(new CellImpl(6, row), new DiskImpl(playerTwo));
    }
    field.set(new CellImpl(2, 1), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(2, 4), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 2), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(4, 2), new DiskImpl(playerTwo));
    field.set(new CellImpl(4, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(5, 1), new DiskImpl(playerTwo));
    field.set(new CellImpl(5, 4), new DiskImpl(playerTwo));
    field.set(new CellImpl(7, 7), new DiskImpl(playerTwo));

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midGame_PlayerOnesTurn() {

    ModifiableGameField field = new GameFieldImpl();

    for (int column = 1; column < 6; column++) {
      field.set(new CellImpl(column, 4), new DiskImpl(playerOne));
    }
    field.set(new CellImpl(2, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(5, 3), new DiskImpl(playerOne));
    field.set(new CellImpl(4, 1), new DiskImpl(playerOne));

    for (int column = 1; column < 7; column++) {
      field.set(new CellImpl(column, 2), new DiskImpl(playerTwo));
    }
    field.set(new CellImpl(7, 1), new DiskImpl(playerTwo));
    field.set(new CellImpl(1, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 3), new DiskImpl(playerTwo));
    field.set(new CellImpl(3, 5), new DiskImpl(playerTwo));

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState earlyGame_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

    field.set(new CellImpl(4, 1), diskOne);
    field.set(new CellImpl(2, 2), diskOne);
    field.set(new CellImpl(3, 2), diskTwo);
    field.set(new CellImpl(4, 2), diskOne);
    field.set(new CellImpl(5, 2), diskTwo);
    field.set(new CellImpl(1, 3), diskTwo);
    field.set(new CellImpl(2, 3), diskTwo);
    field.set(new CellImpl(3, 3), diskTwo);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(5, 3), diskTwo);
    field.set(new CellImpl(2, 4), diskOne);
    field.set(new CellImpl(3, 4), diskOne);
    field.set(new CellImpl(4, 4), diskOne);

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midToLateGame_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

    for (int i = 0; i < 8; i++) {
      for (int j = 1; j < 3; j++) {
        field.set(new CellImpl(i, j), diskTwo);
      }
    }
    field.set(new CellImpl(4, 1), diskOne);
    field.set(new CellImpl(5, 1), diskOne);
    field.remove(new CellImpl(7, 2));
    for (int i = 2; i < 4; i++) {
      for (int j = 0; j < 8; j++) {
        field.set(new CellImpl(i, j), diskTwo);
      }
    }
    field.remove(new CellImpl(2, 0));
    field.set(new CellImpl(2, 5), diskOne);
    field.remove(new CellImpl(2, 7));

    field.set(new CellImpl(4, 0), diskTwo);
    field.set(new CellImpl(5, 0), diskOne);
    field.set(new CellImpl(1, 3), diskOne);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(5, 3), diskTwo);
    field.set(new CellImpl(1, 4), diskOne);
    field.set(new CellImpl(4, 4), diskOne);
    field.set(new CellImpl(5, 4), diskOne);
    field.set(new CellImpl(6, 4), diskOne);
    field.set(new CellImpl(7, 4), diskOne);
    field.set(new CellImpl(0, 5), diskTwo);
    field.set(new CellImpl(1, 5), diskTwo);
    field.set(new CellImpl(4, 5), diskOne);
    field.set(new CellImpl(4, 6), diskTwo);
    field.set(new CellImpl(1, 7), diskOne);
    field.set(new CellImpl(5, 7), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState playerTwoCantMoveGame_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

    for (int column = 2; column < 7; column++) {
      field.set(new CellImpl(column, 6), diskOne);
    }
    for (int column = 1; column < 8; column++) {
      field.set(new CellImpl(column, 7), diskOne);
    }
    field.set(new CellImpl(0, 6), diskTwo);
    field.set(new CellImpl(0, 7), diskTwo);
    field.set(new CellImpl(1, 6), diskTwo);
    field.set(new CellImpl(2, 5), diskTwo);

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  @Test
  public void testGetPossibleMovesForPlayer_earlyGamePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    for (int row = 2; row < 7; row++) {
      createdFakeList.add(new CellImpl(2, row));
    }
    helperGetPossibleMovesForPlayer(earlyGame_PlayerTwosTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_midGamePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    CellImpl cell1 = new CellImpl(0, 3);
    CellImpl cell2 = new CellImpl(0, 4);
    CellImpl cell3 = new CellImpl(0, 5);
    CellImpl cell4 = new CellImpl(1, 5);
    CellImpl cell5 = new CellImpl(1, 6);
    CellImpl cell6 = new CellImpl(2, 6);
    createdFakeList.add(cell1);
    createdFakeList.add(cell2);
    createdFakeList.add(cell3);
    createdFakeList.add(cell4);
    createdFakeList.add(cell5);
    createdFakeList.add(cell6);

    helperGetPossibleMovesForPlayer(midGame_PlayerTwosTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_MidToLatePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    CellImpl cell1 = new CellImpl(0, 2);
    CellImpl cell2 = new CellImpl(0, 3);
    CellImpl cell3 = new CellImpl(0, 4);
    CellImpl cell4 = new CellImpl(4, 1);
    CellImpl cell5 = new CellImpl(5, 2);

    createdFakeList.add(cell1);
    createdFakeList.add(cell2);
    createdFakeList.add(cell3);
    createdFakeList.add(cell4);
    createdFakeList.add(cell5);

    helperGetPossibleMovesForPlayer(midToLateGame_PlayerTwosTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_LateGamePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(7, 3));

    helperGetPossibleMovesForPlayer(veryLateGame_PlayerTwosTurn(), createdFakeList);
  }

  private void helperGetPossibleMovesForPlayer(ModifiableGameState methodState, Set<Cell> list) {
    ModelImpl model = new ModelImpl(methodState, GameMode.HOTSEAT);
    Set<Cell> actualList =
        model.getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer());
    Assertions.assertEquals(list, actualList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_LastMove06GamePlayerOne() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(0, 6));
    helperGetPossibleMovesForPlayer(lastMove06Game_PlayerOnesTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_MidGamePlayerOne() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(0, 1));
    createdFakeList.add(new CellImpl(1, 1));
    createdFakeList.add(new CellImpl(2, 1));
    createdFakeList.add(new CellImpl(3, 1));
    createdFakeList.add(new CellImpl(5, 1));
    createdFakeList.add(new CellImpl(6, 1));
    createdFakeList.add(new CellImpl(0, 2));
    createdFakeList.add(new CellImpl(0, 3));
    createdFakeList.add(new CellImpl(6, 3));
    createdFakeList.add(new CellImpl(2, 6));
    createdFakeList.add(new CellImpl(3, 6));
    createdFakeList.add(new CellImpl(4, 6));
    helperGetPossibleMovesForPlayer(midGame_PlayerOnesTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_earlyGamePlayerOne() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(3, 1));
    createdFakeList.add(new CellImpl(6, 1));
    createdFakeList.add(new CellImpl(0, 2));
    createdFakeList.add(new CellImpl(1, 2));
    createdFakeList.add(new CellImpl(6, 2));
    createdFakeList.add(new CellImpl(6, 3));
    createdFakeList.add(new CellImpl(0, 4));
    createdFakeList.add(new CellImpl(1, 4));
    createdFakeList.add(new CellImpl(6, 4));
    helperGetPossibleMovesForPlayer(earlyGame_PlayerOnesTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_midToLateGamePlayerOne() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(0, 0));
    createdFakeList.add(new CellImpl(1, 0));
    createdFakeList.add(new CellImpl(2, 0));
    createdFakeList.add(new CellImpl(7, 0));
    createdFakeList.add(new CellImpl(7, 2));
    createdFakeList.add(new CellImpl(6, 3));
    createdFakeList.add(new CellImpl(7, 3));
    createdFakeList.add(new CellImpl(0, 6));
    createdFakeList.add(new CellImpl(1, 6));
    createdFakeList.add(new CellImpl(2, 7));
    createdFakeList.add(new CellImpl(4, 7));
    helperGetPossibleMovesForPlayer(midToLateGame_PlayerOnesTurn(), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_PlayerTwoSkipMove() {
    ModelImpl model = new ModelImpl(playerTwoCantMoveGame_PlayerOnesTurn(), GameMode.HOTSEAT);
    GameFieldImpl field = (GameFieldImpl) model.getState().getField();
    field.set(
        new CellImpl(7, 6), new DiskImpl(model.getState().getPlayerManagement().getPlayerOne()));
    PlayerManagementImpl manager = (PlayerManagementImpl) model.getState().getPlayerManagement();
    manager.switchCurrentPlayer();
    Set<Cell> list = new HashSet<>();
    Assertions.assertEquals(
        list,
        model.getPossibleMovesForPlayer(model.getState().getPlayerManagement().getPlayerTwo()));
  }

  @Test
  public void testPlaceDisk_earlyFieldPlayerTwo_correctMove() {
    ModelImpl model = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    boolean move =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
            new CellImpl(2, 3));
    Assertions.assertTrue(move);

    GameFieldImpl field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);
    field.set(new CellImpl(2, 3), diskTwo);
    field.set(new CellImpl(3, 3), diskTwo);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(3, 4), diskTwo);
    field.set(new CellImpl(4, 4), diskTwo);
    field.set(new CellImpl(4, 5), diskTwo);
    field.set(new CellImpl(3, 5), diskOne);
    ModifiableGameState state = new GameStateImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);
    ModelImpl createdModel = new ModelImpl(state, GameMode.HOTSEAT);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  private void helperPlaceDisk_NoWinner(ModelImpl createdModel, ModelImpl model) {
    Assertions.assertEquals(createdModel.getState(), model.getState());
    Assertions.assertEquals(Optional.empty(), model.getState().getPlayerManagement().getWinner());
  }

  @Test
  public void testPlaceDisk_MidGamePlayerTwo_CorrectMove() {
    ModelImpl model = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    model.placeDisk(
        new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()), new CellImpl(0, 4));

    ModelImpl createdModel = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    GameFieldImpl field = (GameFieldImpl) createdModel.getState().getField();
    field.set(
        new CellImpl(0, 4),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerTwo()));
    field.set(
        new CellImpl(1, 4),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerTwo()));
    field.set(
        new CellImpl(2, 4),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerTwo()));
    field.set(
        new CellImpl(3, 4),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerTwo()));
    PlayerManagementImpl manager =
        (PlayerManagementImpl) createdModel.getState().getPlayerManagement();
    manager.switchCurrentPlayer();
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_MidGamePlayerTwo_IncorrectMove() {
    ModelImpl model = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    boolean move =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
            new CellImpl(0, 0));
    ModelImpl createdModel = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Assertions.assertFalse(move);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_InvalidCell() {
    ModelImpl model = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    try {
      boolean move =
          model.placeDisk(
              new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
              new CellImpl(-1, 0));
      Assertions.assertFalse(move);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof IllegalArgumentException);
    }
    ModelImpl createdModel = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_LastMoveAndOnPhaseFinished() {
    ModelImpl model = new ModelImpl(lastMove06Game_PlayerOnesTurn(), GameMode.HOTSEAT);
    boolean firstMove =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getPlayerOne()),
            new CellImpl(0, 6));
    Assertions.assertTrue(firstMove);
    boolean move =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(1, 6));
    boolean extraMove =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(5, 6));
    Assertions.assertFalse(move);
    Assertions.assertFalse(extraMove);
    ModelImpl createdModel = new ModelImpl(lastMove06Game_PlayerOnesTurn(), GameMode.HOTSEAT);
    GameFieldImpl field = (GameFieldImpl) createdModel.getState().getField();
    field.set(
        new CellImpl(0, 5),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(0, 6),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(1, 5),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(2, 4),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(3, 3),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(4, 2),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    field.set(
        new CellImpl(5, 1),
        new DiskImpl(createdModel.getState().getPlayerManagement().getPlayerOne()));
    PlayerManagementImpl manager =
        (PlayerManagementImpl) createdModel.getState().getPlayerManagement();
    manager.setWinner(Optional.of(createdModel.getState().getPlayerManagement().getPlayerOne()));

    Assertions.assertEquals(Phase.FINISHED, model.getState().getCurrentPhase());
    Assertions.assertEquals(
        createdModel.getState().getField().getCellsOccupiedWithDisks(),
        model.getState().getField().getCellsOccupiedWithDisks());
    Assertions.assertEquals(
        createdModel.getState().getPlayerManagement().getCurrentPlayer(),
        model.getState().getPlayerManagement().getCurrentPlayer());
    Assertions.assertEquals(
        createdModel.getState().getPlayerManagement().getWinner(),
        model.getState().getPlayerManagement().getWinner());
    Assertions.assertEquals(
        createdModel
            .getState()
            .getField()
            .getAllCellsForPlayer(createdModel.getState().getPlayerManagement().getPlayerOne()),
        model
            .getState()
            .getField()
            .getAllCellsForPlayer(model.getState().getPlayerManagement().getPlayerOne()));
    Assertions.assertEquals(
        createdModel.getPossibleMovesForPlayer(
            createdModel.getState().getPlayerManagement().getCurrentPlayer()),
        model.getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer()));
    Assertions.assertEquals(
        createdModel
            .getState()
            .getField()
            .getAllCellsForPlayer(createdModel.getState().getPlayerManagement().getPlayerTwo()),
        model
            .getState()
            .getField()
            .getAllCellsForPlayer(model.getState().getPlayerManagement().getPlayerTwo()));
  }

  @Test
  public void testPlaceDisk_NotCurrentPlayer_midToLateGamePlayerOne() {
    ModelImpl model = new ModelImpl(midToLateGame_PlayerOnesTurn(), GameMode.HOTSEAT);
    ModelImpl createdModel = new ModelImpl(midToLateGame_PlayerOnesTurn(), GameMode.HOTSEAT);
    boolean move =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
            new CellImpl(6, 0));
    Assertions.assertFalse(move);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_EmptyField() {
    ModifiableGameField field = new GameFieldImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setPlayerManagement(manager);
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    ModelImpl model = new ModelImpl(state, GameMode.HOTSEAT);
    boolean move = model.placeDisk(new DiskImpl(playerOne), new CellImpl(3, 3));
    Assertions.assertTrue(move);

    ModifiableGameField createdField = new GameFieldImpl();
    ModifiablePlayerManagement createdManager = new PlayerManagementImpl(playerOne, playerTwo);
    createdField.set(new CellImpl(3, 3), new DiskImpl(playerOne));
    createdManager.switchCurrentPlayer();
    ModifiableGameState createdState = new GameStateImpl();
    createdState.setPlayerManagement(createdManager);
    createdState.setGameField(createdField);
    createdState.setCurrentPhase(Phase.RUNNING);
    ModelImpl createdModel = new ModelImpl(createdState, GameMode.HOTSEAT);

    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_EmptyField_InvalidMove() {
    ModifiableGameField field = new GameFieldImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setPlayerManagement(manager);
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    ModelImpl model = new ModelImpl(state, GameMode.HOTSEAT);
    boolean move = model.placeDisk(new DiskImpl(playerOne), new CellImpl(7, 3));
    Assertions.assertFalse(move);

    ModifiableGameField createdField = new GameFieldImpl();
    ModifiablePlayerManagement createdManager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState createdState = new GameStateImpl();
    createdState.setPlayerManagement(createdManager);
    createdState.setGameField(createdField);
    createdState.setCurrentPhase(Phase.RUNNING);
    ModelImpl createdModel = new ModelImpl(createdState, GameMode.HOTSEAT);

    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testGetState() {
    ModelImpl game = new ModelImpl(midGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Assertions.assertEquals(midGame_PlayerTwosTurn(), game.getState());
  }

  // Dummy class to allow testing of add and remove PropertyChangeListener
  private class Pcldummy implements PropertyChangeListener {

    GameState state;

    Pcldummy() {
      // Do Nothing
    }

    // Needed to appease Spotbugs. Always returns true.
    @SuppressWarnings("unused")
    boolean dummyMethod() {
      GameState temp = earlyGame_PlayerTwosTurn();
      if (temp.getCurrentPhase().equals(Phase.RUNNING)) {
        return true;
      } else {
        return false;
      }
    }

    GameState getEventState() {
      return state;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getNewValue() instanceof GameState) {
        state = (GameState) evt.getNewValue();
      }
    }
  }

  @Test
  public void testAddListener_null() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    try {
      game.addListener(null);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof NullPointerException);
      return;
    }
    Assertions.fail();
  }

  @Test
  public void testAddListener_notNull() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Pcldummy dummy = new Pcldummy();
    game.addListener(dummy);
    game.placeDisk(
        new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()), new CellImpl(2, 2));
    Assertions.assertNotNull(dummy.getEventState());
  }

  @Test
  public void testRemoveListener_null() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    try {
      game.removeListener(null);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof NullPointerException);
      return;
    }
    Assertions.fail();
  }

  @Test
  public void testRemoveListener_notNull() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Pcldummy dummy = new Pcldummy();
    game.addListener(dummy);
    game.removeListener(dummy);
    game.placeDisk(
        new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()), new CellImpl(2, 2));
    Assertions.assertNull(dummy.getEventState());
  }

  @Test
  public void testSetWaiting_gameRunning() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Assertions.assertTrue(game.setWaiting());
    Assertions.assertEquals(Phase.WAITING, game.getState().getCurrentPhase());
  }

  @Test
  public void testSetWaiting_gameWaiting() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    game.setWaiting();
    Assertions.assertFalse(game.setWaiting());
    Assertions.assertEquals(Phase.WAITING, game.getState().getCurrentPhase());
  }

  @Test
  public void testSetWaiting_gameFinished() {
    ModifiableGameState state = earlyGame_PlayerTwosTurn();
    state.setCurrentPhase(Phase.FINISHED);
    ModelImpl game = new ModelImpl(state, GameMode.HOTSEAT);
    Assertions.assertFalse(game.setWaiting());
    Assertions.assertEquals(Phase.FINISHED, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameWaiting() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    game.setWaiting();
    Assertions.assertTrue(game.unsetWaiting());
    Assertions.assertEquals(Phase.RUNNING, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameRunning() {
    ModelImpl game = new ModelImpl(earlyGame_PlayerTwosTurn(), GameMode.HOTSEAT);
    Assertions.assertFalse(game.unsetWaiting());
    Assertions.assertEquals(Phase.RUNNING, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameFinished() {
    ModifiableGameState state = earlyGame_PlayerTwosTurn();
    state.setCurrentPhase(Phase.FINISHED);
    ModelImpl game = new ModelImpl(state, GameMode.HOTSEAT);
    Assertions.assertFalse(game.unsetWaiting());
    Assertions.assertEquals(Phase.FINISHED, game.getState().getCurrentPhase());
  }

  @Test
  public void testPlacementOfFirstDisks() {
    Set<Cell> expectedMoves = new HashSet<>();
    expectedMoves.add(new CellImpl(3, 3));
    expectedMoves.add(new CellImpl(3, 4));
    expectedMoves.add(new CellImpl(4, 3));
    expectedMoves.add(new CellImpl(4, 4));

    ModelImpl game = new ModelImpl(GameMode.HOTSEAT, playerOne, playerTwo);

    helperGetPossibleMovesForPlayer((ModifiableGameState) game.getState(), expectedMoves);

    Assertions.assertTrue(
        game.placeDisk(
            new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(3, 3)));
    expectedMoves.remove(new CellImpl(3, 3));
    helperGetPossibleMovesForPlayer((ModifiableGameState) game.getState(), expectedMoves);

    Assertions.assertTrue(
        game.placeDisk(
            new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(3, 4)));
    expectedMoves.remove(new CellImpl(3, 4));
    helperGetPossibleMovesForPlayer((ModifiableGameState) game.getState(), expectedMoves);

    Assertions.assertTrue(
        game.placeDisk(
            new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(4, 3)));
    expectedMoves.remove(new CellImpl(4, 3));
    helperGetPossibleMovesForPlayer((ModifiableGameState) game.getState(), expectedMoves);

    Assertions.assertTrue(
        game.placeDisk(
            new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()),
            new CellImpl(4, 4)));
  }

  @Test
  public void testSubstitutePlayersWith() {
    Model game = new ModelImpl(midGame_PlayerOnesTurn(), GameMode.HOTSEAT);
    final GameState stateBefore = game.getState();
    final GameField fieldBefore = stateBefore.getField().makeCopy();
    final Player playerOneBefore = stateBefore.getPlayerManagement().getPlayerOne();
    final Player playerTwoBefore = stateBefore.getPlayerManagement().getPlayerTwo();
    final Set<Cell> cellsPlayerOneBefore = fieldBefore.getAllCellsForPlayer(playerOneBefore);
    final Set<Cell> cellsPlayerTwoBefore = fieldBefore.getAllCellsForPlayer(playerTwoBefore);

    Player newPlayerOne = new PlayerImpl("New 1", Color.CORNFLOWERBLUE);
    Player newPlayerTwo = new PlayerImpl("New 2", Color.LIGHTGOLDENRODYELLOW);
    game.substitutePlayersWith(newPlayerOne, newPlayerTwo);

    GameField newField = game.getState().getField();
    Assertions.assertEquals(
        newPlayerOne,
        game.getState().getPlayerManagement().getCurrentPlayer(),
        "Current player should still be first player after player substitution");
    Assertions.assertEquals(cellsPlayerOneBefore, newField.getAllCellsForPlayer(newPlayerOne));
    Assertions.assertEquals(cellsPlayerTwoBefore, newField.getAllCellsForPlayer(newPlayerTwo));
    newField
        .getCellsOccupiedWithDisks()
        .forEach(
            (cell, player) -> {
              if (!playerOneBefore.equals(newPlayerOne)) {
                Assertions.assertNotSame(
                    playerOneBefore,
                    player,
                    "After substitution no reference to old players should remain on game field");
                Assertions.assertNotEquals(playerOneBefore, newField.get(cell).get());
                // get() is safe if getCellsOccupiedWithDisks() works, which can be assumed here
              }
              if (!playerTwoBefore.equals(newPlayerTwo)) {
                Assertions.assertNotSame(
                    playerTwoBefore,
                    player,
                    "After substitution no reference to old players should remain on game field");
                Assertions.assertNotEquals(playerTwoBefore, newField.get(cell).get());
                // get() is safe if getCellsOccupiedWithDisks() works, which can be assumed here
              }
            });
    Assertions.assertEquals(
        fieldBefore.getCellsOccupiedWithDisks().size(),
        newField.getCellsOccupiedWithDisks().size(),
        "Same amount of disks should be on the bord before and after the player substitution");
  }
}
