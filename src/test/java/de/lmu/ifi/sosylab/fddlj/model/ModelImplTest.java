package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Test;

public class ModelImplTest {

  private ModifiableGameState earlyGame_PlayerTwosTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
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
    return state;
  }

  private ModifiableGameState midGame_PlayerTwosTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(one);
    Disk diskTwo = new DiskImpl(two);

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
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl();
    Disk diskOne = new DiskImpl(one);
    Disk diskTwo = new DiskImpl(two);

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
    field.set(new CellImpl(4, 7), diskTwo);
    field.set(new CellImpl(5, 7), diskTwo);
    field.set(new CellImpl(6, 7), diskTwo);

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midToLateGame_playerTwosTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    manager.switchCurrentPlayer();
    ModifiableGameField field = new GameFieldImpl();
    field.set(new CellImpl(0, 7), new DiskImpl(one));
    field.set(new CellImpl(0, 6), new DiskImpl(one));
    field.set(new CellImpl(0, 5), new DiskImpl(one));
    field.set(new CellImpl(1, 7), new DiskImpl(one));
    field.set(new CellImpl(1, 6), new DiskImpl(one));
    field.set(new CellImpl(1, 5), new DiskImpl(one));
    field.set(new CellImpl(1, 4), new DiskImpl(one));
    field.set(new CellImpl(1, 3), new DiskImpl(one));
    field.set(new CellImpl(2, 4), new DiskImpl(one));
    field.set(new CellImpl(3, 3), new DiskImpl(one));
    field.set(new CellImpl(4, 4), new DiskImpl(one));
    field.set(new CellImpl(4, 3), new DiskImpl(one));
    field.set(new CellImpl(4, 2), new DiskImpl(one));
    field.set(new CellImpl(0, 1), new DiskImpl(two));
    field.set(new CellImpl(1, 2), new DiskImpl(two));
    field.set(new CellImpl(2, 2), new DiskImpl(two));
    field.set(new CellImpl(2, 3), new DiskImpl(two));
    field.set(new CellImpl(3, 1), new DiskImpl(two));
    field.set(new CellImpl(3, 2), new DiskImpl(two));
    field.set(new CellImpl(2, 5), new DiskImpl(two));
    field.set(new CellImpl(2, 6), new DiskImpl(two));
    field.set(new CellImpl(3, 4), new DiskImpl(two));
    field.set(new CellImpl(3, 5), new DiskImpl(two));
    field.set(new CellImpl(3, 6), new DiskImpl(two));
    field.set(new CellImpl(4, 5), new DiskImpl(two));
    field.set(new CellImpl(4, 6), new DiskImpl(two));
    field.set(new CellImpl(5, 3), new DiskImpl(two));
    field.set(new CellImpl(5, 4), new DiskImpl(two));
    field.set(new CellImpl(5, 5), new DiskImpl(two));
    field.set(new CellImpl(6, 4), new DiskImpl(two));
    field.set(new CellImpl(6, 5), new DiskImpl(two));
    field.set(new CellImpl(7, 4), new DiskImpl(two));

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState lastMove06Game_PlayerOnesTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    ModifiableGameField field = new GameFieldImpl();

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(0, row), new DiskImpl(one));
    }

    for (int column = 1; column < 3; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), new DiskImpl(one));
      }
    }
    for (int column = 2; column < 8; column++) {
      field.set(new CellImpl(column, 0), new DiskImpl(one));
    }
    for (int row = 1; row < 7; row++) {
      field.set(new CellImpl(7, row), new DiskImpl(one));
    }
    field.set(new CellImpl(3, 1), new DiskImpl(one));
    field.set(new CellImpl(4, 1), new DiskImpl(one));
    field.set(new CellImpl(6, 1), new DiskImpl(one));
    field.set(new CellImpl(2, 2), new DiskImpl(one));
    field.set(new CellImpl(5, 2), new DiskImpl(one));
    field.set(new CellImpl(5, 3), new DiskImpl(one));
    field.set(new CellImpl(3, 4), new DiskImpl(one));
    field.set(new CellImpl(4, 4), new DiskImpl(one));
    field.set(new CellImpl(5, 5), new DiskImpl(one));
    field.set(new CellImpl(6, 6), new DiskImpl(one));
    field.set(new CellImpl(6, 7), new DiskImpl(one));
    field.set(new CellImpl(0, 7), new DiskImpl(one));

    for (int row = 0; row < 5; row++) {
      field.set(new CellImpl(1, row), new DiskImpl(two));
    }
    for (int column = 0; column < 5; column++) {
      field.set(new CellImpl(column, 5), new DiskImpl(two));
    }
    for (int column = 3; column < 6; column++) {
      for (int row = 6; row < 8; row++) {
        field.set(new CellImpl(column, row), new DiskImpl(two));
      }
    }
    for (int row = 2; row < 6; row++) {
      field.set(new CellImpl(6, row), new DiskImpl(two));
    }
    field.set(new CellImpl(2, 1), new DiskImpl(two));
    field.set(new CellImpl(2, 3), new DiskImpl(two));
    field.set(new CellImpl(2, 4), new DiskImpl(two));
    field.set(new CellImpl(3, 2), new DiskImpl(two));
    field.set(new CellImpl(3, 3), new DiskImpl(two));
    field.set(new CellImpl(4, 2), new DiskImpl(two));
    field.set(new CellImpl(4, 3), new DiskImpl(two));
    field.set(new CellImpl(5, 1), new DiskImpl(two));
    field.set(new CellImpl(5, 4), new DiskImpl(two));
    field.set(new CellImpl(7, 7), new DiskImpl(two));

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midGame_PlayerOnesTurn() {
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    ModifiableGameField field = new GameFieldImpl();

    for (int column = 1; column < 6; column++) {
      field.set(new CellImpl(column, 4), new DiskImpl(one));
    }
    field.set(new CellImpl(2, 3), new DiskImpl(one));
    field.set(new CellImpl(4, 3), new DiskImpl(one));
    field.set(new CellImpl(5, 3), new DiskImpl(one));
    field.set(new CellImpl(4, 1), new DiskImpl(one));

    for (int column = 1; column < 7; column++) {
      field.set(new CellImpl(column, 2), new DiskImpl(two));
    }
    field.set(new CellImpl(7, 1), new DiskImpl(two));
    field.set(new CellImpl(1, 3), new DiskImpl(two));
    field.set(new CellImpl(3, 3), new DiskImpl(two));
    field.set(new CellImpl(3, 5), new DiskImpl(two));

    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState earlyGame_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    Disk diskOne = new DiskImpl(one);
    Disk diskTwo = new DiskImpl(two);

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

    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  private ModifiableGameState midToLateGame_PlayerOnesTurn() {
    ModifiableGameField field = new GameFieldImpl();
    Player one = new PlayerImpl("Tina", Color.ANTIQUEWHITE);
    Player two = new PlayerImpl("Rhea", Color.ALICEBLUE);
    Disk diskOne = new DiskImpl(one);
    Disk diskTwo = new DiskImpl(two);

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
    ModifiablePlayerManagement manager = new PlayerManagementImpl(one, two);
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }
}
