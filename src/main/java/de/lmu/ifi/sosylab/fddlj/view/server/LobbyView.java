package de.lmu.ifi.sosylab.fddlj.view.server;

import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.GameLobby;
import de.lmu.ifi.sosylab.fddlj.network.LobbyRepresentation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * This class gives a visual representation of a {@link GameLobby} by showing the lobbie's status
 * and the players in the lobby.
 *
 * @author Josef Feger
 */
public class LobbyView extends BorderPane {

  /**
   * Public constructor of this class initialises the view for the initial lobby.
   *
   * @param lobby the lobby for which to initialise the view
   */
  public LobbyView(LobbyRepresentation lobby) {
    setStyle("-fx-background-color: rgba(180,180,180,160); -fx-padding: 20;");

    initView(lobby);
  }

  private void initView(LobbyRepresentation lobby) {
    Image state;
    if (lobby.getGamePhase() == Phase.RUNNING) {
      state = new Image(getClass().getClassLoader().getResourceAsStream("images/running.png"));
    } else if (lobby.getGamePhase() == Phase.WAITING) {
      state = new Image(getClass().getClassLoader().getResourceAsStream("images/waiting.png"));
    } else {
      state = new Image(getClass().getClassLoader().getResourceAsStream("images/stopped.png"));
    }

    ImageView imageView = new ImageView(state);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(50);

    setLeft(imageView);
    BorderPane.setAlignment(imageView, Pos.CENTER);

    Label title = new Label("Lobby #" + lobby.getLobbyID());
    title.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: x-large;");
    setTop(title);
    BorderPane.setAlignment(title, Pos.CENTER);

    HBox opponents = new HBox(15);
    opponents.setAlignment(Pos.CENTER);
    opponents.getChildren().add(getPlayerView(lobby.getPlayerOne()));

    Label vs = new Label("vs.");
    vs.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: x-large;");
    opponents.getChildren().add(vs);
    opponents.getChildren().add(getPlayerView(lobby.getPlayerTwo()));
    setCenter(opponents);
  }

  private HBox getPlayerView(Player player) {
    HBox hbox = new HBox(20);
    hbox.setAlignment(Pos.CENTER_LEFT);
    hbox.setPadding(new Insets(15));
    hbox.setStyle(
        "-fx-border-color: "
            + toHexString(player.getColor())
            + "; -fx-border-width: 2; -fx-background-color: rgba(160,160,160,200);");

    Image profile =
        new Image(getClass().getClassLoader().getResourceAsStream("images/profile.png"));
    ImageView imageView = new ImageView(profile);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(40);
    imageView.setStyle(
        "-fx-border-color: #000000; -fx-border-radius: 8; -fx-background-radius: 8;");

    hbox.getChildren().add(imageView);

    Label name = new Label(player.getName());
    name.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold; -fx-font-size: 30;");
    hbox.getChildren().add(name);

    return hbox;
  }

  /**
   * Updates the lobby view according to the given {@link LobbyRepresentation} object.
   *
   * @param lobby the lobby representation object which to base the update on
   */
  void updateLobby(LobbyRepresentation lobby) {
    initView(lobby);
  }

  private String toHexString(Color value) {
    return "#"
        + (format(value.getRed())
                + format(value.getGreen())
                + format(value.getBlue())
                + format(value.getOpacity()))
            .toUpperCase();
  }

  private String format(double val) {
    String in = Integer.toHexString((int) Math.round(val * 255));
    return in.length() == 1 ? "0" + in : in;
  }
}
