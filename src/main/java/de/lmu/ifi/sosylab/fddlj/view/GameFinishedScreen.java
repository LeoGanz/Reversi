package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises variables and is responsible for building GUI elements.
   *
   * @param controller a reference to a controller instance
   * @param model a reference to a model instance
   * @param mainStage a reference to the game's main stage
   * @param messages the ResourceBundle for the externalised strings
   */
  public GameFinishedScreen(
      Controller controller, Model model, Stage mainStage, ResourceBundle messages) {
    super();

    this.controller = controller;
    this.model = model;

    this.messages = messages;

    initScreen(mainStage);
  }

  private void initScreen(Stage mainStage) {
    BorderPane root = new BorderPane();
    root.getStylesheets().add("cssFiles/gameModeSelector.css");
    root.setId("main-pane");
    root.setPadding(new Insets(30));

    Label title = new Label(messages.getString("GameFinishedScreen_Title"));
    title.setId("title-label");
    root.setTop(title);
    BorderPane.setAlignment(title, Pos.CENTER);

    BorderPane center = new BorderPane();

    Fireworks fireworks = new Fireworks();
    fireworks.setMaxWidth(Double.POSITIVE_INFINITY);
    center.setCenter(fireworks);
    Label winner = getWinnerLabel();
    center.setBottom(winner);
    BorderPane.setAlignment(winner, Pos.CENTER);
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

    fireworks.start();
  }

  private HBox getOptions(Stage mainStage) {
    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(20));

    Button exit = getButton(messages.getString("GameFinishedScreen_ExitButton_Text"));
    exit.setOnAction(e -> Platform.exit());

    Button restart = getButton(messages.getString("GameFinishedScreen_RestartButton_Text"));
    restart.setOnAction(
        e -> {
          close();
          controller.resetGame(
              controller.getCurrentGameMode(),
              model.getState().getPlayerManagement().getPlayerOne(),
              model.getState().getPlayerManagement().getPlayerTwo());
        });

    Button mainScreen = getButton(messages.getString("GameFinishedScreen_MainScreenButton_Text"));
    mainScreen.setOnAction(
        e -> {
          mainStage.close();
          close();
          if (controller instanceof ControllerImpl) {
            ((ControllerImpl) controller).showGameModeSelector(new Stage());
          } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(messages.getString("GameFinishedScreen_ReturnError_Title"));
            alert.setHeaderText(messages.getString("GameFinishedScreen_ReturnError_Subtitle"));
            alert.setContentText(messages.getString("GameFinishedScreen_ReturnError_Info"));

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
          messages.getString("GameFinishedScreen_Congratulations")
              + " "
              + model.getState().getPlayerManagement().getWinner().get().getName()
              + " "
              + messages.getString("GameFinishedScreen_WinnerText");
    } else {
      text = messages.getString("GameFinishedScreen_Draw");
    }

    Label label = new Label(text);
    label.setFont(Font.font("Serif"));
    label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 30;");

    return label;
  }
}
