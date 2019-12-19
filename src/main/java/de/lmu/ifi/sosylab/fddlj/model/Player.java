package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

/**
 * A player represents an actual human player who plays the reversi game.
 * 
 * @author Josef Feger, Leonard Ganz
 *
 */
public interface Player {

  /**
   * Get this player's name.
   * 
   * @return the player's name
   */
  String getName();

  /**
   * Return the color the player wishes to use for all customizable parts in the GUI.
   * 
   * @return the player's favored color
   */
  Color getColor();

  /**
   * Returns a deep copy of the {@link Player} this method is called for.
   *
   * @return a deep copy of the player
   */
  Player makeCopy();
}
