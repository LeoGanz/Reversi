package de.lmu.ifi.sosylab.fddlj.view;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import java.beans.PropertyChangeListener;

/**
 * The main interface of the view. It gets the state it displays directly from the {@link Model}.
 */
public interface View extends PropertyChangeListener {

  /** Show the current game. */
  void showGame(GameMode gameMode);
}
