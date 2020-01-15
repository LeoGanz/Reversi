package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;

/**
 * This class represents an AI player and is only used inside the program to be able to
 * differentiate between a real player and an AI player. <br>
 * That is the reason why this class extends the {@link PlayerImpl} class - because it is just a
 * more specific kind of player but has to offer the same functionality.
 *
 * @author Josef Feger
 */
public class AiPlayerImpl extends PlayerImpl implements AiPlayer {

  /**
   * Public constructor of this class takes the arguments and uses them to call the {@code super}
   * constructor.
   *
   * @param playerName the AI player's name
   * @param color the AI player's disk color
   */
  public AiPlayerImpl(String playerName, Color color) {
    super(playerName, color);
  }

  /**
   * Constructor of this class that initialises the AI player with the following default values.
   * <br>
   *
   * <ul>
   *   <li>{@code name} : AI
   *   <li>{@code color} : Black
   * </ul>
   */
  public AiPlayerImpl() {
    this("AI", Color.BLACK);
  }
}
