package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class offers a pane that can be used to indicate the current player's disk color and in case
 * of a multi player game, which disk color this game's player is playing.
 *
 * @author Josef Feger
 */
public class DiskIndicator extends VBox implements PropertyChangeListener {

  private static final int DISK_RADIUS = 40;
  private static final int MAX_RADIUS = 80;

  private Label titel;
  private Label name;

  private Model model;
  private Controller controller;

  private GraphicDisk circle;

  /**
   * Constructor of this class initialises variables and calls first layout.
   *
   * @param model a reference to a model instance
   * @param labelText the text to be displayed
   * @param view a reference to a view instance
   */
  public DiskIndicator(Model model, String labelText, View view, Controller controller) {

    super(5);
    setAlignment(Pos.CENTER);

    this.model = model;
    this.controller = controller;
    view.addListener(this);

    setMinHeight(30 + getSpacing() + DISK_RADIUS + getSpacing() + 30);

    initLabel(labelText);
    initCanvas();
    initPlayerName();
  }

  private void initLabel(String labelText) {
    titel = new Label(labelText);
    titel.setId("disk-indicator-label");
    getChildren().add(titel);
  }

  private void initCanvas() {

    Color color = model.getState().getPlayerManagement().getCurrentPlayer().getColor();

    if (controller instanceof MultiplayerController) {
      if (!model
          .getState()
          .getPlayerManagement()
          .getCurrentPlayer()
          .equals(((MultiplayerController) controller).getOwnPlayer())) {
        if (similarTo(color, ((MultiplayerController) controller).getOwnPlayer().getColor())) {
          if (similarTo(color, Color.WHITE)) {
            color = Color.SILVER;
          } else if (similarTo(color, Color.BLACK)) {
            color = Color.GRAY;
          } else {
            color = color.deriveColor(15, 15, 10, 1);
          }
        }
      }
    }

    circle = new GraphicDisk(getWidth(), getHeight(), MAX_RADIUS, color);

    getChildren().add(circle);
  }

  /** Resizes the disk to fit the current parent's size. */
  void resizeDisk() {

    double radius =
        ((getHeight() - titel.getHeight() - name.getHeight()) - GraphicDisk.PADDING) / 2;
    if (radius < DISK_RADIUS) {
      radius = DISK_RADIUS;
    } else if (radius > MAX_RADIUS) {
      radius = MAX_RADIUS;
    }

    circle.resizeDisk(getHeight(), getHeight(), radius);
  }

  private void initPlayerName() {
    name = new Label(model.getState().getPlayerManagement().getCurrentPlayer().getName());
    name.setId("disk-indicator-label");
    getChildren().add(name);
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

  private boolean similarTo(Color c, Color v) {
    double distance =
        Math.sqrt(
            (c.getRed() - v.getRed()) * (c.getRed() - v.getRed())
                + (c.getGreen() - v.getGreen()) * (c.getGreen() - v.getGreen())
                + (c.getBlue() - v.getBlue()) * (c.getBlue() - v.getBlue()));

    if (distance < 0.25) {
      return true;
    } else {
      return false;
    }
  }
}
