package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.Client;
import javafx.stage.Stage;

public class MultiplayerControllerImpl implements MultiplayerController {

  private Client client;
  private Stage mainStage;
  private Player ownPlayer;

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
