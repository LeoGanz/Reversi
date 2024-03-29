package de.lmu.ifi.sosylab.fddlj.model;

import static java.util.Objects.requireNonNull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * A concrete Implementation of the {@link Model} interface.
 *
 * @author Daniel Leidreiter, Dora Pruteanu
 */
public class ModelImpl implements Model {

  private final PropertyChangeSupport support;
  private static final int AI_MOVE_OFFSET = 750;
  private static final int AI_MAX_SLEEP_AMOUNT = 1700;

  private ModifiableGameState state;
  private GameMode mode;
  private ArtificialIntelligence ai;
  private static final int AI_DEPTH = 3;
  private static final int NUMBER_OF_MOVES_IN_BEGINNING_PHASE = 4;
  private final int disksPerPlayer;
  private int numberOfPlayerOneDisks;
  private int numberOfPlayerTwoDisks;

  /**
   * Creates a new game with a given {@link GameMode} and two given {@link Player}.
   *
   * @param mode the game shall be played in.
   * @param playerOne who will play in this game.
   * @param playerTwo who will play in this game.
   */
  public ModelImpl(GameMode mode, Player playerOne, Player playerTwo) {
    this(mode, GameField.STANDARD_SIZE, playerOne, playerTwo);
  }

  /**
   * Creates a new game with a given {@link GameMode}, a given size for the {@link GameField} and
   * two given {@link Player}.
   *
   * @param mode the game shall be played in.
   * @param fieldSize the GameField will be initiated with.
   * @param playerOne who will play in this game.
   * @param playerTwo who will play in this game.
   */
  public ModelImpl(GameMode mode, int fieldSize, Player playerOne, Player playerTwo) {
    support = new PropertyChangeSupport(this);

    this.mode = mode;

    state = new GameStateImpl();
    state.setGameField(new GameFieldImpl(fieldSize));
    state.setCurrentPhase(Phase.RUNNING);
    ModifiablePlayerManagement manager = new PlayerManagementImpl(playerOne, playerTwo);
    state.setPlayerManagement(manager);

    disksPerPlayer = ((int) Math.pow(state.getField().getSize(), 2)) / 2;
    numberOfPlayerOneDisks = disksPerPlayer;
    numberOfPlayerTwoDisks = disksPerPlayer;

    if (mode.equals(GameMode.SINGLEPLAYER)) {
      ai = new ArtificialIntelligenceImpl(AI_DEPTH, new HeuristicImpl());
    }
  }

  /**
   * Creates a new game with a deep copy of the given {@link GameState} and {@link GameMode}.
   *
   * @param newState with which the game shall be initialized.
   * @param mode with which the game shall be initialized.
   */
  public ModelImpl(GameState newState, GameMode mode) {
    support = new PropertyChangeSupport(this);

    state = (ModifiableGameState) newState.makeCopy();
    this.mode = mode;

    disksPerPlayer = ((int) Math.pow(state.getField().getSize(), 2)) / 2;
    numberOfPlayerOneDisks = disksPerPlayer;
    numberOfPlayerTwoDisks = disksPerPlayer;

    if (mode.equals(GameMode.SINGLEPLAYER)) {
      ai = new ArtificialIntelligenceImpl(AI_DEPTH, new HeuristicImpl());
    }
  }

  @Override
  public boolean placeDisk(Disk disk, Cell cell) {
    if (!state.getCurrentPhase().equals(Phase.RUNNING)) {
      return false;
    }

    if (checkIfLegalMove(disk, cell)) {
      ModifiableGameField field = state.getField();
      field.set(cell, disk);
      turnDisks(cell);

      if (state
          .getPlayerManagement()
          .getCurrentPlayer()
          .equals(state.getPlayerManagement().getPlayerOne())) {
        numberOfPlayerOneDisks--;
        if (numberOfPlayerTwoDisks <= 0) {
          state.setCurrentPhase(Phase.FINISHED);
          handleWinner();
        }
      } else {
        numberOfPlayerTwoDisks--;
        if (numberOfPlayerOneDisks <= 0) {
          state.setCurrentPhase(Phase.FINISHED);
          handleWinner();
        }
      }

      if (state.getCurrentPhase().equals(Phase.RUNNING)) {
        ModifiablePlayerManagement manager = state.getPlayerManagement();
        manager.switchCurrentPlayer();
        if (getPossibleMovesForPlayer(manager.getCurrentPlayer()).isEmpty()) {
          manager.switchCurrentPlayer();
          state.setCurrentPhase(Phase.FINISHED);
          handleWinner();
        }
      }

      notifyListenersOfChangedState();
      triggerAiMove();

      return true;

    } else {
      return false;
    }
  }

