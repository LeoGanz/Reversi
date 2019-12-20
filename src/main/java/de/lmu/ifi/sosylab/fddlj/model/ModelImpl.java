package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;

/** @author Daniel Leidreidter, Dora Pruteanu */
public class ModelImpl implements Model {

  private final PropertyChangeSupport support;

  private ModifiableGameState state;
  private GameMode mode;
  // private AI ai;

  // HotSeat and Single
  public ModelImpl(GameMode mode, PlayerManagement manager) {
    state = new GameStateImpl();
    state.setGameField(new GameFieldImpl());
    state.setCurrentPhase(Phase.RUNNING);
    this.mode = mode;
    state.setPlayerManagement(manager);
  }

  // Multiplayer
  public ModelImpl(Player thisClient, PlayerManagement manager) {
    state = new GameStateImpl();
    state.setPlayerManagement(manager);
    if (manager.getCurrentPlayer() == thisClient) {
      state.setCurrentPhase(Phase.WAITING);
    } else {
      state.setCurrentPhase(Phase.RUNNING);
    }
    state.setGameField(new GameFieldImpl());
    this.mode = GameMode.MULTIPLAYER;
  }

  @Override
  public boolean placeDisk(Disk disk, Cell cell) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<Cell> getPossibleMovesForPlayer(Player player) {
    Set<Cell> listOfMoves = new HashSet<>();
    // TODO Change 8 to GameFieldImpl.SIZE
    for (int column = 0; column < 8; column++) {
      for (int row = 0; row < 8; row++) {
        Cell checkedCell = new CellImpl();
        Disk disk = new DiskImpl(state.getPlayerManagement().getCurrentPlayer());
        if (checkIfLegalMove(disk, checkedCell)) {
          listOfMoves.add(checkedCell);
        }
      }
    }
    return listOfMoves;
  }

  @Override
  public void addListener(PropertyChangeListener pcl) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListener(PropertyChangeListener pcl) {
    // TODO Auto-generated method stub

  }

  @Override
  public GameState getState() {
    return state;
  }

  private boolean checkIfLegalMove(Disk disk, Cell cell) {

    if (!(disk.getPlayer() == state.getPlayerManagement().getCurrentPlayer())) {
      return false;
    }
    if (state.getField().get(cell).isPresent()) {
      return false;
    }
    if (beginningMove(cell)) {
      return true;
    }
    Player opponentPlayer =
        (state.getPlayerManagement().getCurrentPlayer()
                == state.getPlayerManagement().getPlayerOne())
            ? state.getPlayerManagement().getPlayerTwo()
            : state.getPlayerManagement().getPlayerOne();

    for (int x = -1; x < 2; x++) {
      for (int y = -1; y < 2; y++) {
        Cell cellChecked = new CellImpl(cell.getColumn() + x, cell.getRow() + y);
        if (state.getField().isWithinBounds(cellChecked)
            && (state.getField().isCellOfPlayer(opponentPlayer, cellChecked))) {
          int xcoor = cellChecked.getColumn();
          int ycoor = cellChecked.getRow();
          Cell opponentCell;
          do {
            xcoor = xcoor + x;
            ycoor = ycoor + y;
            opponentCell = new CellImpl(xcoor, ycoor);

          } while (state.getField().isWithinBounds(opponentCell)
              && (state.getField().isCellOfPlayer(opponentPlayer, opponentCell)));

          if (state.getField().isWithinBounds(opponentCell)
              && state
                  .getField()
                  .isCellOfPlayer(state.getPlayerManagement().getCurrentPlayer(), opponentCell)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void notifyListeners() {}

  private boolean beginningMove(Cell cell) {
    if (state.getField().getCellsOccupiedWithDisks().size() < 5) {
      if (cell.getRow() > 2
          && cell.getRow() < 5
          && cell.getColumn() > 2
          && cell.getColumn() < 5
          && state.getField().get(cell).isEmpty()) {
        return true;
      }
    }
    return false;
  }
}
