package de.lmu.ifi.sosylab.fddlj.view;

import java.util.ResourceBundle;
import javafx.scene.layout.BorderPane;
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
    getStyleClass().add("transparent");

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
    SinglePlayerInformationPane singlePlayerInformationPane =
        new SinglePlayerInformationPane(messages);
    singlePlayerInformationPane.initPane(controller, gameModeSelector, primaryStage);

    setCenter(singlePlayerInformationPane);
  }

  /**
   * Builds a pane in which two players can enter their information.
   *
   * @param gameModeSelector a reference to a game mode selector instance
   */
  void getMultiplePlayersInformation(GameModeSelector selector) {
    HotseatPlayerCreation hotseatPlayerCreation = new HotseatPlayerCreation(messages);
    hotseatPlayerCreation.initPane(selector, controller, primaryStage);

    setCenter(hotseatPlayerCreation);
  }

  /**
   * show a pane that allow a single player to enter his/her information, such as name and preferred
   * color for the disks.
   *
   * @param gameModeSelector the reference to a GameModeSelector instance
   */
  void getOnlinePlayerInformation(GameModeSelector gameModeSelector) {
    MultiPlayerInformationPane multiPlayerInformationPane =
        new MultiPlayerInformationPane(messages);
    multiPlayerInformationPane.initPane(gameModeSelector, primaryStage);

    setCenter(multiPlayerInformationPane);
  }

  void getSpectatorInformation(GameModeSelector gameModeSelector) {
    SpectatorInformationPane spectatorInformationPane = new SpectatorInformationPane(messages);
    spectatorInformationPane.initPane(gameModeSelector, primaryStage);

    setCenter(spectatorInformationPane);
  }
}