  @Override
  public Set<Cell> getPossibleMovesForPlayer(Player player) {
    Set<Cell> listOfMoves = new HashSet<>();
    int size = state.getField().getSize();
    for (int column = 0; column < size; column++) {
      for (int row = 0; row < size; row++) {
        Cell checkedCell = new CellImpl(column, row);
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
    requireNonNull(pcl);
    support.addPropertyChangeListener(pcl);
    notifyListenersOfChangedListeners();
  }

  @Override
  public void removeListener(PropertyChangeListener pcl) {
    requireNonNull(pcl);
    support.removePropertyChangeListener(pcl);
    notifyListenersOfChangedListeners();
  }

  @Override
  public GameState getState() {
    return state;
  }

  @Override
  public boolean setWaiting() {
    if (state.getCurrentPhase().equals(Phase.RUNNING)) {
      state.setCurrentPhase(Phase.WAITING);
      notifyListenersOfChangedState();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean unsetWaiting() {
    if (state.getCurrentPhase().equals(Phase.WAITING)) {
      state.setCurrentPhase(Phase.RUNNING);
      notifyListenersOfChangedState();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void substitutePlayersWith(Player newPlayerOne, Player newPlayerTwo) {
    Set<Cell> cellsPlayerOne =
        state.getField().getAllCellsForPlayer(state.getPlayerManagement().getPlayerOne());
    Set<Cell> cellsPlayerTwo =
        state.getField().getAllCellsForPlayer(state.getPlayerManagement().getPlayerTwo());

    cellsPlayerOne.stream().forEach(c -> state.getField().set(c, new DiskImpl(newPlayerOne)));
    cellsPlayerTwo.stream().forEach(c -> state.getField().set(c, new DiskImpl(newPlayerTwo)));

    state.getPlayerManagement().setPlayerOne(newPlayerOne);
    state.getPlayerManagement().setPlayerTwo(newPlayerTwo);
    notifyListenersOfChangedState();
  }

  /**
   * Turns all Disks belonging to the opponent lying between the placed Disk and another Disk of the
   * same Player.
   *
   * @param cell at which a disk was recently placed. The cell must not be empty.
   */
  private void turnDisks(Cell cell) {

    assert state.getField().get(cell).isPresent();
    Player player = state.getField().get(cell).get().getPlayer();

    Player opponentPlayer = state.getPlayerManagement().getOpponentPlayer(player);

    for (int x = -1; x < 2; x++) {
      for (int y = -1; y < 2; y++) {
        if (!((x == 0) && (y == 0))) {
          Cell cellChecked = new CellImpl(cell.getColumn() + x, cell.getRow() + y);
          if (state.getField().isWithinBounds(cellChecked)
              && (state.getField().isCellOfPlayer(opponentPlayer, cellChecked))) {
            Set<Cell> toTurn = new HashSet<>();
            int xcoor = cellChecked.getColumn();
            int ycoor = cellChecked.getRow();
            Cell opponentCell = new CellImpl(xcoor, ycoor);

            while (state.getField().isWithinBounds(opponentCell)
                && (state.getField().isCellOfPlayer(opponentPlayer, opponentCell))) {
              toTurn.add(opponentCell);
              xcoor = xcoor + x;
              ycoor = ycoor + y;
              opponentCell = new CellImpl(xcoor, ycoor);
            }

            if (state.getField().isWithinBounds(opponentCell)
                && state.getField().isCellOfPlayer(player, opponentCell)) {
              for (Cell c : toTurn) {
                ModifiableGameField field = state.getField();
                field.set(c, new DiskImpl(player));
              }
            }
          }
        }
      }
    }
  }

  /**
   * Checks whether a given {@link Disk} can be placed at a given {@link Cell} according to the
   * Reversi rules.
   *
   * @param disk which is tested for whether it can be placed.
   * @param cell which is tested for whether the disk can be placed here.
   * @return {@code true} if the disk can be placed according to the rules, {@code false} otherwise.
   */
  private boolean checkIfLegalMove(Disk disk, Cell cell) {

    if (!(disk.getPlayer().equals(state.getPlayerManagement().getCurrentPlayer()))) {
      return false;
    }
    if (state.getField().get(cell).isPresent()) {
      return false;
    }
    if (state.getField().getCellsOccupiedWithDisks().size() < NUMBER_OF_MOVES_IN_BEGINNING_PHASE) {
      return isWithinMiddleSquare(cell);
    }

    PlayerManagement manager = state.getPlayerManagement();
    Player opponentPlayer = manager.getOpponentPlayer(manager.getCurrentPlayer());

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

  /** 
   * Notifies all Listeners of a changed {@link GameState}. 
   */
  private void notifyListenersOfChangedState() {
    support.firePropertyChange(Model.STATE_CHANGED, null, state);
  }

  /** Notifies all listeners that a listener was added or removed. */
  private void notifyListenersOfChangedListeners() {
    support.firePropertyChange(Model.LISTENERS_CHANGED, null, this);
  }

  /**
   * Checks whether a {@link Cell} is part of the middle of the {@link GameField} in which {@link
   * Disk} are placed during the very beginning of the game.
   *
   * @param cell which shall be checked.
   * @return {@code true} if the disk can be placed at the cell during the first moves, {@code
   *     false} otherwise.
   */
  private boolean isWithinMiddleSquare(Cell cell) {
    int size = state.getField().getSize();
    int minCoordinate = (size / 2) - 1;
    int maxCoordinate = size / 2;
    return (cell.getRow() >= minCoordinate)
        && (cell.getRow() <= maxCoordinate)
        && (cell.getColumn() >= minCoordinate)
        && (cell.getColumn() <= maxCoordinate)
        && state.getField().get(cell).isEmpty();
  }

  /** Checks who has won if the game is finished and sets the winner. */
  private void handleWinner() {
    if (!(state.getCurrentPhase().equals(Phase.FINISHED))) {
      return;
    }
    int numberOfDisksPlayerOne =
        state.getField().getAllCellsForPlayer(state.getPlayerManagement().getPlayerOne()).size();
    int numberOfDisksPlayerTwo =
        state.getField().getAllCellsForPlayer(state.getPlayerManagement().getPlayerTwo()).size();
    ModifiablePlayerManagement manager = state.getPlayerManagement();

    if (numberOfDisksPlayerOne == numberOfDisksPlayerTwo) {
      manager.setWinner(Optional.empty());
      return;
    }
    if (numberOfDisksPlayerOne > numberOfDisksPlayerTwo) {
      manager.setWinner(Optional.of(manager.getPlayerOne()));
    } else {
      manager.setWinner(Optional.of(manager.getPlayerTwo()));
    }
  }

  /**
   * Returns the number of disks player one still has left.
   *
   * @return the remaining number of disks of player one
   */
  public int getNumberOfDisksPlayerOne() {
    return numberOfPlayerOneDisks;
  }

  /**
   * Returns the number of disks player two still has left.
   *
   * @return the remaining number of disks of player two
   */
  public int getNumberOfDisksPlayerTwo() {
    return numberOfPlayerTwoDisks;
  }

  @Override
  public void triggerAiMove() {
    if (mode.equals(GameMode.SINGLEPLAYER)
        && (state.getPlayerManagement().getCurrentPlayer() instanceof AiPlayer)
        && state.getCurrentPhase().equals(Phase.RUNNING)) {
      long sleepAmount =
          new Random().nextInt(AI_MAX_SLEEP_AMOUNT - AI_MOVE_OFFSET) + AI_MOVE_OFFSET;
      try {
        Thread.sleep(sleepAmount);
      } catch (InterruptedException e) {
        // Doesn't matter
      }
      Cell bestMove = ai.calculateBestMove(state);
      placeDisk(new DiskImpl(state.getPlayerManagement().getCurrentPlayer()), bestMove);
    }
  }
}
