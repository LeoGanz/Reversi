package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;

/**
 * A concrete Implementation of {@link ModifiableGameState} and thus by extension an Implementation
 * of {@link GameState}. Provides the equals method. Equals depends on {@link
 * GameField#equals(Object)} and {@link PlayerManagement#equals(Object)}.
 *
 * @author Daniel Leidreiter
 */
public class GameStateImpl implements ModifiableGameState {

  private static final long serialVersionUID = 1L;
  private Phase phase;
  private GameField field;
  private PlayerManagement manager;

  /** Creates an empty GameState with all values nulled. */
  public GameStateImpl() {
    this.phase = null;
    this.field = null;
    this.manager = null;
  }

  @Override
  public void setCurrentPhase(Phase phase) {
    this.phase = phase;
  }

  @Override
  public void setGameField(GameField gameField) {
    this.field = gameField;
  }

  @Override
  public void setPlayerManagement(PlayerManagement playerManagement) {
    this.manager = playerManagement;
  }

  @Override
  public Phase getCurrentPhase() {
    return phase;
  }

  @Override
  public GameField getField() {
    return field;
  }

  @Override
  public PlayerManagement getPlayerManagement() {
    return manager;
  }

  @Override
  public GameState makeCopy() {
    GameStateImpl copy = new GameStateImpl();
    copy.setCurrentPhase(this.getCurrentPhase());
    copy.setGameField(this.getField().makeCopy());
    copy.setPlayerManagement(this.getPlayerManagement().makeCopy());
    return copy;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof GameStateImpl)) {
      return false;
    }

    GameStateImpl other = (GameStateImpl) obj;
    return Objects.equals(this.phase, other.phase)
        && Objects.equals(this.field, other.field)
        && Objects.equals(this.manager, other.manager);
  }
}
