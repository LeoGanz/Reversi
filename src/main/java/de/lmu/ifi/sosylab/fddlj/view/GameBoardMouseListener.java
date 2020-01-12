package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.CellImpl;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
<<<<<<< HEAD
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.util.Optional;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class GameBoardMouseListener {

  private GameMode gameMode;
  private Model model;
  private GameBoard gameBoard;
  private Controller controller;

  public GameBoardMouseListener(
      GameMode gameMode, Model model, GameBoard gameBoard, Controller controller) {
    this.gameMode = gameMode;
    this.model = model;
    this.gameBoard = gameBoard;
    this.controller = controller;
  }

  void handleMouseMoved(MouseEvent e) {

    if (gameMode == GameMode.SINGLEPLAYER
        && model.getState().getPlayerManagement().getCurrentPlayer()
            == model.getState().getPlayerManagement().getPlayerTwo()) {
      gameBoard.setCursor(Cursor.DEFAULT);
      return;
    }

    // checks whether the game is running and if the cursor is inside of the game field's bounds
    if (model.getState().getCurrentPhase() == Phase.RUNNING && isMouseInGameField(e)) {
      // Converts the cursor's current coordinates into a cell
      Cell current = getCellForCoordinates((int) e.getX(), (int) e.getY());
      // Change the cursor only when it hovers over a cell in which no disk is currently placed
      if (model.getState().getField().get(current).isEmpty()) {
        if (model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current)) {
          gameBoard.setCursor(Cursor.HAND);
        } else {
          gameBoard.setCursor(Cursor.DEFAULT);
        }
      } else {
        gameBoard.setCursor(Cursor.DEFAULT);
      }
    }
  }

  void handleMouseClicked(MouseEvent e) {
    if (isMouseInGameField(e)) {
      // Check whether user used left or right mouse button
      if (e.isPrimaryButtonDown()) {
        // Converts the cursor's current coordinates into a cell
        Cell current = getCellForCoordinates((int) e.getX(), (int) e.getY());

        if (gameBoard.getCursor() == Cursor.HAND
            && model
                .getPossibleMovesForPlayer(
                    model.getState().getPlayerManagement().getCurrentPlayer())
                .contains(current)) {
          gameBoard.setCursor(Cursor.DEFAULT);
          controller.placeDisk(current);
        }
      }
    }
  }

  void handleScrollEvent(ScrollEvent e) {}

  private boolean isMouseInGameField(MouseEvent e) {
    return e.getX() > gameBoard.getWidthOffsetForGameField()
        && e.getX() < gameBoard.getWidthOffsetForGameField() + gameBoard.getGameFieldWidth()
        && e.getY() > gameBoard.getHeightOffsetForGameField()
        && e.getY() < gameBoard.getHeightOffsetForGameField() + gameBoard.getGameFieldHeight();
  }

  /**
   * Converts the given coordinates to a cell on the game field.
   *
   * @param x the x coordinate in the cell
   * @param y the y coordinate in the cell
   * @return the cell on the game field containing the coordinates
   */
  private Cell getCellForCoordinates(int x, int y) {

    int column = x - gameBoard.getWidthOffsetForGameField();
    column = (int) (column / (gameBoard.getCellWidth() + gameBoard.getSpacing()));

    int row = y - gameBoard.getHeightOffsetForGameField();
    row = (int) (row / (gameBoard.getCellHeight() + gameBoard.getSpacing()));

    return new CellImpl(column, (GameFieldImpl.SIZE - 1) - row);
  }

  Player getPlayerOnCell(Cell cell) {
    Optional<Disk> opt = model.getState().getField().get(cell);
    if (opt.isPresent()) {
      return opt.get().getPlayer();
    } else {
      return null;
    }
  }
=======
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.util.Optional;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class GameBoardMouseListener {

  private GameMode gameMode;
  private Model model;
  private GameBoard gameBoard;
  private Controller controller;

  public GameBoardMouseListener(GameMode gameMode, Model model, GameBoard gameBoard,
      Controller controller) {
    this.gameMode = gameMode;
    this.model = model;
    this.gameBoard = gameBoard;
    this.controller = controller;
  }

  void handleMouseMoved(MouseEvent e) {

    if (gameMode == GameMode.SINGLEPLAYER
        && model.getState().getCurrentPlayer() == model.getAiPlayer()) {
      gameBoard.setCursor(Cursor.DEFAULT);
      return;
    }

    // checks whether the game is running and if the cursor is inside of the game field's bounds
    if (model.getState().getCurrentPhase() == Phase.RUNNING && isMouseInGameField(e)) {
      // Converts the cursor's current coordinates into a cell
      Cell current = getCellForCoordinates((int) e.getX(), (int) e.getY());
      // Change the cursor only when it hovers over a cell in which no disk is currently placed
      if (model.getState().getField().get(current).isEmpty()) {
        if (model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current)) {
          gameBoard.setCursor(Cursor.HAND);
        } else {
          gameBoard.setCursor(Cursor.DEFAULT);
        }
      } else {
        gameBoard.setCursor(Cursor.DEFAULT);
      }

    }

  }

  void handleMouseClicked(MouseEvent e) {
    if (isMouseInGameField(e)) {
      // Check whether user used left or right mouse button
      if (e.isPrimaryButtonDown()) {
        // Converts the cursor's current coordinates into a cell
        Cell current = getCellForCoordinates((int) e.getX(), (int) e.getY());

        if (gameBoard.getCursor() == Cursor.HAND && model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current)) {
          gameBoard.setCursor(Cursor.DEFAULT);
          controller.place(current);
        }
      }
    }
  }

  void handleScrollEvent(ScrollEvent e) {

  }

  private boolean isMouseInGameField(MouseEvent e) {
    return e.getX() > gameBoard.getWidthOffsetForGameField()
        && e.getX() < gameBoard.getWidthOffsetForGameField() + gameBoard.getGameFieldWidth()
        && e.getY() > gameBoard.getHeightOffsetForGameField()
        && e.getY() < gameBoard.getHeightOffsetForGameField() + gameBoard.getGameFieldHeight();
  }

  /**
   * Converts the given coordinates to a cell on the game field.
   * 
   * @param x the x coordinate in the cell
   * @param y the y coordinate in the cell
   * @return the cell on the game field containing the coordinates
   */
  private Cell getCellForCoordinates(int x, int y) {

    int column = x - gameBoard.getWidthOffsetForGameField();
    column = (int) (column / (gameBoard.getCellWidth() + gameBoard.getSpacing()));

    int row = y - gameBoard.getHeightOffsetForGameField();
    row = (int) (row / (gameBoard.getCellHeight() + gameBoard.getSpacing()));

    return new CellImpl(column, (GameFieldImpl.SIZE - 1) - row);


  }

  Player getPlayerOnCell(Cell cell) {
    Optional<Disk> opt = model.getState().getField().get(cell);
    if (opt.isPresent()) {
      return opt.get().getPlayer();
    } else {
      return null;
    }
  }

>>>>>>> refs/remotes/origin/20-create-gui
}
