package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

public class GameStateUtility {
  static ModifiableGameState earlyGame_PlayerTwosTurn(Player playerOne, Player playerTwo) {

    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);
    ModifiableGameField field = new GameFieldImpl(8);
    field.set(new CellImpl(3, 3), diskOne);
    field.set(new CellImpl(3, 4), diskOne);
    field.set(new CellImpl(3, 5), diskOne);
    field.set(new CellImpl(4, 3), diskTwo);
    field.set(new CellImpl(4, 4), diskTwo);
    field.set(new CellImpl(4, 5), diskTwo);

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();

    ModifiableGameState state = new GameStateImpl();
    state.setGameField(field);
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(manager);
    return state;
  }

  static ModifiableGameState midGame_PlayerTwosTurn(Player playerOne, Player playerTwo) {

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl(8);
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

  static ModifiableGameState veryLateGame_PlayerTwosTurn(Player playerOne, Player playerTwo) {

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();

    ModifiableGameField field = new GameFieldImpl(8);
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

  static ModifiableGameState midToLateGame_PlayerTwosTurn(Player playerOne, Player playerTwo) {

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    manager.switchCurrentPlayer();
    ModifiableGameField field = new GameFieldImpl(8);
    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);
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

  static ModifiableGameState lastMove06Game_PlayerOnesTurn(Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);

    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

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

    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    ModifiableGameState state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }

  static ModifiableGameState finishedGame_PlayerTwoWins(Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);

    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

    for (int column = 0; column < 7; column++) {
      for (int row = 0; row < 8; row++) {
        field.set(new CellImpl(column, row), diskTwo);
      }
    }
    for (int row = 0; row < 8; row++) {
      field.set(new CellImpl(7, row), diskOne);
    }

    ModifiableGameState state = new GameStateImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setCurrentPhase(Phase.FINISHED);
    state.setGameField(field);
    state.setPlayerManagement(manager);
    state.getPlayerManagement().setWinner(Optional.of(playerTwo));

    return state;
  }

  static ModifiableGameState playerTwoDisksInCorners(Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);

    Disk diskOne = new DiskImpl(playerOne);
    Disk diskTwo = new DiskImpl(playerTwo);

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
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);
    manager.switchCurrentPlayer();

    return state;
  }

  static ModifiableGameState midGame_PlayerOnesTurn(Player playerOne, Player playerTwo) {

    ModifiableGameField field = new GameFieldImpl(8);

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

  static ModifiableGameState earlyGame_PlayerOnesTurn(Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);
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

  static ModifiableGameState midToLateGame_PlayerOnesTurn(Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);
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

  static ModifiableGameState playerTwoCantMoveGame_PlayerOnesTurn(
      Player playerOne, Player playerTwo) {
    ModifiableGameField field = new GameFieldImpl(8);

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

    ModifiableGameState state = new GameStateImpl();
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setCurrentPhase(Phase.RUNNING);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    return state;
  }
}
