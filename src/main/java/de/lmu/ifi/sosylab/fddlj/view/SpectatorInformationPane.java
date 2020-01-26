package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class offers a pane in which a user can enter all the necessary information to start
 * spectating an online game.
 *
 * @author Josef Feger
 */
public class SpectatorInformationPane extends BorderPane {

  private ResourceBundle messages;

  /**
   * Public constructor of this class, initialises the Resource Bundle used in this class for
   * displaying predefined text.
   *
   * @param messages the resource bundle object for externalized strings
   */
  public SpectatorInformationPane(ResourceBundle messages) {
    this.messages = messages;
  }

  /**
   * Builds a pane in which two players can enter their information.
   *
   * @param gameModeSelector a reference to a game mode selector instance *
   * @param primaryStage the stage created by the FX application to display the actual game
   */
  void initPane(GameModeSelector gameModeSelector, Stage primaryStage) {
    VBox vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);

    TextField textField = new TextField();
    vbox.getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textField, messages.getString("PlayerCreation_UsernamePrompt")));

    TextField textFieldServerAdress = new TextField();
    vbox.getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textFieldServerAdress, messages.getString("PlayerCreation_serverIPAddress")));

    TextField textFieldLobbyID = new TextField();
    vbox.getChildren()
        .add(
            PlayerCreationUtils.getInputField(
                textFieldLobbyID, messages.getString("PlayerCreation_Spectator_Lobby")));

    setTop(vbox);
    BorderPane.setAlignment(vbox, Pos.CENTER);
    BorderPane.setMargin(vbox, new Insets(50));

    HBox bottom =
        buildBottomSpectate(
            textField, textFieldServerAdress, textFieldLobbyID, gameModeSelector, primaryStage);
    setBottom(bottom);
    BorderPane.setAlignment(bottom, Pos.CENTER);
    BorderPane.setMargin(bottom, new Insets(0, 0, 20, 0));
  }

  private HBox buildBottomSpectate(
      TextField playername,
      TextField serverAddress,
      TextField lobbyID,
      GameModeSelector selector,
      Stage primaryStage) {
    HBox hbox = new HBox(30);
    hbox.setAlignment(Pos.CENTER);

    Button start =
        PlayerCreationUtils.getButton(messages.getString("PlayerCreation_ButtonStart_Text"));
    start.setOnAction(
        e -> {
          if (!PlayerCreationUtils.isTextFieldInputValid(playername.getText())) {
            playername.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!PlayerCreationUtils.isTextFieldInputValid(serverAddress.getText())) {
            serverAddress.setStyle(
                "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
            return;
          }

          if (!lobbyID.getText().isEmpty()) {
            if (!lobbyID.getText().trim().matches("(?<=\\s|^)\\d+(?=\\s|$)")) {
              lobbyID.setStyle(
                  "-fx-border-color: transparent transparent rgb(255,0,0) transparent;");
              return;
            }
          }

          int lobbyId;
          if (!lobbyID.getText().isEmpty()) {
            lobbyId = Integer.parseInt(lobbyID.getText());
          } else {
            lobbyId = -1;
          }
          startSpectateMode(playername.getText(), serverAddress.getText(), lobbyId, primaryStage);
          selector.close();
        });

    playername.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    serverAddress.setOnKeyPressed(
        e -> {
          if (e.getCode() == KeyCode.ENTER) {
            start.fire();
          }
        });
    lobbyID.setOnKeyPressed(
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

  private void startSpectateMode(
      String playerName, String serverAddress, int lobbyID, Stage primaryStage) {
    Player spectator = new PlayerImpl(playerName, PlayerCreationUtils.getRandomColor());

    MultiplayerControllerImpl controller = new MultiplayerControllerImpl(primaryStage);
    controller.startSpectateGame(spectator, serverAddress, lobbyID);
  }
}
