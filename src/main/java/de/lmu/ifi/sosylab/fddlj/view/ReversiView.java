package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import java.beans.PropertyChangeEvent;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
public class ReversiView implements View {

  private Model model;

  private Stage stage;
  private Scene scene;
  private BorderPane root;
  private GameBoard gameBoard;

  private Controller controller;

  /**
   * Constructor of this class initialises the main frame of the game.
   *
   * @param stage the stage created upon launching the application
   * @param model a reference to the model instance
   * @param controller a reference to the controller instance
   */
  public ReversiView(Stage stage, Model model, Controller controller) {

    this.controller = controller;
    this.stage = stage;
    this.model = model;

    this.stage.setTitle("Reversi");
    this.stage.setMaximized(true);
    this.stage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() / 3.0);
    this.stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() / 3.0);
  }

  @Override
  public void showGame(GameMode gameMode) {
    root = new BorderPane();
    root.setStyle("-fx-background-color: #6e7175;");

    VBox left = getDiskIndicators(gameMode);
    root.setLeft(left);
    BorderPane.setAlignment(left, Pos.CENTER);

    // gameBoard = new GameBoard(model, gameMode, controller);
    GameBoardGrid gameBoard = new GameBoardGrid(model, gameMode, controller, stage);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);
    BorderPane.setMargin(gameBoard, new Insets(50));

    if (gameMode != GameMode.MULTIPLAYER) {
      Button reset = getButton("Reset game");
      reset.setOnAction(e -> {});
      root.setBottom(reset);
      BorderPane.setAlignment(root, Pos.CENTER_LEFT);
    }

    scene = new Scene(root);

    stage.setScene(scene);
    stage.show();
  }

  private VBox getDiskIndicators(GameMode gameMode) {

    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);
    vbox.setPadding(new Insets(20));

    DiskIndicator currentPlayer = new DiskIndicator(model, "Current player:");

    if (gameMode == GameMode.HOTSEAT) {
      vbox.getChildren().add(currentPlayer);
    } else {
      DiskIndicator ownDiskIndicator =
          new DiskIndicator(model, "You are playing the following disks:");

      Region spacer = new Region();
      VBox.setVgrow(spacer, Priority.ALWAYS);

      vbox.getChildren().addAll(currentPlayer, spacer, ownDiskIndicator);
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

    // the next lines are for demonstration purposes
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      if (model.getState().getCurrentPhase() == Phase.FINISHED) {
        new GameFinishedScreen(controller, model, stage);
      }
    }

    if (event.getPropertyName().equals(Model.LISTENERS_CHANGED)) {
      if (event.getNewValue() instanceof Model) {
        this.model = (Model) event.getNewValue();
      }
    }
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(500);
    button.setMinWidth(200);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));

    return button;
  }
}
