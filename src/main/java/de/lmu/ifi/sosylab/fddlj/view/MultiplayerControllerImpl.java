package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.Client;
import javafx.stage.Stage;

/**
 * This class represents the implementation of the {@link MultiplayerController} interface and is
 * responsible for setting up and managing an online game.
 *
 * @author Josef Feger
 */
public class MultiplayerControllerImpl implements MultiplayerController {

  private Client client;
  private Stage mainStage;
  private Player ownPlayer;

  /**
   * Public constructor of this class takes the stage created by the JavaFX thread that is used to
   * display the game.
   *
   * @param primaryStage the javaFX thread's stage
   */
  public MultiplayerControllerImpl(Stage primaryStage) {
    this.mainStage = primaryStage;
  }

  @Override
  public void startMainView(GameMode gameMode, Stage stage, Player playerOne, Player playerTwo) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void resetGame(GameMode gameMode, Player playerOne, Player playerTwo) {
    requestGameReset();
  }

  @Override
  public void placeDisk(Cell on) {
    // place disk in client
  }

  @Override
  public GameMode getCurrentGameMode() {
    return GameMode.MULTIPLAYER;
  }

  @Override
  public void startOnlineGame(
      Player ownPlayer, String serverAddress, int lobbyID, boolean createPrivateLobby) {
    // create client
    // create view
    // connect to server

  }

  @Override
  public void requestGameReset() {
    // TODO Auto-generated method stub
  }

  @Override
  public Player getOwnPlayer() {
    return ownPlayer;
  }
}
