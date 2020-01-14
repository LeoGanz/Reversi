package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class offers a pane that can be used to indicate the current player's disk color and in case
 * of a multi player game, which disk color this game's player is playing.
 *
 * @author Josef Feger
 */
public class DiskIndicator extends BorderPane implements PropertyChangeListener {

  private static final int DISK_RADIUS = 50;
  private static final int PADDING = 10;

  private Label titel;
  private Label name;

  private Model model;

  private GraphicDisk circle;

  /**
   * Constructor of this class initialises variables and calls first layout.
   *
   * @param model a reference to a model instance
   * @param labelText the text to be displayed
   * @param view a reference to a view instance
   */
  public DiskIndicator(Model model, String labelText, View view) {

    this.model = model;
    view.addListener(this);

    setMinHeight(130);

    initLabel(labelText);
    initCanvas();
    initPlayerName();
  }

  private void initLabel(String labelText) {
    titel = new Label(labelText);
    titel.setFont(Font.font("Arial", FontWeight.MEDIUM, 25));
    titel.setStyle("-fx-text-fill: #ffffff");
    setTop(titel);
    BorderPane.setAlignment(titel, Pos.CENTER);
    BorderPane.setMargin(titel, new Insets(0, 0, 20, 0));
  }

  private void initCanvas() {

    circle =
        new GraphicDisk(
            getWidth(),
            getHeight(),
            DISK_RADIUS,
            model.getState().getPlayerManagement().getCurrentPlayer().getColor());

    setCenter(circle);
    BorderPane.setAlignment(circle, Pos.CENTER);

    widthProperty()
        .addListener(
            e -> {
              circle.setCenterX(getWidth() / 2);
              circle.setLightX(circle.getCenterX() - DISK_RADIUS / 2 + 3 * PADDING);
              circle.setLightZ(0.8 * getHeight());
            });
    heightProperty()
        .addListener(
            e -> {
              circle.setCenterY(getHeight() / 2);
              circle.setLightY(circle.getCenterY() - DISK_RADIUS / 2 + 3 * PADDING);
              circle.setLightZ(0.8 * getHeight());
            });
  }

  private void initPlayerName() {
    name = new Label(model.getState().getPlayerManagement().getCurrentPlayer().getName());
    name.setFont(Font.font("Arial", FontWeight.MEDIUM, 25));
    name.setStyle("-fx-text-fill: #ffffff");
    setBottom(name);
    BorderPane.setAlignment(name, Pos.CENTER);
    BorderPane.setMargin(name, new Insets(20, 0, 0, 0));
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Platform.runLater(() -> handlePropertyChangeEvent(evt));
  }

  private void handlePropertyChangeEvent(PropertyChangeEvent event) {
    if (event.getPropertyName().equals(Model.STATE_CHANGED)) {
      circle.setFill(model.getState().getPlayerManagement().getCurrentPlayer().getColor());
      name.setText(model.getState().getPlayerManagement().getCurrentPlayer().getName());
    }

    if (event.getPropertyName().equals(Model.LISTENERS_CHANGED)) {
      if (event.getNewValue() instanceof Model) {
        this.model = (Model) event.getNewValue();
      }
    }
  }
}
