package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.DiskImpl;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

  private ResourceBundle messages;

  private BlockingQueue<Runnable> asynchronousWorkload = new LinkedBlockingQueue<Runnable>();

  /**
   * Public constructor of this class takes the stage created by the JavaFX thread that is used to
   * display the game.
   *
   * @param primaryStage the javaFX thread's stage
   */
  public MultiplayerControllerImpl(Stage primaryStage) {
    this.mainStage = primaryStage;

    Locale locale = Locale.getDefault();
    messages = ResourceBundle.getBundle("files/MessagesBundle", locale);

    initWorkerThread();
  }

  private void initWorkerThread() {
    new Thread(
        () -> {
          while (true) {
            try {
              asynchronousWorkload.take().run();
            } catch (InterruptedException e) {
              continue;
            }
          }
        })
        .start();
    ;
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
    client.placeDisk(new DiskImpl(ownPlayer), on);
  }

  @Override
  public GameMode getCurrentGameMode() {
    return gameMode;
  }

  @Override
  public void startOnlineGame(
      Player ownPlayer, String serverAddress, int lobbyID, boolean createPrivateLobby) {
    ClientCompatibleGui gui = new ViewImpl(mainStage, null, this, messages);

    try {
      client = new ClientImpl(gui, InetAddress.getByName(serverAddress), ownPlayer);
      gui.showWaitingScreen();

      asynchronousWorkload.add(
          () -> {
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
          });

    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    gameMode = GameMode.MULTIPLAYER;
  }

  @Override
  public void requestGameReset() {
    client.requestGameRestart();
  }

  @Override
  public Player getOwnPlayer() {
    return ownPlayer;
  }

  @Override
  public void startSpectateGame(Player ownPlayer, String serverAddress, int lobbyID) {
    gameMode = GameMode.SPECTATOR;
    ClientCompatibleGui gui = new ViewImpl(mainStage, null, this, messages);

    try {
      client = new ClientImpl(gui, InetAddress.getByName(serverAddress), ownPlayer);
      gui.showWaitingScreen();

      asynchronousWorkload.add(
          () -> {
            client.startClient();

            if (lobbyID > 0) {
              client.joinSpecificLobby(true, lobbyID);
            } else {
              client.joinAnyRandomPublicLobby(true);
            }
          });
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
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
          messages.getString("ControllerImpl_ServerError_Title"),
          messages.getString("ControllerImpl_ServerError_Subtitle"),
          messages.getString("ControllerImpl_ServerError_Info"));
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
