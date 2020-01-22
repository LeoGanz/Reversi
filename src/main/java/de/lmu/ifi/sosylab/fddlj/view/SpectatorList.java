package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
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

public class SpectatorList extends BorderPane implements PropertyChangeListener {

  public SpectatorList() {

    initSpectatorList();
  }

  private void initSpectatorList() {
    getStylesheets().add("cssFiles/mainGame.css");
    setId("spectator-pane");
  }

  private void buildSpectatorList(HashSet<Player> spectators) {

    VBox spectatorList = new VBox(15);
    spectatorList.setAlignment(Pos.CENTER);

    if (spectators.isEmpty()) {
      Label label = new Label("No spectators");
      label.setStyle("-fx-font-size: x-large; -fx-text-fill: #000000;");
      spectatorList.getChildren().add(label);
    }

    for (Player player : spectators) {
      HBox hbox = new HBox(20);
      hbox.setAlignment(Pos.CENTER_LEFT);
      hbox.setPadding(new Insets(15));
      hbox.setStyle(
          "-fx-border-color: "
              + toHexString(player.getColor())
              + "; -fx-border-width: 2; -fx-background-color: rgba(160,160,160,0.8);");

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
      spectatorList.getChildren().add(hbox);
    }

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(spectatorList);
    scrollPane.setFitToWidth(false);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
    setCenter(scrollPane);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {}

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
