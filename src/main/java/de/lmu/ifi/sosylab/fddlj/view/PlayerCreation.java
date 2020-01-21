package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class offers a pane in which, based on the game mode, either one or two players can enter
 * their information.
 *
 * @author Josef Feger
 */
public class PlayerCreation extends BorderPane {

  private Controller controller;
  private Stage primaryStage;

  private Button start;

  private final String tooltipLobby =
      "If you want to join an already existing lobby enter the lobbie's ID here. \n"
          + " By default - meaning when this field stays empty "
          + "- \nyou will join a lobby based on automatic matchmaking.";
  private final String alternateLabelText =
      "When creating a private lobby you can't enter a lobby ID "
          + "since the server will assign you one."
          + " For sharing purposes - you can view your lobby ID"
          + " as soon as you've hit the start button.";

  /**
   * Constructor of this class initialises variables and sets basic styling.
   *
   * @param controller the reference to a controller instance
   * @param stage the reference to the stage created by the application thread
   */
  public PlayerCreation(Controller controller, Stage stage) {
    super();

    this.controller = controller;
    this.primaryStage = stage;
    setStyle("-fx-background-color: transparent");
  }

  /**
   * Show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param controller the reference to a controller instance
   * @param gameModeSelector the reference to a gameModeSelector instance
   */
  void getSinglePlayerInformation(Controller controller, GameModeSelector gameModeSelector) {
    VBox vbox = new VBox(50);
    vbox.setAlignment(Pos.CENTER);

    TextField textField = new TextField();
    textField.setOnKeyTyped(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    vbox.getChildren().add(getInputField(textField, "Geben Sie Ihren Usernamen ein:"));

    ColorPicker colorPicker = new ColorPicker(Color.WHITE);
    vbox.getChildren().add(getColorPickerPane(colorPicker));
    setCenter(vbox);

    HBox bottom = buildBottomSingle(textField, colorPicker, gameModeSelector);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  /**
   * Builds a pane in which two players can enter their information.
   *
   * @param gameModeSelector a reference to a game mode selector instance
   */
  void getMultiplePlayersInformation(GameModeSelector gameModeSelector) {

    HBox alignment = new HBox(50);
    alignment.setAlignment(Pos.CENTER);

    VBox vboxPlayerOne = new VBox(50);
    vboxPlayerOne.setAlignment(Pos.CENTER);
    TextField textFieldOne = new TextField();
    textFieldOne.setOnKeyTyped(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    vboxPlayerOne
        .getChildren()
        .add(getInputField(textFieldOne, "Geben Sie den Usernamen von Spieler 1 ein:"));
    ColorPicker colorPickerOne = new ColorPicker(getRandomColor());
    vboxPlayerOne.getChildren().add(getColorPickerPane(colorPickerOne));
    alignment.getChildren().add(vboxPlayerOne);

    alignment.getChildren().add(getSeparator());

    VBox vboxPlayerTwo = new VBox(50);
    vboxPlayerTwo.setAlignment(Pos.CENTER);
    TextField textFieldTwo = new TextField();
    textFieldTwo.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    vboxPlayerTwo
        .getChildren()
        .add(getInputField(textFieldTwo, "Geben Sie den Usernamen von Spieler 2 ein:"));
    ColorPicker colorPickerTwo = new ColorPicker(getRandomColor());
    vboxPlayerTwo.getChildren().add(getColorPickerPane(colorPickerTwo));
    alignment.getChildren().add(vboxPlayerTwo);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(50));

    HBox bottom =
        buildBottomMulti(
            textFieldOne, textFieldTwo, colorPickerOne, colorPickerTwo, gameModeSelector);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  /**
   * Show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param controller the reference to a controller instance
   * @param gameModeSelector the reference to a gameModeSelector instance
   */
  void getOnlinePlayerInformation(Controller controller, GameModeSelector gameModeSelector) {
    HBox alignment = new HBox(50);
    alignment.setAlignment(Pos.CENTER);

    VBox vboxPlayer = new VBox(50);
    vboxPlayer.setAlignment(Pos.TOP_CENTER);
    TextField textField = new TextField();
    vboxPlayer.getChildren().add(getInputField(textField, "Geben Sie ihren Usernamen ein:"));
    ColorPicker colorPicker = new ColorPicker(getRandomColor());
    vboxPlayer.getChildren().add(getColorPickerPane(colorPicker));
    alignment.getChildren().add(vboxPlayer);

    alignment.getChildren().add(getSeparator());

    VBox vboxConnection = new VBox(50);
    vboxConnection.setAlignment(Pos.TOP_CENTER);
    TextField textFieldServer = new TextField();
    vboxConnection
        .getChildren()
        .add(getInputField(textFieldServer, "Geben Sie IP-Adresse des Servers ein:"));
    TextField textFieldLobby = new TextField();
    textFieldLobby.setPromptText("Hover for info");
    Tooltip tooltip = new Tooltip();
    tooltip.setText(tooltipLobby);
    textFieldLobby.setTooltip(tooltip);
    VBox container = getInputField(textFieldLobby, "Lobby ID");
    vboxConnection.getChildren().add(container);

    Label alternateText = new Label();
    alternateText.setText(alternateLabelText);
    alternateText.setMaxWidth(300);
    alternateText.setWrapText(true);
    alternateText.setFont(Font.font(15));
    alternateText.setStyle("-fx-text-fill: white");

    alignment.getChildren().add(vboxConnection);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(50));

    CheckBox checkbox = new CheckBox("Select if you want to join in a private lobby.");
    checkbox.setText("Create a private lobby");
    checkbox.setFont(Font.font(15));
    checkbox.setSelected(false);
    checkbox.setMinHeight(25);
    checkbox
        .selectedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                if (!newValue) {
                  container.getChildren().remove(alternateText);
                  container.getChildren().add(textFieldLobby);
                } else {
                  container.getChildren().remove(textFieldLobby);
                  container.getChildren().add(alternateText);
                }
              }
            });
    Tooltip tooltipCheckbox = new Tooltip();
    tooltipCheckbox.setText(
        "You can create a private lobby in order to not join a public join and instead host a game,"
            + " which you can play with a friend by sharing the lobbie's ID.");
    checkbox.setTooltip(tooltipCheckbox);
    setCenter(checkbox);
    BorderPane.setAlignment(checkbox, Pos.TOP_CENTER);
    BorderPane.setMargin(checkbox, new Insets(20));

