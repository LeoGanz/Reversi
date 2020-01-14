package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.CellImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameFieldImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;

public class GraphicCell extends BorderPane implements PropertyChangeListener {

  private Cell current;

  private GraphicDisk diskOnCell;

  private Model model;
  private GameMode gameMode;

  private boolean indicateMoves;
  private boolean isMovePossibleOnCell;

  private Controller controller;

  public GraphicCell(
      int column,
      int row,
      GameBoardGrid gameBoardGrid,
      Model model,
      GameMode gameMode,
      Controller controller) {
    this.model = model;
    this.gameMode = gameMode;
    this.controller = controller;

    current = new CellImpl(column, row);
    indicateMoves = true;

    setStyle(
        "-fx-background-color: transparent; -fx-border-color: #d6d6d6; -fx-border-width: 1.2;");

    double initValue =
        (3 * Screen.getPrimary().getVisualBounds().getHeight() / 4)
            / (double) (GameFieldImpl.SIZE + 1);
    setMinHeight(initValue);
    setMinWidth(initValue);

    gameBoardGrid
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double prefWidth =
                  (2 * gameBoardGrid.getHeight() / 3) / (double) (GameFieldImpl.SIZE + 1);
            });
    gameBoardGrid
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double prefHeight =
                  (2 * gameBoardGrid.getHeight() / 3) / (double) (GameFieldImpl.SIZE + 1);
            });

    setOnMouseMoved(e -> handleMouseMoved(e));
    setOnMouseClicked(e -> handleMouseClicked(e));

    isMovePossibleOnCell =
        model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current);
    indicatePossibleMove();
  }

  private void handleMouseMoved(MouseEvent e) {

    if (gameMode == GameMode.SINGLEPLAYER
        && model.getState().getPlayerManagement().getCurrentPlayer()
            == model.getState().getPlayerManagement().getPlayerTwo()) {
      setCursor(Cursor.DEFAULT);
      return;
    }

    if (model.getState().getCurrentPhase() == Phase.RUNNING) {
      if (model.getState().getField().get(current).isEmpty()) {
        if (model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current)) {
          setCursor(Cursor.HAND);
        } else {
          setCursor(Cursor.DEFAULT);
        }
      } else {
        setCursor(Cursor.DEFAULT);
      }
    }
  }

  private void handleMouseClicked(MouseEvent e) {
    if (getCursor().equals(Cursor.HAND)) {
      if (model
          .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
          .contains(current)) {
        setCursor(Cursor.DEFAULT);
        boolean successful = controller.placeDisk(current);
        if (!successful) {
          setCursor(Cursor.HAND);
        } else {
          diskOnCell =
              new GraphicDisk(
                  getWidth() - 20,
                  getHeight() - 20,
                  (getHeight() - 20) / 2,
                  model.getState().getField().get(current).get().getPlayer().getColor());
          setCenter(diskOnCell);
          setStyle(
              "-fx-background-color: transparent; -fx-border-color: #d6d6d6;"
                  + " -fx-border-width: 1.2;");
        }
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> handlePropertyChangeEvent(evt));
  }

  private void handlePropertyChangeEvent(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(SwitchButton.HINTS_STATE_CHANGED)) {

      if (evt.getNewValue() instanceof Boolean) {
        indicateMoves = (boolean) evt.getNewValue();
        if (model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current)) {
          indicatePossibleMove();
        }
      }

    } else if (evt.getPropertyName().equals(Model.STATE_CHANGED)) {
      if (model
          .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
          .contains(current)) {
        isMovePossibleOnCell = true;
      } else {
        isMovePossibleOnCell = false;
      }
      indicatePossibleMove();

      if (model.getState().getField().get(current).isPresent()) {
        diskOnCell.setFill(model.getState().getField().get(current).get().getPlayer().getColor());
      }
    }
  }

  private void indicatePossibleMove() {
    if (indicateMoves && isMovePossibleOnCell && diskOnCell == null) {
      double prefRadius =
          ((3 * Screen.getPrimary().getVisualBounds().getHeight() / 4)
                      / (double) (GameFieldImpl.SIZE + 1))
                  / 2
              - 20;

      Circle circle = new Circle(prefRadius);
      circle.setFill(new Color(0, 1, 0, 0.2));
      circle.setCenterX(getWidth() / 2);
      circle.setCenterY(getHeight() / 2);

      setStyle(
          "-fx-background-color: transparent; -fx-border-color: rgba(0,255,0,255);"
              + " -fx-border-width: 2.2;");
      setCenter(circle);
    } else {
      if (diskOnCell == null) {
        setCenter(null);
        setStyle(
            "-fx-background-color: transparent; -fx-border-color: #d6d6d6; -fx-border-width: 1.2;");
      }
    }
  }
}
