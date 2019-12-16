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
  public String getName();

  /**
   * Return the color the player wishes to use for all customizable parts in the GUI.
   * 
   * @return the player's favored color
   */
  public Color getColor();
}
