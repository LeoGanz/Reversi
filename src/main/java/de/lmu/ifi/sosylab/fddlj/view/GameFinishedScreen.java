package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class offers a frame that can be used to signal the end of the game and notify the player
 * who won the game.
 *
 * @author Josef Feger
 */
public class GameFinishedScreen extends Stage {

  private Model model;
  private Controller controller;

  /**
   * Constructor of this class initialises variables and is responsible for building GUI elements.
   *
   * @param controller a reference to a controller instance
   * @param model a reference to a model instance
   * @param mainStage a reference to the game's main stage
   */
  public GameFinishedScreen(
      Controller controller, Model model, Stage mainStage, WritableImage snapshot) {
    super();

    this.controller = controller;
    this.model = model;

    initScreen(mainStage, snapshot);
  }

  private void initScreen(Stage mainStage, WritableImage snapshot) {
    BorderPane root = new BorderPane();
    root.getStylesheets().add("cssFiles/gameModeSelector.css");
    root.setId("main-pane");
    root.setPadding(new Insets(30));

    Label title = new Label("Game over!");
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 40;");
    root.setTop(title);
    BorderPane.setAlignment(title, Pos.CENTER);

    VBox center = new VBox(30);
    center.setAlignment(Pos.CENTER);

    ImageView imageView = new ImageView(snapshot);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(2 * (Screen.getPrimary().getVisualBounds().getHeight() / 3) / 2);
    center.getChildren().add(imageView);
    center.getChildren().add(getWinnerLabel());
    root.setCenter(center);

    root.setBottom(getOptions(mainStage));

    Scene scene = new Scene(root);

    setScene(scene);
    setTitle("Reversi");
    initStyle(StageStyle.UNDECORATED);
    setMinWidth(2 * (Screen.getPrimary().getVisualBounds().getWidth() / 3));
    setMinHeight(2 * (Screen.getPrimary().getVisualBounds().getHeight() / 3));
    setWidth(getMinWidth());
    setHeight(getMinHeight());
    centerOnScreen();
    show();
  }

  private HBox getOptions(Stage mainStage) {
    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(20));

    Button exit = getButton("Exit");
    exit.setOnAction(e -> Platform.exit());

    Button restart = getButton("Restart");
    restart.setOnAction(
        e -> {
          controller.resetGame(
              controller.getCurrentGameMode(),
              model.getState().getPlayerManagement().getPlayerOne(),
              model.getState().getPlayerManagement().getPlayerTwo());
          close();
        });

    Button mainScreen = getButton("Main screen");
    mainScreen.setOnAction(
        e -> {
          mainStage.close();
          if (controller instanceof ControllerImpl) {
            ((ControllerImpl) controller).showGameModeSelector(new Stage());
          } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to return to main screen");
            alert.setContentText("Failed to return to main screen. Exiting the program ...");

            alert.showAndWait();
            Platform.exit();
          }
        });

    hbox.getChildren().addAll(mainScreen, restart, exit);
    return hbox;
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

  private Label getWinnerLabel() {
    String text;

    if (model.getState().getPlayerManagement().getWinner().isPresent()) {
      text =
          model.getState().getPlayerManagement().getWinner().get().getName() + " has won the game!";
    } else {
      text = "The game resulted in a draw!";
    }

    Label label = new Label(text);
    label.setFont(Font.font("Serif"));
    label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 30;");

    return label;
  }
}
