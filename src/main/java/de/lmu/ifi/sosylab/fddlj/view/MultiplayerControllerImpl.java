package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.Client;
import de.lmu.ifi.sosylab.fddlj.network.ClientCompatibleGui;
import de.lmu.ifi.sosylab.fddlj.network.ClientImpl;
import de.lmu.ifi.sosylab.fddlj.network.Server;
import de.lmu.ifi.sosylab.fddlj.network.ServerImpl;
import de.lmu.ifi.sosylab.fddlj.view.server.ServerGui;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

  private GameMode gameMode;

  // private ResourceBundle messages;

  /**
   * Public constructor of this class takes the stage created by the JavaFX thread that is used to
   * display the game.
   *
   * @param primaryStage the javaFX thread's stage
   */
  public MultiplayerControllerImpl(Stage primaryStage) {
    this.mainStage = primaryStage;

    // Locale locale = Locale.getDefault();
    // messages = ResourceBundle.getBundle("MessagesBundle", locale);
  }

  @Override
  public void startMainView(
      GameMode gameMode, Stage stage, Player playerOne, Player playerTwo, int gameFieldSize) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void resetGame(GameMode gameMode, Player playerOne, Player playerTwo, int gameFieldSize) {
    requestGameReset();
  }

  @Override
  public void placeDisk(Cell on) {
    // place disk in client
  }

  @Override
  public GameMode getCurrentGameMode() {
    return gameMode;
  }

  @Override
  public void startOnlineGame(
      Player ownPlayer, String serverAddress, int lobbyID, boolean createPrivateLobby) {
    ClientCompatibleGui gui = new ViewImpl(mainStage, null, this);

    try {
      client = new ClientImpl(gui, InetAddress.getByName(serverAddress), ownPlayer);
      gui.showWaitingScreen();
      client.startClient();

      if (createPrivateLobby) {
        client.createNewPrivateLobby();
      } else {
        if (lobbyID > 0) {
          client.joinSpecificLobby(false, lobbyID);
        } else {
          client.joinAnyRandomPublicLobby(false);
        }
      }
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    gameMode = GameMode.MULTIPLAYER;
  }

  @Override
  public void requestGameReset() {
    // TODO Auto-generated method stub
  }

  @Override
  public Player getOwnPlayer() {
    return ownPlayer;
  }

  @Override
  public void startSpectateGame(Player ownPlayer, String serverAddress, int lobbyID) {
    gameMode = GameMode.SPECTATOR;
  }

  @Override
  public void startServer() {
    Server server;
    try {
      server = new ServerImpl();
      server.startServer();
    } catch (IOException e) {
      showAlert(
          AlertType.ERROR,
          "Error",
          "Error while creating server",
          "Failed to create a server instance! Maybe there is one already running?");
      return;
    }

    if (server != null) {
      ServerGui view = new ServerGui(server);
      server.addListener(view);
    }
  }

  private void showAlert(AlertType alertType, String title, String header, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.showAndWait();
  }
}
