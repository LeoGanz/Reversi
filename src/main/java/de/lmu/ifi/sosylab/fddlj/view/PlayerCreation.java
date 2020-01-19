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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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
   * @param multiplayer {@code true} if the selected game mode is multi player, {@code
   *     false}otherwise
   */
  void getSinglePlayerInformation(
      Controller controller, GameModeSelector gameModeSelector, boolean multiplayer) {
    VBox vbox = new VBox(50);
    vbox.setAlignment(Pos.CENTER);

    TextField textField = new TextField();
    textField.setOnKeyTyped(e -> {
	if (e.getCode() == KeyCode.ENTER) {
	    start.fire();
	}
    });  
    vbox.getChildren().add(getUsernameInputField(textField, "Geben Sie Ihren Usernamen ein:"));

    ColorPicker colorPicker = new ColorPicker(Color.WHITE);
    vbox.getChildren().add(getColorPickerPane(colorPicker));
    setCenter(vbox);

    HBox bottom = buildBottomSingle(textField, colorPicker, gameModeSelector, multiplayer);
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
    textFieldOne.setOnKeyTyped(e -> {
	if (e.getCode() == KeyCode.ENTER) {
	    start.fire();
	}
    });  
    vboxPlayerOne
        .getChildren()
        .add(getUsernameInputField(textFieldOne, "Geben Sie den Usernamen von Spieler 1 ein:"));
    ColorPicker colorPickerOne = new ColorPicker(getRandomColor());
    vboxPlayerOne.getChildren().add(getColorPickerPane(colorPickerOne));
    alignment.getChildren().add(vboxPlayerOne);

    Separator sep = new Separator(Orientation.VERTICAL);
    sep.setStyle("-fx-background-color: #d4d4d4; -fx-background-radius: 1; -fx-border-width: 1;");
    sep.setMaxHeight(450);
    alignment.getChildren().add(sep);

    VBox vboxPlayerTwo = new VBox(50);
    vboxPlayerTwo.setAlignment(Pos.CENTER);
    TextField textFieldTwo = new TextField();
    textFieldTwo.setOnKeyReleased(e -> {
	if (e.getCode() == KeyCode.ENTER) {
	    start.fire();
	}
    });    
    vboxPlayerTwo
        .getChildren()
        .add(getUsernameInputField(textFieldTwo, "Geben Sie den Usernamen von Spieler 2 ein:"));
    ColorPicker colorPickerTwo = new ColorPicker(getRandomColor());
    vboxPlayerTwo.getChildren().add(getColorPickerPane(colorPickerTwo));
    alignment.getChildren().add(vboxPlayerTwo);

    setCenter(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(50));

    HBox bottom =
        buildBottomMulti(
            textFieldOne, textFieldTwo, colorPickerOne, colorPickerTwo, gameModeSelector);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private HBox buildBottomSingle(
      TextField textField,
      ColorPicker colorPicker,
      GameModeSelector gameModeSelector,
      boolean multiplayer) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    start = getButton("Start");
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

  private VBox getUsernameInputField(TextField textField, String labelText) {
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

    Label label = new Label("Wählen Sie eine Farbe aus:");
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

  private void startMultiplayer(String playerName, Color playerColor) {
    Player player = new PlayerImpl(playerName, playerColor);

    controller.startMainView(GameMode.MULTIPLAYER, primaryStage, player, null);
  }

  private Color getRandomColor() {
    return new Color(Math.random(), Math.random(), Math.random(), 1.0);
  }
}
