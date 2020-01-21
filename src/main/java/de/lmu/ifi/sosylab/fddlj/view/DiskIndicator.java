package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * This class offers a pane that can be used to indicate the current player's disk color and in case
 * of a multi player game, which disk color this game's player is playing.
 *
 * @author Josef Feger
 */
public class DiskIndicator extends BorderPane implements PropertyChangeListener {

  private static final int DISK_RADIUS = 65;
  private static final float PADDING = 10;

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
    titel.setStyle("-fx-text-fill: #ffffff; -fx-font-size: x-large; -fx-font-family: arial;");
    setTop(titel);
    BorderPane.setAlignment(titel, Pos.CENTER);
    BorderPane.setMargin(titel, new Insets(0, 0, 10, 0));
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

    widthProperty().addListener(e -> resizeDisk());
    heightProperty().addListener(e -> resizeDisk());
  }

  private void resizeDisk() {
    double radius =
        ((getHeight() - titel.getHeight() - name.getHeight()) - GraphicDisk.PADDING) / 2;
    if (radius > DISK_RADIUS) {
      radius = DISK_RADIUS;
    }

    circle.setCenterY(getHeight() / 2);
    circle.setLightY(circle.getCenterY() - DISK_RADIUS / 2 + PADDING);
    circle.setLightZ(1.15 * getHeight());
    circle.resizeDisk(getHeight(), getHeight(), radius);
  }

  private void initPlayerName() {
    name = new Label(model.getState().getPlayerManagement().getCurrentPlayer().getName());
    name.setStyle("-fx-text-fill: #ffffff; -fx-font-size: x-large; -fx-font-family: arial;");
    setBottom(name);
    BorderPane.setAlignment(name, Pos.CENTER);
    BorderPane.setMargin(name, new Insets(10, 0, 0, 0));
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    handlePropertyChangeEvent(evt);
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
