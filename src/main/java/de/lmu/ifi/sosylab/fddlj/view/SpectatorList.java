package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class offers a pane for the multiplayer view in which all spectators currently watching the
 * game are being displayed.
 *
 * @author Josef Feger
 */
public class SpectatorList extends BorderPane implements PropertyChangeListener {

  private ResourceBundle messages;

  private static final int SPACING_VBOX_SPECTATORLIST = 15;
  private static final int SPACING_HBOX_PLAYER = 20;
  private static final Insets PADDING_HBOX_PLAYER = new Insets(15);
  private static final int PREFWIDTH_HBOX_PLAYER = 150;

  private static final String CSS_PROFILEIMAGE =
      "-fx-border-color: #000000; -fx-border-radius: 8; -fx-background-radius: 8;";

  /**
   * Public constructor of this class initialises variables and builds pane.
   *
   * @param messages the ResourceBundle for the externalised strings
   */
  public SpectatorList(ResourceBundle messages, int lobbyId) {

    this.messages = messages;

    initSpectatorList(lobbyId);
  }

  private void initSpectatorList(int lobbyId) {
    getStylesheets().add("cssFiles/mainGame.css");
    setId("spectator-pane");

    showLobbyId(lobbyId);
    buildSpectatorList(new Spectators(lobbyId));
  }

  private void showLobbyId(int lobbyId) {
    Label label =
        new Label(messages.getString("ViewImpl_LabelLobbyID") + " " + String.valueOf(lobbyId));
    label.getStyleClass().add("spectatorlist-label");
    setTop(label);
    BorderPane.setAlignment(label, Pos.CENTER);
  }

  private void buildSpectatorList(Spectators spectators) {

    VBox spectatorList = new VBox(SPACING_VBOX_SPECTATORLIST);
    spectatorList.setAlignment(Pos.CENTER);

    if (spectators.isEmpty()) {
      Label label = new Label(messages.getString("SpectatorList_NoSpectators"));
      label.getStyleClass().add("spectatorList-label");
      spectatorList.getChildren().add(label);
    }

    for (Player player : spectators) {
      HBox hbox = new HBox(SPACING_HBOX_PLAYER);
      hbox.setAlignment(Pos.CENTER_LEFT);
      hbox.setPadding(PADDING_HBOX_PLAYER);
      hbox.setPrefWidth(PREFWIDTH_HBOX_PLAYER);
      hbox.setStyle(
          "-fx-border-color: "
              + toHexString(player.getColor())
              + "; -fx-border-width: 2; -fx-background-color: rgb(235,235,235);");

      Image profile =
          new Image(getClass().getClassLoader().getResourceAsStream("images/profile.png"));
      ImageView imageView = new ImageView(profile);
      imageView.setPreserveRatio(true);
      imageView.setFitHeight(40);
      imageView.setStyle(CSS_PROFILEIMAGE);

      hbox.getChildren().add(imageView);

      Label name = new Label(player.getName());
      name.getStyleClass().add("spectatorList-label");
      hbox.getChildren().add(name);
      spectatorList.getChildren().add(hbox);
    }

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(spectatorList);
    scrollPane.setFitToWidth(false);
    scrollPane.setFitToHeight(true);
    scrollPane.setPrefWidth(PREFWIDTH_HBOX_PLAYER);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    setCenter(scrollPane);
    setMinWidth(PREFWIDTH_HBOX_PLAYER);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == View.SPECTATORS_CHANGED) {
      if (evt.getNewValue() instanceof Spectators) {
        buildSpectatorList((Spectators) evt.getNewValue());
      }
    } else if (evt.getPropertyName() == ViewImpl.LOBBYID_CHANGED) {
      if (evt.getNewValue() instanceof Integer) {
        showLobbyId((int) evt.getNewValue());
      }
    }
  }

  private String format(double val) {
    String in = Integer.toHexString((int) Math.round(val * 255));
    return in.length() == 1 ? "0" + in : in;
  }

  private String toHexString(Color value) {
    return "#"
        + (format(value.getRed())
                + format(value.getGreen())
                + format(value.getBlue())
                + format(value.getOpacity()))
            .toUpperCase();
  }
}
