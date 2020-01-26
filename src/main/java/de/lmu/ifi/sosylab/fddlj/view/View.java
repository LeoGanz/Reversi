package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeListener;
import javafx.scene.control.Alert.AlertType;

/**
 * The main interface of the view. It gets the state it displays directly from the {@link Model}.
 */
public interface View extends PropertyChangeListener {

  static final String STAGE_RESIZED = "Stage resized";
  static final String SOUND_MODE_CHANGED = "Sound mode changed";
  static final String VOLUME_CHANGED = "Volume changed";
  static final String SPECTATORS_CHANGED = "Spectators changed";

  /** Show the current game. */
  void showGame(GameMode gameMode);

  /**
   * Adds a listener.
   *
   * @param listener the listener to add
   */
  void addListener(PropertyChangeListener listener);

  /**
   * Removes the listener.
   *
   * @param listener the listener to remove
   */
  void removeListener(PropertyChangeListener listener);

  /** Closes the view. */
  void close();

  /**
   * Displays a dialog to the user.
   *
   * @param alertType the dialog's alert type
   * @param title the dialog frame's title
   * @param subtitle the dialog's content heading
   * @param content the dialog's content
   */
  void displayAlert(AlertType alertType, String title, String subtitle, String content);
}
