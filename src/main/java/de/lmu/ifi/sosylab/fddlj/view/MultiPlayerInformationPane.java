package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Offers a pane in which a user can enter all the necessary information to start an online game.
 *
 * @author Josef Feger
 */
public class MultiPlayerInformationPane extends BorderPane {

  private static final int SPACING_HBOX = 50;
  private static final int SPACING_VBOXES = 40;

  private ResourceBundle messages;

  /**
   * Public constructor of this class, initialises the Resource Bundle used in this class for
   * displaying predefined text.
   *
   * @param messages the resource bundle object for externalized strings
   */
  public MultiPlayerInformationPane(ResourceBundle messages) {
    this.messages = messages;
  }

  /**
   * Show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param gameModeSelector the reference to a gameModeSelector instance
   * @param primaryStage the stage created by the FX application to display the actual game
   */
  void initPane(GameModeSelector gameModeSelector, Stage primaryStage) {
    HBox alignment = new HBox(SPACING_HBOX);
    alignment.setAlignment(Pos.CENTER);

    VBox vboxPlayer = new VBox(SPACING_VBOXES);
    vboxPlayer.setAlignment(Pos.TOP_CENTER);
    TextField textField = new TextField();
    vboxPlayer
        .getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textField, messages.getString("PlayerCreation_UsernamePrompt")));
    ColorPicker colorPicker = new ColorPicker(PlayerCreationUtils.getRandomColor());
    vboxPlayer.getChildren().add(PlayerCreationUtils.getColorPickerPane(colorPicker, messages));
    alignment.getChildren().add(vboxPlayer);

    alignment.getChildren().add(PlayerCreationUtils.getSeparator());

    VBox vboxConnection = new VBox(SPACING_VBOXES);
    vboxConnection.setAlignment(Pos.TOP_CENTER);
    TextField textFieldServer = new TextField();
    vboxConnection
        .getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textFieldServer, messages.getString("PlayerCreation_serverIPAddress")));
    TextField textFieldLobby = new TextField();
    textFieldLobby.setPromptText(messages.getString("PlayerCreation_TextFieldPrompt"));
    Tooltip tooltip = new Tooltip();
    tooltip.setText(messages.getString("PlayerCreation_TooltipLobbyId"));
    tooltip.setFont(Font.font(14));
    textFieldLobby.setTooltip(tooltip);
    VBox container =
        PlayerCreationUtils.getInputField(
            textFieldLobby, messages.getString("PlayerCreation_LobbyIDPrompt"));
    vboxConnection.getChildren().add(container);

    Label alternateText = new Label();
    alternateText.setText(messages.getString("PlayerCreation_AlternateLobbyIDText"));
    alternateText.setMaxWidth(300);
    alternateText.setWrapText(true);
    alternateText.setId("alternateText");

    alignment.getChildren().add(vboxConnection);

    setTop(alignment);
    BorderPane.setAlignment(alignment, Pos.CENTER);
    BorderPane.setMargin(alignment, new Insets(50));

    CheckBox checkbox =
        new CheckBox(messages.getString("PlayerCreation_PrivateLobbyCheckbox_Text"));
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
    tooltipCheckbox.setFont(Font.font(14));
    checkbox.setTooltip(tooltipCheckbox);
    setCenter(checkbox);
    BorderPane.setAlignment(checkbox, Pos.TOP_CENTER);
    BorderPane.setMargin(checkbox, new Insets(20));

    HBox bottom =
        buildBottomOnline(
            textField,
            colorPicker,
            textFieldServer,
            textFieldLobby,
            checkbox,
            gameModeSelector,
            primaryStage);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private HBox buildBottomOnline(
      TextField playerName,
      ColorPicker playerColor,
      TextField serverAddress,
      TextField lobbyNumber,
      CheckBox checkbox,
      GameModeSelector selector,
      Stage primaryStage) {

    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
    start.setOnAction(
        e -> {
          if (!PlayerCreationUtils.isTextFieldInputValid(playerName.getText())) {
            playerName.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!PlayerCreationUtils.isTextFieldInputValid(serverAddress.getText())) {
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
              checkbox.isSelected(),
              primaryStage);
          selector.close();
        });

    serverAddress.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    playerName.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    lobbyNumber.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });

    Button back =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonBack_Text"));
    back.setOnAction(e -> selector.returnToMainScreen());

    hbox.getChildren().addAll(start, back);

    return hbox;
  }

  private void startMultiplayer(
      String playerName,
      Color playerColor,
      String serverAddress,
      int lobbyID,
      boolean checkbox,
      Stage primaryStage) {
    Player player = new PlayerImpl(playerName, playerColor);

    MultiplayerControllerImpl controller = new MultiplayerControllerImpl(primaryStage);
    controller.startOnlineGame(player, serverAddress, lobbyID, checkbox);
  }
}
