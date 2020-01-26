package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.util.ResourceBundle;
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

  private static final Insets INSETS_ROOTPANE = new Insets(30);
  private static final Insets PADDING_OPTIONSPANE = new Insets(20);
  private static final int SPACING_OPTIONSPANE = 30;

  private static final Font FONT_BUTTON = Font.font(18);
  private static final Cursor CURSOR_BUTTON = Cursor.HAND;
  private static final int MINWIDTH_BUTTON = 200;
  private static final int MAXWIDTH_BUTTON = 500;
  private static final int MINHEIGHT_BUTTON = 50;

  private static final Font FONT_WINNERLABEL = Font.font("Serif");
  private static final String CSS_WINNERLABEL =
      "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 30;";

  private Model model;
  private Controller controller;

  private ResourceBundle messages;

  /**
   * Constructor of this class initialises variables and is responsible for building GUI elements.
   *
   * @param controller a reference to a controller instance
   * @param model a reference to a model instance
   * @param mainStage a reference to the game's main stage
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
    root.setPadding(INSETS_ROOTPANE);

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
    setTitle(messages.getString("Game_Title"));
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
    HBox hbox = new HBox(SPACING_OPTIONSPANE);
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(PADDING_OPTIONSPANE);

    Button exit = getButton(messages.getString("GameFinishedScreen_ExitButton_Text"));
    exit.setOnAction(e -> controller.close());

    Button restart = getButton(messages.getString("GameFinishedScreen_RestartButton_Text"));
    restart.setOnAction(
        e -> {
          close();
          controller.resetGame(
              controller.getCurrentGameMode(),
              model.getState().getPlayerManagement().getPlayerOne(),
              model.getState().getPlayerManagement().getPlayerTwo(),
              model.getState().getField().getSize());
        });

    Button mainScreen = getButton(messages.getString("GameFinishedScreen_MainScreenButton_Text"));
    mainScreen.setOnAction(
        e -> {
          close();
          mainStage.close();

          if (controller instanceof ControllerImpl) {
            ((ControllerImpl) controller).showGameModeSelector(new Stage());
          } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(messages.getString("GameFinishedScreen_ReturnError_Title"));
            alert.setHeaderText(messages.getString("GameFinishedScreen_ReturnError_Subtitle"));
            alert.setContentText(messages.getString("GameFinishedScreen_ReturnError_Info"));

            alert.showAndWait();
            controller.close();
          }
        });

    hbox.getChildren().addAll(mainScreen, restart, exit);
    return hbox;
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(MINHEIGHT_BUTTON);
    button.setMaxWidth(MAXWIDTH_BUTTON);
    button.setMinWidth(MINWIDTH_BUTTON);
    button.setCursor(CURSOR_BUTTON);
    button.setFont(FONT_BUTTON);

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
    label.setFont(FONT_WINNERLABEL);
    label.setStyle(CSS_WINNERLABEL);

    return label;
  }
}