    HBox bottom =
        buildBottomOnline(
            textField, colorPicker, textFieldServer, textFieldLobby, checkbox, gameModeSelector);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private Separator getSeparator() {
    Separator sep = new Separator(Orientation.VERTICAL);
    sep.setStyle("-fx-background-color: #d4d4d4; -fx-background-radius: 0.2;");
    sep.setMaxHeight(450);

    return sep;
  }

  private HBox buildBottomSingle(
      TextField textField, ColorPicker colorPicker, GameModeSelector gameModeSelector) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    start = getButton("Start");
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(textField.getText())) {
            textField.setStyle("-fx-border-color: red;");
            return;
          }
          startSinglePlayer(textField.getText(), colorPicker.getValue());
          gameModeSelector.close();
        });

    Button back = getButton("Back");
    back.setOnAction(e -> gameModeSelector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);
    return hbox;
  }

  private HBox buildBottomMulti(
      TextField textFieldOne,
      TextField textFieldTwo,
      ColorPicker colorPickerOne,
      ColorPicker colorPickerTwo,
      GameModeSelector gameModeSelector) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start = getButton("Start");
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(textFieldOne.getText())) {
            textFieldOne.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!isTextFieldInputValid(textFieldTwo.getText())) {
            textFieldTwo.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          startHotseat(
              textFieldOne.getText(),
              colorPickerOne.getValue(),
              textFieldTwo.getText(),
              colorPickerTwo.getValue());
          gameModeSelector.close();
        });

    Button back = getButton("Back");
    back.setOnAction(e -> gameModeSelector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);

    return hbox;
  }

  private HBox buildBottomOnline(
      TextField playerName,
      ColorPicker playerColor,
      TextField serverAddress,
      TextField lobbyNumber,
      CheckBox checkbox,
      GameModeSelector selector) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start = getButton("Start");
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(playerName.getText())) {
            playerName.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!isTextFieldInputValid(serverAddress.getText())) {
            serverAddress.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!lobbyNumber.getText().trim().matches("(?<=\\s|^)\\d+(?=\\s|$)")) {
            lobbyNumber.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          startMultiplayer(
              playerName.getText(),
              playerColor.getValue(),
              serverAddress.getText(),
              Integer.parseInt(lobbyNumber.getText().trim()),
              checkbox.isSelected());
          selector.close();
        });

    Button back = getButton("Back");
    back.setOnAction(e -> selector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);

    return hbox;
  }

  private VBox getInputField(TextField textField, String labelText) {
    VBox vbox = new VBox(5);
    vbox.setPadding(new Insets(20));
    vbox.setAlignment(Pos.CENTER);

    textField.setMinWidth(200);
    textField.setId("text-field");
    textField.setMinWidth(200);
    textField.setMaxWidth(400);
    textField
        .focusedProperty()
        .addListener(
            new ChangeListener<Boolean>() {

              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {

                if (newValue) {
                  textField.setStyle(
                      "-fx-border-color: transparent transparent rgb(0,0,255) transparent; "
                          + "-fx-border-width: 3");
                } else {
                  textField.setStyle(
                      "-fx-border-color: transparent transparent #ffffff transparent;"
                          + " -fx-border-width: 1");
                }
              }
            });

    Label lbl = new Label(labelText);
    lbl.setId("normal-text");
    lbl.setWrapText(true);
    lbl.setMaxWidth(300);
    vbox.getChildren().addAll(lbl, textField);
    return vbox;
  }

  private VBox getColorPickerPane(ColorPicker colorPicker) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);

    Pane color = new Pane();
    color.setId("color-pane");
    color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
    color.setMinHeight(50);
    color.setMaxHeight(50);
    color.setMinWidth(50);
    color.setMaxWidth(50);
    colorPicker.setOnAction(
        (ActionEvent t) -> {
          color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
        });

    Label label = new Label("W�hlen Sie eine Farbe aus:");
    label.setId("normal-text");
    vbox.getChildren().addAll(label, color, colorPicker);

    return vbox;
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(50);
    button.setMaxWidth(250);
    button.setMinWidth(150);
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

  private void startMultiplayer(
      String playerName, Color playerColor, String serverAddress, int lobbyID, boolean checkbox) {
    Player player = new PlayerImpl(playerName, playerColor);

    MultiplayerControllerImpl controller = new MultiplayerControllerImpl(primaryStage);
    controller.startOnlineGame(player, serverAddress, lobbyID, checkbox);
  }

  private Color getRandomColor() {
    return new Color(Math.random(), Math.random(), Math.random(), 1.0);
  }
}
