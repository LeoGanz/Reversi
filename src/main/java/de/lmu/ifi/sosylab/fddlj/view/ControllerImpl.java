package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.DiskImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.Server;
import de.lmu.ifi.sosylab.fddlj.network.ServerImpl;
import de.lmu.ifi.sosylab.fddlj.view.server.ServerGui;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * An implementation of the interface {@link Controller} which offers minimal additional
 * functionality.
 *
 * @author Josef Feger
 */
public class ControllerImpl extends Application implements Controller {

  private Model model;
  private View view;

  private GameMode gameMode;
  private Locale locale;

  private ResourceBundle messages;

  /**
   * Public constructor of this class takes a reference to the resource bundle containing the
   * externalized strings.
   *
   * @param messages the ResourceBundle for the externalised strings
   */
  public ControllerImpl(ResourceBundle messages) {
    this.messages = messages;
  }

  /** Public constructor of this class initialises the ResourceBundle. */
  public ControllerImpl() {
    locale = Locale.getDefault();
    messages = ResourceBundle.getBundle("files/MessagesBundle", locale);
  }

  @Override
  public void startMainView(
      GameMode gameMode, Stage stage, Player playerOne, Player playerTwo, int gameFieldSize) {

    this.gameMode = gameMode;

    model = new ModelImpl(gameMode, gameFieldSize, playerOne, playerTwo);
    view = new ViewImpl(stage, model, this, messages);
    model.addListener(view);
    view.showGame(gameMode);
  }

  @Override
  public void resetGame(GameMode gameMode, Player playerOne, Player playerTwo, int gameFieldSize) {
    model = new ModelImpl(gameMode, gameFieldSize, playerOne, playerTwo);
    model.addListener(view);

    this.gameMode = gameMode;
  }

  @Override
  public void placeDisk(Cell on) {
    Disk disk = new DiskImpl(model.getState().getPlayerManagement().getCurrentPlayer());
    new Thread(
        () -> {
          boolean succesful = model.placeDisk(disk, on);

          if (!succesful) {
            showAlert(
                AlertType.ERROR,
                messages.getString("ControllerImpl_DiskError_Title"),
                messages.getString("ControllerImpl_DiskError_Subtitle"),
                messages.getString("ControllerImpl_DiskError_Info"));
          }
        })
        .start();
  }

  private void showAlert(AlertType alertType, String title, String header, String content) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.showAndWait();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    showGameModeSelector(primaryStage);
  }

  void showGameModeSelector(Stage primaryStage) {
    GameModeSelector gms = new GameModeSelector(this, primaryStage, messages);
    gms.showGameModeSelection();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public GameMode getCurrentGameMode() {
    return gameMode;
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

  @Override
  public void continueAgainstAi(Model model) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void close() {
    view.close();
  }
}
