package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class offers a pane in which two players can enter their information which is needed to
 * start a hotseat game.
 *
 * @author Josef Feger
 */
public class HotseatPlayerCreation extends BorderPane {

  private static final int HBOX_SPACING = 50;
  private static final int SPACING_VBOX_PLAYER = 40;

  private static final int PADDING_TOP_PANE = 20;
  private static final String CSS_TEXTFIELD_NORMAL =
      "-fx-border-color: transparent transparent rgb(255,0,0) transparent;";
  private static final String CSS_TEXTFIELD_SELECTED =
      "-fx-border-color: transparent transparent rgb(255,0,0) transparent;";

  private ResourceBundle messages;

  /**
   * Public constructor of this class, initialises the Resource Bundle used in this class for
   * displaying predefined text.
   *
   * @param messages the resource bundle object for externalized strings
   */
  public HotseatPlayerCreation(ResourceBundle messages) {
    this.messages = messages;
  }

  /**
   * Builds a pane in which two players can enter their information.
   *
   * @param gameModeSelector a reference to a game mode selector instance *
   * @param controller the reference to a controller instance
   * @param primaryStage the stage created by the FX application to display the actual game
   */
  void initPane(GameModeSelector gameModeSelector, Controller controller, Stage primaryStage) {

    HBox alignment = new HBox(HBOX_SPACING);
    alignment.setAlignment(Pos.CENTER);

    VBox vboxPlayerOne = new VBox(SPACING_VBOX_PLAYER);
    vboxPlayerOne.setAlignment(Pos.CENTER);
    TextField textFieldOne = new TextField();
    vboxPlayerOne
        .getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textFieldOne, messages.getString("PlayerCreation_UsernamePlayerOnePrompt")));
    ColorPicker colorPickerOne = new ColorPicker(PlayerCreationUtils.getRandomColor());
    vboxPlayerOne
        .getChildren()
        .add(PlayerCreationUtils.getColorPickerPane(colorPickerOne, messages));
    alignment.getChildren().add(vboxPlayerOne);

    alignment.getChildren().add(PlayerCreationUtils.getSeparator());

    VBox vboxPlayerTwo = new VBox(SPACING_VBOX_PLAYER);
    vboxPlayerTwo.setAlignment(Pos.CENTER);
    TextField textFieldTwo = new TextField();
    vboxPlayerTwo
        .getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textFieldTwo, messages.getString("PlayerCreation_UsernamePlayerTwoPrompt")));
    ColorPicker colorPickerTwo = new ColorPicker(PlayerCreationUtils.getRandomColor());
    vboxPlayerTwo
        .getChildren()
        .add(PlayerCreationUtils.getColorPickerPane(colorPickerTwo, messages));
    alignment.getChildren().add(vboxPlayerTwo);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(PADDING_TOP_PANE));

    ChoiceBox<String> choiceBox =
        new ChoiceBox<String>(
            FXCollections.observableArrayList(PlayerCreationUtils.gameFieldSizeOptions));
    VBox choice = PlayerCreationUtils.getGameFieldSizeSelection(choiceBox, messages);
    setCenter(choice);
    BorderPane.setAlignment(choice, Pos.TOP_CENTER);

    HBox bottom =
        buildBottomMulti(
            textFieldOne,
            textFieldTwo,
            colorPickerOne,
            colorPickerTwo,
            gameModeSelector,
            choiceBox,
            controller,
            primaryStage);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private HBox buildBottomMulti(
      TextField textFieldOne,
      TextField textFieldTwo,
      ColorPicker colorPickerOne,
      ColorPicker colorPickerTwo,
      GameModeSelector gameModeSelector,
      ChoiceBox<String> choiceBox,
      Controller controller,
      Stage primaryStage) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
    start.setOnAction(
        e -> {
          if (!PlayerCreationUtils.isTextFieldInputValid(textFieldOne.getText())) {
            textFieldOne.setStyle(CSS_TEXTFIELD_NORMAL);
            return;
          }

          if (!PlayerCreationUtils.isTextFieldInputValid(textFieldTwo.getText())) {
            textFieldTwo.setStyle(CSS_TEXTFIELD_SELECTED);
            return;
          }

          startHotseat(
              textFieldOne.getText(),
              colorPickerOne.getValue(),
              textFieldTwo.getText(),
              colorPickerTwo.getValue(),
              PlayerCreationUtils.gameFieldSizes[choiceBox.getSelectionModel().getSelectedIndex()],
              controller,
              primaryStage);
          gameModeSelector.close();
        });

    Button back =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
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

  private void startHotseat(
      String playerOneName,
      Color playerOneColor,
      String playerTwoName,
      Color playerTwoColor,
      int gameFieldSize,
      Controller controller,
      Stage primaryStage) {

    if (PlayerCreationUtils.similarTo(playerOneColor, playerTwoColor)) {
      if (PlayerCreationUtils.similarTo(playerTwoColor, Color.WHITE)) {
        playerTwoColor = Color.SILVER;
      } else if (PlayerCreationUtils.similarTo(playerTwoColor, Color.BLACK)) {
        playerTwoColor = Color.GRAY;
      } else {
        playerTwoColor = playerTwoColor.deriveColor(15, 15, 10, 1);
      }
    }
    Player playerOne = new PlayerImpl(playerOneName, playerOneColor);
    Player playerTwo = new PlayerImpl(playerTwoName, playerTwoColor);

    controller.startMainView(GameMode.HOTSEAT, primaryStage, playerOne, playerTwo, gameFieldSize);
  }
}
