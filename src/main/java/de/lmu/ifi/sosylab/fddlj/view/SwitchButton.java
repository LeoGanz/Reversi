package de.lmu.ifi.sosylab.fddlj.view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class implements a SwitchButton which has the two states off and on.
 *
 * @author Josef Feger
 */
public class SwitchButton extends StackPane {

  static final String HINTS_STATE_CHANGED = "state_changed";

  private final PropertyChangeSupport support;

  private Rectangle back;
  private final Button button = new Button();
  private String buttonStyleOff =
      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2);"
          + " -fx-background-color: WHITE;";
  private String buttonStyleOn =
      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2);"
          + " -fx-background-color: #00893d;";
  private boolean state;

  private void init(double width, double height) {

    back = new Rectangle(width, height, Color.RED);
    getChildren().addAll(back, button);
    setMinSize(width, height);
    back.maxWidth(width);
    back.minWidth(width);
    back.prefWidth(width);
    back.maxHeight(height);
    back.minHeight(height);
    back.prefHeight(height);
    back.setArcHeight(back.getHeight());
    back.setArcWidth(back.getHeight());
    Double r = 2 * height;
    button.setShape(new Circle(r));
    setAlignment(button, Pos.CENTER_LEFT);
    button.setMaxSize(2 * height, 2 * height);
    button.setMinSize(2 * height, 2 * height);

    button.setStyle(buttonStyleOn);
    back.setFill(Color.valueOf("#80C49E"));
    setAlignment(button, Pos.CENTER_RIGHT);
    state = true;
  }

  /**
   * Constructor of the class initialises the button and adds the necessary functionality.
   *
   * @param width the button's preferred width
   * @param height the button's preferred height
   */
  public SwitchButton(double width, double height) {

    setPrefSize(width, height);
    init(width, height);

    support = new PropertyChangeSupport(this);
    EventHandler<Event> click =
        new EventHandler<Event>() {
          @Override
          public void handle(Event e) {
            if (state) {
              button.setStyle(buttonStyleOff);
              back.setFill(Color.valueOf("#ced5da"));
              setAlignment(button, Pos.CENTER_LEFT);
              state = false;
            } else {
              button.setStyle(buttonStyleOn);
              back.setFill(Color.valueOf("#80C49E"));
              setAlignment(button, Pos.CENTER_RIGHT);
              state = true;
            }

            support.firePropertyChange(HINTS_STATE_CHANGED, null, state);
          }
        };

    button.setFocusTraversable(false);
    setOnMouseClicked(click);
    button.setOnMouseClicked(click);
  }

  /**
   * Adds a listener that fires when the button's state changes.
   *
   * @param listener the listener to add
   */
  void addListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes a listener.
   *
   * @param listener the listener to remove
   */
  void removeListener(PropertyChangeListener listener) {
    support.removePropertyChangeListener(listener);
  }
}
