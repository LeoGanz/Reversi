package de.lmu.ifi.sosylab.fddlj.view;

/**
 * This interfaces represents a specialisation of the {@link View} interface used for a view for
 * online games.
 *
 * @author Josef Feger
 */
public interface OnlineView extends View {

  /** Shows a waiting screen while waiting for opponent to connect. */
  void showWaitingScreen();
}
