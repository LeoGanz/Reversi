package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.AiPlayer;
import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.CellImpl;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
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

  static final int MIN_WIDTH = 60;
  static final int MIN_HEIGHT = 60;

  private Model model;
  private GameBoardGrid gameBoardGrid;
  private Controller controller;
  private Cell current;

  private boolean indicateMoves;
  private boolean isMovePossibleOnCell;

  static final float SPACING = 60;

  private String color;

  private String cssNormal;
  private String cssHighlighted;
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
    this.gameBoardGrid = gameBoardGrid;

    current = new CellImpl(column, row);
    indicateMoves = true;

    initialiseCellColor();
    setStyle(cssNormal);
    double initValue =
        (Screen.getPrimary().getVisualBounds().getHeight() - SPACING)
            / (double) (model.getState().getField().getSize() + 1);
    setPrefHeight(initValue);
    setPrefWidth(initValue);
    setMinWidth(MIN_WIDTH);
    setMinHeight(MIN_HEIGHT);

    addListeners(gameBoardGrid);
    setOnMouseMoved(e -> handleMouseMoved(e));
    setOnMouseClicked(e -> handleMouseClicked(e));

    setCursor(Cursor.DEFAULT);

    isMovePossibleOnCell =
        model
            .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
            .contains(current);
    indicatePossibleMove();
  }

  private void initialiseCellColor() {

    if ((current.getRow() % 2 == 0 && current.getColumn() % 2 == 0)) {
      color = "#636363";
    } else if ((current.getRow() % 2 != 0 && current.getColumn() % 2 == 0)) {
      color = "#cecece";
    } else if (current.getRow() % 2 == 0 && current.getColumn() % 2 != 0) {
      color = "#cecece";
    } else if (current.getRow() % 2 != 0 && current.getColumn() % 2 != 0) {
      color = "#636363";
    } else {
      color = "transparent";
    }

    cssNormal =
        "-fx-background-color: " + color + "; -fx-border-color: black;" + " -fx-border-width: 1.5;";
    cssHighlighted =
        "-fx-background-color: "
            + color
            + "; -fx-border-color: rgba(0,255,0,255);"
            + " -fx-border-width: 2.2;";
  }

  /**
   * Public constructor with no arguments. Used for creating a simple graphic cell simply for
   * displaying purposes.
   */
  public GraphicCell() {
    setStyle(cssStart);
    double initValue = (Screen.getPrimary().getVisualBounds().getHeight() - SPACING) / 9.0;
    setPrefHeight(initValue);
    setPrefWidth(initValue);
  }

  private void addListeners(GameBoardGrid gameBoardGrid) {
    gameBoardGrid.widthProperty().addListener((obs, oldVal, newVal) -> resizeCell());
    gameBoardGrid.heightProperty().addListener((obs, oldVal, newVal) -> resizeCell());
  }

  private void resizeCell() {
    double prefSize;
    if (gameBoardGrid.getWidth() >= gameBoardGrid.getHeight()) {
      prefSize =
          (gameBoardGrid.getHeight() - SPACING)
              / (double) (model.getState().getField().getSize() + 1);

      if (prefSize < MIN_HEIGHT) {
        prefSize = MIN_HEIGHT;
      }
    } else {
      prefSize =
          (gameBoardGrid.getWidth() - SPACING)
              / (double) (model.getState().getField().getSize() + 1);

      if (prefSize < MIN_WIDTH) {
        prefSize = MIN_WIDTH;
      }
    }

    setPrefWidth(prefSize);
    setPrefHeight(prefSize);
    setMaxWidth(prefSize);
    setMaxHeight(prefSize);

    if (diskOnCell != null) {
      diskOnCell.resizeDisk(
          prefSize - 2 * GraphicDisk.PADDING,
          prefSize - 2 * GraphicDisk.PADDING,
          (prefSize - 2 * GraphicDisk.PADDING) / 2);
    }

    if (indicateMoves && isMovePossibleOnCell) {
      indicatePossibleMove();
    }
  }

  private void handleMouseMoved(MouseEvent e) {

    if (controller.getCurrentGameMode() == GameMode.SPECTATOR) {
      return;
    }

    if (model.getState().getCurrentPhase() != Phase.RUNNING) {
      setCursor(Cursor.DEFAULT);
      return;
    }

    if (controller.getCurrentGameMode() == GameMode.SINGLEPLAYER
        && model.getState().getPlayerManagement().getCurrentPlayer()
            == model.getState().getPlayerManagement().getPlayerTwo()) {
      setCursor(Cursor.DEFAULT);
      return;
    }

    if (getCursor() == null) {
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
    if (controller.getCurrentGameMode() == GameMode.SPECTATOR) {
      return;
    }

    if (getCursor() == null) {
      return;
    }

    if (getCursor().equals(Cursor.HAND)) {
      if (model
          .getPossibleMovesForPlayer(model.getState().getPlayerManagement().getCurrentPlayer())
          .contains(current)) {
        setCursor(Cursor.DEFAULT);
        controller.placeDisk(current);
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
      handleStateChanged(evt);
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
    } else if (evt.getPropertyName().equals(ViewImpl.STAGE_RESIZED)) {
      resizeCell();
    }
  }

  private void handleStateChanged(PropertyChangeEvent evt) {
    if (model.getState().getCurrentPhase() == Phase.FINISHED) {
      isMovePossibleOnCell = false;
      indicateMoves = false;
      indicatePossibleMove();
    }

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
              getWidth() - 10, getHeight() - 10, (getHeight() - 10) / 2, getDiskColor());

      gameBoardGrid.playPlacementSound();

      setCenter(diskOnCell);
      setStyle(cssNormal);
    } else if (model.getState().getField().get(current).isPresent() && diskOnCell != null) {
      diskOnCell.changeColor(getDiskColor());
    }
  }

  private Color getDiskColor() {
    Color color = model.getState().getField().get(current).get().getPlayer().getColor();

    if (controller instanceof MultiplayerController) {
      if (!model
          .getState()
          .getField()
          .get(current)
          .get()
          .getPlayer()
          .equals(((MultiplayerController) controller).getOwnPlayer())) {
        if (similarTo(color, ((MultiplayerController) controller).getOwnPlayer().getColor())) {
          if (similarTo(color, Color.WHITE)) {
            color = Color.SILVER;
          } else if (similarTo(color, Color.BLACK)) {
            color = Color.GRAY;
          } else {
            color = color.deriveColor(15, 15, 10, 1);
          }
        }
      }
    }

    return color;
  }

  private void indicatePossibleMove() {
    if (indicateMoves && isMovePossibleOnCell && diskOnCell == null) {

      if (controller.getCurrentGameMode() == GameMode.SINGLEPLAYER
          && model.getState().getPlayerManagement().getCurrentPlayer() instanceof AiPlayer) {
        return;
      }
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

  private boolean similarTo(Color c, Color v) {
    double distance =
        Math.sqrt(
            (c.getRed() - v.getRed()) * (c.getRed() - v.getRed())
                + (c.getGreen() - v.getGreen()) * (c.getGreen() - v.getGreen())
                + (c.getBlue() - v.getBlue()) * (c.getBlue() - v.getBlue()));

    if (distance < 0.25) {
      return true;
    } else {
      return false;
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
