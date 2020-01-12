package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PlayerCreation extends BorderPane {

  private Controller controller;
  private Stage primaryStage;

  public PlayerCreation(Controller controller, Stage stage) {
    super();

    this.controller = controller;
    this.primaryStage = stage;
    setStyle("-fx-background-color: #ffffff");
  }

  void getSinglePlayerInformation(
      Controller controller, GameModeSelector gameModeSelector, boolean multiplayer) {
    VBox vbox = new VBox(5);

    TextField textField = new TextField();
    vbox.getChildren().add(getUsernameInputField(textField, "Geben Sie Ihren Usernamen ein:"));

    ColorPicker colorPicker = new ColorPicker(Color.WHITE);
    vbox.getChildren().add(getColorPickerPane(colorPicker));
    setCenter(vbox);

    Button start = getButton("Start");
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(textField.getText())) {
            textField.setStyle("-fx-border-color: red;");
            return;
          }

          if (multiplayer) {
            startMultiplayer(textField.getText(), colorPicker.getValue());
          } else {
            startSinglePlayer(textField.getText(), colorPicker.getValue());
          }
          gameModeSelector.close();
        });
    setBottom(start);
    BorderPane.setAlignment(start, Pos.CENTER);
  }

  void getMultiplePlayersInformation(GameModeSelector gameModeSelector) {

    VBox vboxPlayerOne = new VBox();
    TextField textFieldOne = new TextField();
    vboxPlayerOne
        .getChildren()
        .add(getUsernameInputField(textFieldOne, "Geben Sie den Usernamen von Spieler 1 ein:"));
    ColorPicker colorPickerOne = new ColorPicker(Color.WHITE);
    vboxPlayerOne.getChildren().add(getColorPickerPane(colorPickerOne));
    setLeft(vboxPlayerOne);

    VBox vboxPlayerTwo = new VBox();
    TextField textFieldTwo = new TextField();
    vboxPlayerTwo
        .getChildren()
        .add(getUsernameInputField(textFieldTwo, "Geben Sie den Usernamen von Spieler 2 ein:"));
    ColorPicker colorPickerTwo = new ColorPicker(Color.WHITE);
    vboxPlayerTwo.getChildren().add(getColorPickerPane(colorPickerTwo));
    setRight(vboxPlayerTwo);

    Button start = getButton("Start");
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(textFieldOne.getText())) {
            textFieldOne.setStyle("-fx-border-color: red;");
            return;
          }

          if (!isTextFieldInputValid(textFieldTwo.getText())) {
            textFieldTwo.setStyle("-fx-border-color: red;");
            return;
          }

          startHotseat(
              textFieldOne.getText(),
              colorPickerOne.getValue(),
              textFieldTwo.getText(),
              colorPickerTwo.getValue());
          gameModeSelector.close();
        });
    setBottom(start);
    BorderPane.setAlignment(start, Pos.CENTER);
  }

  private VBox getUsernameInputField(TextField textField, String labelText) {
    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);

    Label lbl = new Label(labelText);
    textField.setMinWidth(200);
    textField.setId("text-field");

    vbox.getChildren().addAll(lbl, textField);
    return vbox;
  }

  private VBox getColorPickerPane(ColorPicker colorPicker) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);
    Color c = Color.color(Math.random(), Math.random(), Math.random());

    Pane color = new Pane();
    color.setId("color-pane");
    color.setStyle("-fx-background-color: " + toRgbCode(c) + ";");
    color.setMinHeight(50);
    color.setMaxHeight(50);
    color.setMinWidth(50);
    color.setMaxWidth(50);
    colorPicker.setOnAction(
        (ActionEvent t) -> {
          color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
        });

    Label label = new Label("Wählen Sie eine Farbe aus:");
    vbox.getChildren().addAll(label, color, colorPicker);

    return vbox;
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

  private String toRgbCode(Color color) {

    return String.format(
        "#%02X%02X%02X",
        (int) (color.getRed() * 255),
        (int) (color.getGreen() * 255),
        (int) (color.getBlue() * 255));
  }

  private boolean isTextFieldInputValid(String input) {
    return input != null && !input.equals("");
  }

  private void startSinglePlayer(String playerName, Color playerColor) {
    Player player = new PlayerImpl(playerName, playerColor);
    Player ai = new PlayerImpl("AI", Color.BLACK);

    controller.startMainView(GameMode.SINGLEPLAYER, primaryStage, player, ai);
  }

  private void startHotseat(
      String playerOneName, Color playerOneColor, String playerTwoName, Color playerTwoColor) {
    Player playerOne = new PlayerImpl(playerOneName, playerOneColor);
    Player playerTwo = new PlayerImpl(playerTwoName, playerTwoColor);

    controller.startMainView(GameMode.HOTSEAT, primaryStage, playerOne, playerTwo);
  }

  private void startMultiplayer(String playerName, Color playerColor) {
    Player player = new PlayerImpl(playerName, playerColor);

    controller.startMainView(GameMode.MULTIPLAYER, primaryStage, player, null);
  }
}
