package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ModelImplTest {

  private final Player playerOne = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
  private final Player playerTwo = new PlayerImpl("Rhea", Color.ALICEBLUE);

  @Test
  public void testGetPossibleMovesForPlayer_earlyGamePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    for (int row = 2; row < 7; row++) {
      createdFakeList.add(new CellImpl(2, row));
    }
    helperGetPossibleMovesForPlayer(
        GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), createdFakeList);
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

    helperGetPossibleMovesForPlayer(
        GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), createdFakeList);
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

    helperGetPossibleMovesForPlayer(
        GameStateUtility.midToLateGame_PlayerTwosTurn(playerOne, playerTwo), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_LateGamePlayerTwo() {
    Set<Cell> createdFakeList = new HashSet<>();
    createdFakeList.add(new CellImpl(7, 3));

    helperGetPossibleMovesForPlayer(
        GameStateUtility.veryLateGame_PlayerTwosTurn(playerOne, playerTwo), createdFakeList);
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
    helperGetPossibleMovesForPlayer(
        GameStateUtility.lastMove06Game_PlayerOnesTurn(playerOne, playerTwo), createdFakeList);
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
    helperGetPossibleMovesForPlayer(
        GameStateUtility.midGame_PlayerOnesTurn(playerOne, playerTwo), createdFakeList);
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
    helperGetPossibleMovesForPlayer(
        GameStateUtility.earlyGame_PlayerOnesTurn(playerOne, playerTwo), createdFakeList);
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
    helperGetPossibleMovesForPlayer(
        GameStateUtility.midToLateGame_PlayerOnesTurn(playerOne, playerTwo), createdFakeList);
  }

  @Test
  public void testGetPossibleMovesForPlayer_PlayerTwoSkipMove() {
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.playerTwoCantMoveGame_PlayerOnesTurn(playerOne, playerTwo),
            GameMode.HOTSEAT);
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
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    model.placeDisk(
        new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()), new CellImpl(0, 4));

    ModelImpl createdModel =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    boolean move =
        model.placeDisk(
            new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
            new CellImpl(0, 0));
    ModelImpl createdModel =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Assertions.assertFalse(move);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_InvalidCell() {
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    try {
      boolean move =
          model.placeDisk(
              new DiskImpl(model.getState().getPlayerManagement().getPlayerTwo()),
              new CellImpl(-1, 0));
      Assertions.assertFalse(move);
    } catch (Exception e) {
      Assertions.assertTrue(e instanceof IllegalArgumentException);
    }
    ModelImpl createdModel =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    helperPlaceDisk_NoWinner(createdModel, model);
  }

  @Test
  public void testPlaceDisk_LastMoveAndOnPhaseFinished() {
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.lastMove06Game_PlayerOnesTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl createdModel =
        new ModelImpl(
            GameStateUtility.lastMove06Game_PlayerOnesTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl model =
        new ModelImpl(
            GameStateUtility.midToLateGame_PlayerOnesTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    ModelImpl createdModel =
        new ModelImpl(
            GameStateUtility.midToLateGame_PlayerOnesTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Assertions.assertEquals(
        GameStateUtility.midGame_PlayerTwosTurn(playerOne, playerTwo), game.getState());
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
      GameState temp = GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo);
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
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Pcldummy dummy = new Pcldummy();
    game.addListener(dummy);
    game.placeDisk(
        new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()), new CellImpl(2, 2));
    Assertions.assertNotNull(dummy.getEventState());
  }

  @Test
  public void testRemoveListener_null() {
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
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
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Pcldummy dummy = new Pcldummy();
    game.addListener(dummy);
    game.removeListener(dummy);
    game.placeDisk(
        new DiskImpl(game.getState().getPlayerManagement().getCurrentPlayer()), new CellImpl(2, 2));
    Assertions.assertNull(dummy.getEventState());
  }

  @Test
  public void testSetWaiting_gameRunning() {
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Assertions.assertTrue(game.setWaiting());
    Assertions.assertEquals(Phase.WAITING, game.getState().getCurrentPhase());
  }

  @Test
  public void testSetWaiting_gameWaiting() {
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    game.setWaiting();
    Assertions.assertFalse(game.setWaiting());
    Assertions.assertEquals(Phase.WAITING, game.getState().getCurrentPhase());
  }

  @Test
  public void testSetWaiting_gameFinished() {
    ModifiableGameState state = GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo);
    state.setCurrentPhase(Phase.FINISHED);
    ModelImpl game = new ModelImpl(state, GameMode.HOTSEAT);
    Assertions.assertFalse(game.setWaiting());
    Assertions.assertEquals(Phase.FINISHED, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameWaiting() {
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    game.setWaiting();
    Assertions.assertTrue(game.unsetWaiting());
    Assertions.assertEquals(Phase.RUNNING, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameRunning() {
    ModelImpl game =
        new ModelImpl(
            GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo), GameMode.HOTSEAT);
    Assertions.assertFalse(game.unsetWaiting());
    Assertions.assertEquals(Phase.RUNNING, game.getState().getCurrentPhase());
  }

  @Test
  public void testUnsetWaiting_gameFinished() {
    ModifiableGameState state = GameStateUtility.earlyGame_PlayerTwosTurn(playerOne, playerTwo);
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
  public void testGame_PlayerTwoIsAi() {
    Assertions.assertTimeout(
        Duration.ofSeconds(10),
        () -> {
          Random r = new Random();
          AiPlayer ai = new AiPlayerImpl();
          ModelImpl game = new ModelImpl(GameMode.SINGLEPLAYER, playerOne, ai);

          while (game.getState().getCurrentPhase().equals(Phase.RUNNING)) {
            Set<Cell> moves = game.getPossibleMovesForPlayer(playerOne);
            int elem = r.nextInt(moves.size());
            for (Cell c : moves) {
              elem -= 1;
              if (elem <= 0) {
                game.placeDisk(new DiskImpl(playerOne), c);
                break;
              }
            }
          }
          Assertions.assertEquals(Phase.FINISHED, game.getState().getCurrentPhase());
        });
  }
}
