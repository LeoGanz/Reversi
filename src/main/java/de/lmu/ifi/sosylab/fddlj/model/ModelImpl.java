package de.lmu.ifi.sosylab.fddlj.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

public class ModelImpl implements Model {

  private final PropertyChangeSupport support;

  private ModifiableGameState state;
  private GameMode mode;
  // private AI ai;

  // HotSeat and Single
  public ModelImpl(GameMode mode) {}

  // Multiplayer
  public ModelImpl(Player thisClient, PlayerManagement manager) {}

  @Override
  public boolean placeDisk(Disk disk, Cell cell) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Set<Cell> getPossibleMovesForPlayer(Player player) {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return null;
  }

  private boolean checkIfLegalMove(Disk disk, Cell cell) {

    if (!(disk.getPlayer() == state.getPlayerManagement().getCurrentPlayer())) {
      return false;
    }
    if (state.getField().get(cell).isPresent()) {
      return false;
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
}
