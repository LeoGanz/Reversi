package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.AiPlayerImpl;
import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.DiskImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
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
import javafx.scene.paint.Color;
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
  private View view;
  private Model model;

  private GameMode gameMode;

  private ResourceBundle messages;

  private BlockingQueue<Runnable> asynchronousWorkloads;

  /**
   * Public constructor of this class takes the stage created by the JavaFX thread that is used to
   * display the game.
   *
   * @param primaryStage the javaFX thread's stage
   */
  public MultiplayerControllerImpl(Stage primaryStage) {
    this.mainStage = primaryStage;

    asynchronousWorkloads = new LinkedBlockingQueue<>();

    Locale locale = Locale.getDefault();
    messages = ResourceBundle.getBundle("files/MessagesBundle", locale);

    initWorkerThread();
  }

  private void initWorkerThread() {
    Thread worker =
        new Thread(
            () -> {
              while (true) {
                try {
                  asynchronousWorkloads.take().run();
                } catch (InterruptedException e) {
                  continue;
                }
              }
            });
    worker.setDaemon(true);
    worker.start();
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
    if (gameMode == GameMode.MULTIPLAYER) {
      client.placeDisk(new DiskImpl(ownPlayer), on);
    } else {
      model.placeDisk(new DiskImpl(model.getState().getPlayerManagement().getCurrentPlayer()), on);
    }
  }

  @Override
  public GameMode getCurrentGameMode() {
    return gameMode;
  }

  @Override
  public void startOnlineGame(
      Player ownPlayer, String serverAddress, int lobbyID, boolean createPrivateLobby) {
    this.ownPlayer = ownPlayer;
    ClientCompatibleGui gui = new ViewImpl(mainStage, null, this, messages);
    view = gui;

    try {
      client = new ClientImpl(gui, InetAddress.getByName(serverAddress), ownPlayer);
      gui.showWaitingScreen();

      asynchronousWorkloads.add(
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
      showAlert(
          AlertType.ERROR,
          messages.getString("ControllerImpl_ConnectionError_Title"),
          messages.getString("ControllerImpl_ConnectionError_Subtitle"),
          messages.getString("ControllerImpl_ConnectionError_Info"));

      Controller controller = new ControllerImpl(messages);
      ((ControllerImpl) controller).showGameModeSelector(new Stage());
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
    this.ownPlayer = ownPlayer;
    ClientCompatibleGui gui = new ViewImpl(mainStage, null, this, messages);
    view = gui;

    try {
      client = new ClientImpl(gui, InetAddress.getByName(serverAddress), ownPlayer);
      gui.showWaitingScreen();

      asynchronousWorkloads.add(
          () -> {
            client.startClient();

            if (lobbyID > 0) {
              client.joinSpecificLobby(true, lobbyID);
            } else {
              client.joinAnyRandomPublicLobby(true);
            }
          });
    } catch (UnknownHostException e) {
      showAlert(
          AlertType.ERROR,
          messages.getString("ControllerImpl_ConnectionError_Title"),
          messages.getString("ControllerImpl_ConnectionError_Subtitle"),
          messages.getString("ControllerImpl_ConnectionError_Info"));

      Controller controller = new ControllerImpl(messages);
      ((ControllerImpl) controller).showGameModeSelector(new Stage());
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

    ServerGui view = new ServerGui(server);
    server.addListener(view);
  }

  private void showAlert(AlertType alertType, String title, String header, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.showAndWait();
  }

  @Override
  public void continueAgainstAi(Model model) {
    client.terminate();

    gameMode = GameMode.SINGLEPLAYER;
    view.showGame(gameMode);
    this.model = new ModelImpl(model.getState(), gameMode);

    Player aiPlayer;
    if (ownPlayer.getColor().equals(Color.BLACK)) {
      aiPlayer = new AiPlayerImpl(messages.getString("PlayerCreation_AIPlayer_Name"), Color.WHITE);
    } else {
      aiPlayer = new AiPlayerImpl();
    }
    this.model.substitutePlayersWith(ownPlayer, aiPlayer);
    this.model.unsetWaiting();
    this.model.addListener(view);
    if (view instanceof ClientCompatibleGui) {
      ((ClientCompatibleGui) view).modelExchanged(this.model);
    }
  }

  @Override
  public void acceptGameRestart() {
    client.acceptGameRestart();
  }

  @Override
  public void denyGameRestart() {
    client.denyGameRestart();
  }

  @Override
  public void close() {
    if (client != null) {
      client.terminate();
    }
    view.close();
  }
}
