package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class OnlineViewImpl implements OnlineView {

  private MultiplayerController controller;
  private Stage stage;
  private BorderPane root;

  private PropertyChangeSupport support;
  private Model model;

  private Scene scene;

  /**
   * @param controller
   * @param stage
   */
  public OnlineViewImpl(MultiplayerController controller, Stage stage) {
    this.controller = controller;
    this.stage = stage;

    support = new PropertyChangeSupport(this);

    this.stage.setTitle("Reversi");
    this.stage.setMaximized(true);
    this.stage.setMinWidth(2 * Screen.getPrimary().getVisualBounds().getWidth() / 5.0);
    this.stage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() / 2.0);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> handlePropertyChangeEvent(evt));
  }

  private void handlePropertyChangeEvent(PropertyChangeEvent evt) {}

  @Override
  public void showGame(GameMode gameMode) {

    VBox left = getDiskIndicators(gameMode);
    root.setLeft(left);
    BorderPane.setAlignment(left, Pos.CENTER);

    GameBoardGrid gameBoard = new GameBoardGrid(model, controller, stage, this);
    root.setCenter(gameBoard);
    BorderPane.setAlignment(root, Pos.CENTER);
    BorderPane.setMargin(gameBoard, new Insets(30, 50, 30, 50));

    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void addListener(PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListener(PropertyChangeListener listener) {
    // TODO Auto-generated method stub

  }

  @Override
  public void showWaitingScreen() {
    root = new BorderPane();
    root.getStylesheets().add("cssFiles/mainGame.css");
    root.setId("main-background");

    Image loadingGif =
        new Image(getClass().getClassLoader().getResourceAsStream("images/LoadingGif.gif"));
    ImageView imageView = new ImageView(loadingGif);
    root.setCenter(imageView);

    Label waiting = new Label("Waiting for opponent to join lobby...");
    waiting.setFont(Font.font(20));
    waiting.setStyle("-fx-text-fill: white");
    root.setTop(waiting);

    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  private VBox getDiskIndicators(GameMode gameMode) {

    VBox vbox = new VBox(50);
    vbox.setAlignment(Pos.TOP_CENTER);
    vbox.setPadding(new Insets(20));

    DiskIndicator currentPlayer = new DiskIndicator(model, "Current player:", this);
    DiskIndicator ownDiskIndicator =
        new DiskIndicator(model, "You are playing the following disks:", this);

    vbox.getChildren().addAll(currentPlayer, ownDiskIndicator);

    Button reset = getButton("Reset game");
    reset.setOnAction(
        e -> {
          controller.requestGameReset();
        });

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    vbox.getChildren().addAll(spacer, reset);

    return vbox;
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
}
