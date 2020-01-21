package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.CellImpl;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
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

/**
 * This class is the graphical representation of a {@link Cell} and can hold a single {@link Disk}.
 *
 * @author Josef Feger
 */
public class GraphicCell extends BorderPane implements PropertyChangeListener {

  private GraphicDisk diskOnCell;

  private Model model;
  private Controller controller;
  private Cell current;

  private boolean indicateMoves;
  private boolean isMovePossibleOnCell;

  static final float SPACING = 100;

  private final String cssNormal =
      "-fx-background-color: #a3a3a3; -fx-border-color: #d6d6d6;" + " -fx-border-width: 1.2;";
  private final String cssHighlighted =
      "-fx-background-color: transparent; -fx-border-color: rgba(0,255,0,255);"
          + " -fx-border-width: 2.2;";
  private final String cssStart =
      "-fx-background-color: #4a4a4a; -fx-border-color: #d6d6d6;" + " -fx-border-width: 1.2;";

  /**
   * Constructor of this class initialises variables and sets initial style and size.
   *
   * @param column the cell's columns
   * @param row the cell's row
   * @param gameBoardGrid a reference to the gameBoardGrid instance
   * @param model a reference to a model instance
   * @param controller a reference to a controller instance
   */
  public GraphicCell(
      int column, int row, GameBoardGrid gameBoardGrid, Model model, Controller controller) {

    this.model = model;
    this.controller = controller;

    current = new CellImpl(column, row);
    indicateMoves = true;

    setStyle(cssNormal);
    double initValue =
        (Screen.getPrimary().getVisualBounds().getHeight() - SPACING)
            / (double) (GameFieldImpl.SIZE + 1);
    setPrefHeight(initValue);
    setPrefWidth(initValue);

    addListeners(gameBoardGrid);
    setOnMouseMoved(e -> handleMouseMoved(e));
    setOnMouseClicked(e -> handleMouseClicked(e));

    isMovePossibleOnCell =
        model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current);
    indicatePossibleMove();
  }

  /**
   * Public constructor with no arguments. Used for creating a simple graphic cell simply for
   * displaying purposes.
   */
  public GraphicCell() {
    setStyle(cssStart);
    double initValue =
        (Screen.getPrimary().getVisualBounds().getHeight() - SPACING)
            / (double) (GameFieldImpl.SIZE + 1);
    setPrefHeight(initValue);
    setPrefWidth(initValue);
  }

  private void addListeners(GameBoardGrid gameBoardGrid) {
    gameBoardGrid
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double prefWidth =
                  (gameBoardGrid.getHeight() - SPACING) / (double) (GameFieldImpl.SIZE + 1);
              setPrefWidth(prefWidth);
              setMinWidth(prefWidth);
              setPrefHeight(prefWidth);
              setMinHeight(prefWidth);

              if (diskOnCell != null) {
                diskOnCell.resizeDisk(getWidth() - 10, getHeight() - 10, (getHeight() - 10) / 2);
              }

              if (indicateMoves && isMovePossibleOnCell) {
                indicatePossibleMove();
              }
            });
    gameBoardGrid
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double prefHeight =
                  (gameBoardGrid.getHeight() - SPACING) / (double) (GameFieldImpl.SIZE + 1);
              setPrefHeight(prefHeight);
              setMinHeight(prefHeight);
              setPrefWidth(prefHeight);
              setMinWidth(prefHeight);

              if (diskOnCell != null) {
                diskOnCell.resizeDisk(getWidth() - 10, getHeight() - 10, (getHeight() - 10) / 2);
              }

              if (indicateMoves && isMovePossibleOnCell) {
                indicatePossibleMove();
              }
            });
  }

  private void handleMouseMoved(MouseEvent e) {

    if (controller.getCurrentGameMode() == GameMode.SINGLEPLAYER
        && model.getState().getPlayerManagement().getCurrentPlayer()
            == model.getState().getPlayerManagement().getPlayerTwo()) {
      setCursor(Cursor.DEFAULT);
      return;
    }

    if (controller.getCurrentGameMode() == GameMode.MULTIPLAYER
        && controller instanceof MultiplayerController) {
      if (!model
          .getState()
          .getPlayerManagement()
          .getCurrentPlayer()
          .equals(((MultiplayerController) controller).getOwnPlayer())) {
        setCursor(Cursor.DEFAULT);
        return;
      }
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
        setCursor(Cursor.HAND);

        setStyle(cssNormal);
        setId("graphic-cell-normal");
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

      if (model.getState().getField().get(current).isPresent() && diskOnCell == null) {

        diskOnCell =
            new GraphicDisk(
                getWidth() - 10,
                getHeight() - 10,
                (getHeight() - 10) / 2,
                model.getState().getField().get(current).get().getPlayer().getColor());
        setCenter(diskOnCell);
      } else if (model.getState().getField().get(current).isPresent()) {
        diskOnCell.changeColor(
            model.getState().getField().get(current).get().getPlayer().getColor());
      }
    } else if (evt.getPropertyName().equals(Model.LISTENERS_CHANGED)) {
      if (evt.getNewValue() instanceof Model) {
        this.model = (Model) evt.getNewValue();
        diskOnCell = null;
        isMovePossibleOnCell =
            model
                .getPossibleMovesForPlayer(
                    model.getState().getPlayerManagement().getCurrentPlayer())
                .contains(current);
        indicatePossibleMove();
      }
    }
  }

  private void indicatePossibleMove() {
    if (indicateMoves && isMovePossibleOnCell && diskOnCell == null) {
      double prefRadius = (getHeight() - 30) / 2;

      Circle circle = new Circle(prefRadius);
      circle.setFill(new Color(0, 1, 0, 0.5));
      circle.setCenterX(getWidth() / 2);
      circle.setCenterY(getHeight() / 2);

      setStyle(cssHighlighted);
      setCenter(circle);
    } else {
      if (diskOnCell == null) {
        setCenter(null);
        setStyle(cssNormal);
      }
    }
  }

  /**
   * Places the given disk on this visual cell.
   *
   * @param disk the disk to place on this cell
   */
  void placeDisk(GraphicDisk disk) {
    setCenter(disk);
  }
}
