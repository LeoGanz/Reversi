package de.lmu.ifi.sosylab.fddlj.view.server;

import de.lmu.ifi.sosylab.fddlj.network.LobbyRepresentation;
import de.lmu.ifi.sosylab.fddlj.network.Server;
import de.lmu.ifi.sosylab.fddlj.network.ServerListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class offers a simple GUI for the server in which the user sees the different active lobbies
 * and their respective status and he also can shut down the server.
 *
 * @author Josef Feger
 */
public class ServerGui implements ServerListener {

  private Server server;
  private Map<Integer, LobbyView> lobbies;

  private Stage stage;
  private BorderPane root;
  private VBox lobbyList;

  /**
   * Public constructor of this class initialises the GUI and attaches it to the given server.
   *
   * @param server the server instance for which to create the GUI.
   */
  public ServerGui(Server server) {
    this.server = server;
    lobbies = new HashMap<>();

    server.addListener(this);

    initGui();
  }

  private void initGui() {
    stage = new Stage();

    root = new BorderPane();
    root.setStyle("-fx-background-color: #576385; -fx-padding: 20;");

    Button button = new Button("Stop server");
    button.setStyle("-fx-background-color: #46729c;-fx-text-fill: #ffffff;");
    button.setMinHeight(50);
    button.setMaxWidth(350);
    button.setMinWidth(150);
    button.setCursor(Cursor.HAND);
    button.setFont(Font.font(18));
    button.setEffect(new DropShadow(4, Color.DARKGRAY));
    button.setOnAction(
        e -> {
          server.initiateShutdown();
          stage.close();
        });

    root.setLeft(button);
    BorderPane.setAlignment(button, Pos.CENTER);

    lobbyList = new VBox(15);
    lobbyList.setStyle("-fx-background-color: rgba(160,160,160,150)");

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(lobbyList);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(600);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    root.setCenter(lobbyList);
    BorderPane.setMargin(scrollPane, new Insets(0, 0, 0, 20));

    Scene scene = new Scene(root);

    stage.setScene(scene);
    stage.setWidth(800);
    stage.setHeight(500);
    stage.show();

    stage.setOnCloseRequest(e -> server.initiateShutdown());
  }

  private void buildLobbies(Map<Integer, LobbyRepresentation> lobbies) {

    lobbyList.getChildren().clear();
    this.lobbies.clear();

    for (Entry<Integer, LobbyRepresentation> id : lobbies.entrySet()) {
      LobbyRepresentation lobby = id.getValue();
      LobbyView lobbyView = new LobbyView(lobby);
      lobbyList.getChildren().add(lobbyView);
      this.lobbies.put(lobby.getLobbyID(), lobbyView);
    }
  }

  @Override
  public void serverShuttingDown() {
    Platform.runLater(
        new Runnable() {

          @Override
          public void run() {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Server");
            alert.setHeaderText("Shutting down.");
            alert.setContentText("The server is shutting down");

            alert.show();

            stage.close();
          }
        });
  }

  @Override
  public void lobbyUpdated(int lobbyID, LobbyRepresentation lobbyRepresentation) {
    Platform.runLater(
        () -> {
          if (lobbyRepresentation == null) {
            lobbyList.getChildren().remove(lobbies.get(lobbyID));
            return;
          }

          if (lobbies.get(lobbyID) != null) {
            lobbies.get(lobbyID).updateLobby(lobbyRepresentation);
          } else {
            LobbyView lobbyView = new LobbyView(lobbyRepresentation);
            lobbies.put(lobbyRepresentation.getLobbyID(), lobbyView);
            lobbyList.getChildren().add(lobbyView);
          }
        });
  }

  @Override
  public void allLobbies(Map<Integer, LobbyRepresentation> lobbies) {

    Platform.runLater(() -> buildLobbies(lobbies));
  }
}
