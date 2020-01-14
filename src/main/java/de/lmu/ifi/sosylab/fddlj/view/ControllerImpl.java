package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.DiskImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.beans.PropertyChangeEvent;
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

  @Override
  public void startMainView(GameMode gameMode, Stage stage, Player playerOne, Player playerTwo) {

    this.gameMode = gameMode;

    model = new ModelImpl(gameMode, playerOne, playerTwo);
    view = new ReversiView(stage, model, this);
    model.addListener(view);
    view.showGame(gameMode);
  }

  @Override
  public void resetGame(GameMode gameMode, Player playerOne, Player playerTwo) {
    model = new ModelImpl(gameMode, playerOne, playerTwo);
    model.addListener(view);

    this.gameMode = gameMode;
  }

  @Override
  public boolean placeDisk(Cell on) {
    Disk disk = new DiskImpl(model.getState().getPlayerManagement().getCurrentPlayer());
    boolean succesful = model.placeDisk(disk, on);

    if (!succesful) {
      showAlert(
          AlertType.ERROR,
          "Error",
          "Error while placing disk",
          "The placement of your disk on this cell resulted in an error. Please try again!");
    }
    return succesful;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      // TODO update where needed
      return;
    }
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
    GameModeSelector gms = new GameModeSelector(this, primaryStage);
    gms.showGameModeSelection();
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public GameMode getCurrentGameMode() {
    return gameMode;
  }
}
