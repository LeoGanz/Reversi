package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.AiPlayerImpl;
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
 * This class offers a pane in which the player can enter information needed for a single player
 * game.
 *
 * @author Josef Feger
 */
public class SinglePlayerInformationPane extends BorderPane {

  private static final int SPACING_VBOX = 20;

  private ResourceBundle messages;

  /**
   * Public constructor of this class, initialises the Resource Bundle used in this class for
   * displaying predefined text.
   *
   * @param messages the resource bundle object for externalized strings
   */
  public SinglePlayerInformationPane(ResourceBundle messages) {
    this.messages = messages;
  }

  /**
   * Show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param controller the reference to a controller instance
   * @param gameModeSelector the reference to a gameModeSelector instance
   * @param primaryStage the stage created by the FX application to display the actual game
   */
  void initPane(Controller controller, GameModeSelector gameModeSelector, Stage primaryStage) {
    VBox vbox = new VBox(SPACING_VBOX);
    vbox.setAlignment(Pos.CENTER);

    TextField textField = new TextField();
    vbox.getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textField, messages.getString("PlayerCreation_UsernamePrompt")));

    ColorPicker colorPicker = new ColorPicker(Color.WHITE);
    vbox.getChildren().add(PlayerCreationUtils.getColorPickerPane(colorPicker, messages));

    ChoiceBox<String> choiceBox =
        new ChoiceBox<String>(
            FXCollections.observableArrayList(PlayerCreationUtils.gameFieldSizeOptions));
    vbox.getChildren().add(PlayerCreationUtils.getGameFieldSizeSelection(choiceBox, messages));
    setCenter(vbox);

    HBox bottom =
        buildBottomSingle(
            textField, colorPicker, gameModeSelector, choiceBox, controller, primaryStage);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private HBox buildBottomSingle(
      TextField textField,
      ColorPicker colorPicker,
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
          if (!PlayerCreationUtils.isTextFieldInputValid(textField.getText())) {
            textField.setStyle("-fx-border-color: red;");
            return;
          }
          startSinglePlayer(
              textField.getText(),
              colorPicker.getValue(),
              PlayerCreationUtils.gameFieldSizes[choiceBox.getSelectionModel().getSelectedIndex()],
              controller,
              primaryStage);
          gameModeSelector.close();
        });

    Button back =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
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

  private void startSinglePlayer(
      String playerName,
      Color playerColor,
      int gameFieldSize,
      Controller controller,
      Stage primaryStage) {
    Player player = new PlayerImpl(playerName, playerColor);

    Color aiColor;
    if (PlayerCreationUtils.similarTo(playerColor, Color.BLACK)) {
      aiColor = Color.WHITE;
    } else {
      aiColor = Color.BLACK;
    }
    Player ai = new AiPlayerImpl("AI", aiColor);

    controller.startMainView(GameMode.SINGLEPLAYER, primaryStage, player, ai, gameFieldSize);
  }
}
