package de.lmu.ifi.sosylab.fddlj.view;

import com.sun.glass.ui.Screen;
import de.lmu.ifi.sosylab.fddlj.model.Model;
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
import javafx.stage.Stage;

public class ReversiView implements View {

  private Model model;

  private Stage stage;
  private Scene scene;
  private BorderPane root;
  private GameBoard gameBoard;

  public ReversiView(Stage stage, Model model, Controller controller) {

    stage.setTitle("Reversi");
    stage.setMaximized(true);
    stage.setMinWidth(Screen.getMainScreen().getWidth() / 3);
    stage.setMinHeight(Screen.getMainScreen().getHeight() / 3);
  }

  @Override
  public void showGame(GameMode gameMode) {
    root = new BorderPane();

    VBox left = getDiskIndicators(gameMode);
    root.setLeft(left);
    BorderPane.setAlignment(left, Pos.CENTER);

    gameBoard = new GameBoard(model, gameMode);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);

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
      root.layout();
    }

    if (event.getPropertyName().equals(Model.MODEL_CHANGED)) {
      if (event.getNewValue() instanceof Model) {
        this.model = (Model) event.getNewValue();

        root.layout();
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
