package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class represents the game's main frame that holds all other GUI elements.
 *
 * @author Josef Feger
 */
public class ViewImpl implements View {

  private Model model;
  private Controller controller;

  private Stage stage;
  private Scene scene;
  private BorderPane root;
  private GameBoardGrid gameBoard;

  private PropertyChangeSupport support;

  /**
   * Constructor of this class initialises the main frame of the game.
   *
   * @param stage the stage created upon launching the application
   * @param model a reference to the model instance
   * @param controller a reference to the controller instance
   */
  public ViewImpl(Stage stage, Model model, Controller controller) {

    this.controller = controller;
    this.stage = stage;
    this.model = model;

    support = new PropertyChangeSupport(this);

    this.stage.setTitle("Reversi");
    this.stage.setMaximized(true);
    this.stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3.0);
    this.stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() / 2.0);
  }

  @Override
  public void showGame(GameMode gameMode) {
    root = new BorderPane();
    root.getStylesheets().add("cssFiles/mainGame.css");
    root.setId("main-background");

    VBox left = getDiskIndicators(gameMode);
    root.setLeft(left);
    BorderPane.setAlignment(left, Pos.CENTER);

    gameBoard = new GameBoardGrid(model, controller, stage, this);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);
    BorderPane.setMargin(gameBoard, new Insets(30, 50, 30, 50));

    scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  private VBox getDiskIndicators(GameMode gameMode) {

    VBox vbox = new VBox(50);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(20));

    Region spacerTop = new Region();
    VBox.setVgrow(spacerTop, Priority.ALWAYS);
    vbox.getChildren().add(spacerTop);

    DiskIndicator currentPlayer = new DiskIndicator(model, "Current player:", this);

    if (gameMode == GameMode.HOTSEAT) {
      vbox.getChildren().add(currentPlayer);
    } else {
      DiskIndicator ownDiskIndicator =
          new DiskIndicator(model, "You are playing the following disks:", this);

      vbox.getChildren().addAll(currentPlayer, ownDiskIndicator);
    }

    if (gameMode != GameMode.MULTIPLAYER) {
      Button reset = getButton("Reset game");
      reset.setOnAction(
          e -> {
            controller.resetGame(
                gameMode,
                model.getState().getPlayerManagement().getPlayerOne(),
                model.getState().getPlayerManagement().getPlayerTwo());
          });

      Region spacer = new Region();
      VBox.setVgrow(spacer, Priority.ALWAYS);

      vbox.getChildren().addAll(spacer, reset);
    }

    return vbox;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    Platform.runLater(
        new Runnable() {

          @Override
          public void run() {
            handleChangeEvent(event);
          }
        });
  }

  /**
   * The observable (= model) has just published that it has changed its state. The GUI needs to be
   * updated accordingly here.
   *
   * @param event The event that has been fired by the model.
   */
  private void handleChangeEvent(PropertyChangeEvent event) {

    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      support.firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());

      if (model.getState().getCurrentPhase() == Phase.FINISHED) {

        WritableImage snapshot = gameBoard.snapshot(new SnapshotParameters(), null);

        root.setDisable(true);
        new GameFinishedScreen(controller, model, stage, snapshot);
      }
    }

    if (event.getPropertyName().equals(Model.LISTENERS_CHANGED)) {
      if (event.getNewValue() instanceof Model) {
        this.model = (Model) event.getNewValue();
        support.firePropertyChange(event);
      }
    }
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(250);
    button.setMinWidth(100);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));

    return button;
  }

  @Override
  public void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }
}
