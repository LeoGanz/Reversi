package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.AiPlayerImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
  private ResourceBundle messages;

  private Button start;

  private int[] gameFieldSizes = new int[] {8, 10, 12, 14, 16};
  private String[] gameFieldSizeOptions = new String[] {"8x8", "10x10", "12x12", "14x14", "16x16"};

  /**
   * Constructor of this class initialises variables and sets basic styling.
   *
   * @param controller the reference to a controller instance
   * @param stage the reference to the stage created by the application thread
   * @param messages the ResourceBundle for the externalised strings
   */
  public PlayerCreation(Controller controller, Stage stage, ResourceBundle messages) {
    super();

    this.controller = controller;
    this.primaryStage = stage;
    setStyle("-fx-background-color: transparent");

    this.messages = messages;
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
    vbox.getChildren()
        .add(getInputField(textField, messages.getString("PlayerCreation_UsernamePrompt")));

    ColorPicker colorPicker = new ColorPicker(Color.WHITE);
    vbox.getChildren().add(getColorPickerPane(colorPicker));

    ChoiceBox<String> choiceBox =
        new ChoiceBox<String>(FXCollections.observableArrayList(gameFieldSizeOptions));
    vbox.getChildren().add(getGameFieldSizeSelection(choiceBox));
    setCenter(vbox);

    HBox bottom = buildBottomSingle(textField, colorPicker, gameModeSelector, choiceBox);
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

    VBox vboxPlayerOne = new VBox(40);
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
        .add(
            getInputField(
                textFieldOne, messages.getString("PlayerCreation_UsernamePlayerOnePrompt")));
    ColorPicker colorPickerOne = new ColorPicker(getRandomColor());
    vboxPlayerOne.getChildren().add(getColorPickerPane(colorPickerOne));
    alignment.getChildren().add(vboxPlayerOne);

    alignment.getChildren().add(getSeparator());

    VBox vboxPlayerTwo = new VBox(40);
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
        .add(
            getInputField(
                textFieldTwo, messages.getString("PlayerCreation_UsernamePlayerTwoPrompt")));
    ColorPicker colorPickerTwo = new ColorPicker(getRandomColor());
    vboxPlayerTwo.getChildren().add(getColorPickerPane(colorPickerTwo));
    alignment.getChildren().add(vboxPlayerTwo);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(20));

    ChoiceBox<String> choiceBox =
        new ChoiceBox<String>(FXCollections.observableArrayList(gameFieldSizeOptions));
    VBox choice = getGameFieldSizeSelection(choiceBox);
    setCenter(choice);
    BorderPane.setAlignment(choice, Pos.TOP_CENTER);

    HBox bottom =
        buildBottomMulti(
            textFieldOne,
            textFieldTwo,
            colorPickerOne,
            colorPickerTwo,
            gameModeSelector,
            choiceBox);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  /**
   * Show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param gameModeSelector the reference to a gameModeSelector instance
   */
  void getOnlinePlayerInformation(GameModeSelector gameModeSelector) {
    HBox alignment = new HBox(50);
    alignment.setAlignment(Pos.CENTER);

    VBox vboxPlayer = new VBox(40);
    vboxPlayer.setAlignment(Pos.TOP_CENTER);
    TextField textField = new TextField();
    vboxPlayer
        .getChildren()
        .add(getInputField(textField, messages.getString("PlayerCreation_UsernamePrompt")));
    ColorPicker colorPicker = new ColorPicker(getRandomColor());
    vboxPlayer.getChildren().add(getColorPickerPane(colorPicker));
    alignment.getChildren().add(vboxPlayer);

    alignment.getChildren().add(getSeparator());

    VBox vboxConnection = new VBox(40);
    vboxConnection.setAlignment(Pos.TOP_CENTER);
    TextField textFieldServer = new TextField();
    vboxConnection
        .getChildren()
        .add(getInputField(textFieldServer, messages.getString("PlayerCreation_serverIPAddress")));
    TextField textFieldLobby = new TextField();
    textFieldLobby.setPromptText(messages.getString("PlayerCreation_TextFieldPrompt"));
    Tooltip tooltip = new Tooltip();
    tooltip.setText(messages.getString("PlayerCreation_TooltipLobbyId"));
    textFieldLobby.setTooltip(tooltip);
    VBox container =
        getInputField(textFieldLobby, messages.getString("PlayerCreation_LobbyIDPrompt"));
    vboxConnection.getChildren().add(container);

    Label alternateText = new Label();
    alternateText.setText(messages.getString("PlayerCreation_AlternateLobbyIDText"));
    alternateText.setMaxWidth(300);
    alternateText.setWrapText(true);
    alternateText.setFont(Font.font(15));
    alternateText.setStyle("-fx-text-fill: white");

    alignment.getChildren().add(vboxConnection);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(50));

    CheckBox checkbox =
        new CheckBox(messages.getString("PlayerCreation_PrivateLobbyCheckbox_Text"));
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
    tooltipCheckbox.setText(messages.getString("PlayerCreation_PrivateLobbyCheckbox_Tooltip"));
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

  void getSpectatorInformation(GameModeSelector gameModeSelector) {
    VBox vbox = new VBox(0);
    vbox.setAlignment(Pos.CENTER);

    TextField textField = new TextField();
    vbox.getChildren()
        .add(getInputField(textField, messages.getString("PlayerCreation_UsernamePrompt")));

    TextField textFieldServerAdress = new TextField();
    vbox.getChildren()
        .add(
            getInputField(
                textFieldServerAdress, messages.getString("PlayerCreation_serverIPAddress")));

    TextField textFieldLobbyID = new TextField();
    vbox.getChildren()
        .add(getInputField(textFieldLobbyID, messages.getString("PlayerCreation_Spectator_Lobby")));

    setTop(vbox);
    BorderPane.setAlignment(vbox, Pos.CENTER);
    BorderPane.setMargin(vbox, new Insets(50));

    HBox bottom =
        buildBottomSpectate(textField, textFieldServerAdress, textFieldLobbyID, gameModeSelector);
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
      TextField textField,
      ColorPicker colorPicker,
      GameModeSelector gameModeSelector,
      ChoiceBox<String> choiceBox) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    start = getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(textField.getText())) {
            textField.setStyle("-fx-border-color: red;");
            return;
          }
          startSinglePlayer(
              textField.getText(),
              colorPicker.getValue(),
              gameFieldSizes[choiceBox.getSelectionModel().getSelectedIndex()]);
          gameModeSelector.close();
        });

    Button back = getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
    back.setOnAction(e -> gameModeSelector.returnToMainScreen());

    textField.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });

    hbox.getChildren().addAll(start, back);
    return hbox;
  }

  private HBox buildBottomMulti(
      TextField textFieldOne,
      TextField textFieldTwo,
      ColorPicker colorPickerOne,
      ColorPicker colorPickerTwo,
      GameModeSelector gameModeSelector,
      ChoiceBox<String> choiceBox) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start = getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
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
              colorPickerTwo.getValue(),
              gameFieldSizes[choiceBox.getSelectionModel().getSelectedIndex()]);
          gameModeSelector.close();
        });

    Button back = getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
    back.setOnAction(e -> gameModeSelector.returnToMainScreen());

    textFieldOne.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    textFieldTwo.setOnKeyReleased(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });

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

    Button start = getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
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
          if (!lobbyNumber.getText().isEmpty()
              && !lobbyNumber.getText().trim().matches("(?<=\\s|^)\\d+(?=\\s|$)")) {
            lobbyNumber.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          int id = -1;
          if (!lobbyNumber.getText().isEmpty()) {
            id = Integer.parseInt(lobbyNumber.getText().trim());
          }

          startMultiplayer(
              playerName.getText(),
              playerColor.getValue(),
              serverAddress.getText(),
              id,
              checkbox.isSelected());
          selector.close();
        });

    Button back = getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
    back.setOnAction(e -> selector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);

    return hbox;
  }

  private HBox buildBottomSpectate(
      TextField playername, TextField serverAddress, TextField lobbyID, GameModeSelector selector) {
    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start = getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
    start.setOnAction(
        e -> {
          if (!isTextFieldInputValid(playername.getText())) {
            playername.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!isTextFieldInputValid(serverAddress.getText())) {
            serverAddress.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!lobbyID.getText().trim().matches("(?<=\\s|^)\\d+(?=\\s|$)")) {
            lobbyID.setStyle("-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          startSpectateMode(
              playername.getText(), serverAddress.getText(), Integer.parseInt(lobbyID.getText()));
        });

    Button back = getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
    back.setOnAction(e -> selector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);

    return hbox;
  }

  private VBox getInputField(TextField textField, String labelText) {
    VBox vbox = new VBox(0);
    vbox.setPadding(new Insets(15));
    vbox.setAlignment(Pos.CENTER);

    textField.setMinWidth(200);
    textField.setId("text-field");
    textField.setPrefWidth(200);
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
    color.setMinHeight(30);
    color.setPrefHeight(50);
    color.setMaxHeight(50);
    color.setMinWidth(30);
    color.setPrefWidth(50);
    color.setMaxWidth(50);
    colorPicker.setOnAction(
        (ActionEvent t) -> {
          color.setStyle("-fx-background-color: " + toRgbCode(colorPicker.getValue()) + ";");
        });

    Label label = new Label(messages.getString("PlayerCreation_ChooseColor"));
    label.setId("normal-text");
    vbox.getChildren().addAll(label, color, colorPicker);

    return vbox;
  }

  private Button getButton(String text) {
    Button button = new Button(text);
    button.setId("button");
    button.setMinHeight(30);
    button.setMaxWidth(250);
    button.setMinWidth(100);
    button.setPrefWidth(150);
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

  private void startSinglePlayer(String playerName, Color playerColor, int gameFieldSize) {
    Player player = new PlayerImpl(playerName, playerColor);

    Color aiColor;
    if (similarTo(playerColor, Color.BLACK)) {
      aiColor = Color.WHITE;
    } else {
      aiColor = Color.BLACK;
    }
    Player ai = new AiPlayerImpl("AI", aiColor);

    controller.startMainView(GameMode.SINGLEPLAYER, primaryStage, player, ai, gameFieldSize);
  }

  private void startHotseat(
      String playerOneName,
      Color playerOneColor,
      String playerTwoName,
      Color playerTwoColor,
      int gameFieldSize) {

    if (similarTo(playerOneColor, playerTwoColor)) {
      if (similarTo(playerTwoColor, Color.WHITE)) {
        playerTwoColor = Color.SILVER;
      } else if (similarTo(playerTwoColor, Color.BLACK)) {
        playerTwoColor = Color.GRAY;
      } else {
        playerTwoColor = playerTwoColor.deriveColor(15, 15, 10, 1);
      }
    }
    Player playerOne = new PlayerImpl(playerOneName, playerOneColor);
    Player playerTwo = new PlayerImpl(playerTwoName, playerTwoColor);

    controller.startMainView(GameMode.HOTSEAT, primaryStage, playerOne, playerTwo, gameFieldSize);
  }

  private void startMultiplayer(
      String playerName, Color playerColor, String serverAddress, int lobbyID, boolean checkbox) {
    Player player = new PlayerImpl(playerName, playerColor);

    MultiplayerControllerImpl controller = new MultiplayerControllerImpl(primaryStage);
    controller.startOnlineGame(player, serverAddress, lobbyID, checkbox);
  }

  private void startSpectateMode(String playerName, String serverAddress, int lobbyID) {
    Player spectator = new PlayerImpl(playerName, getRandomColor());

    MultiplayerControllerImpl controller = new MultiplayerControllerImpl(primaryStage);
    controller.startSpectateGame(spectator, serverAddress, lobbyID);
  }

  private Color getRandomColor() {
    return new Color(Math.random(), Math.random(), Math.random(), 1.0);
  }

  private VBox getGameFieldSizeSelection(ChoiceBox<String> choiceBox) {
    VBox vbox = new VBox(5);
    vbox.setAlignment(Pos.CENTER);

    Label label = new Label(messages.getString("PlayerCreation_ChooseGameFieldSize"));
    label.setId("normal-text");
    vbox.getChildren().add(label);

    choiceBox.getSelectionModel().select(0);
    vbox.getChildren().add(choiceBox);

    return vbox;
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
}
