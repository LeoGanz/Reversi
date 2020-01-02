package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

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
}
