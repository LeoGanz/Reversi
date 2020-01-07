package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;

/**
 * A concrete Implementation of ModifiableGameState and thus by extension an implementation of
 * {@link GameState}. Provides the equals and hashCode methods.
 *
 * @author Daniel Leidreiter
 */
public class GameStateImpl implements ModifiableGameState {

  private Phase phase;
  private ModifiableGameField field;
  private ModifiablePlayerManagement manager;

  /** Creates an empty GameState with all values nulled. */
  public GameStateImpl() {
    phase = null;
    field = null;
    manager = null;
  }

  @Override
  public void setCurrentPhase(Phase phase) {
    this.phase = phase;
  }

  @Override
  public void setGameField(ModifiableGameField gameField) {
    field = gameField;
  }

  @Override
  public void setPlayerManagement(ModifiablePlayerManagement playerManagement) {
    manager = playerManagement;
  }

  @Override
  public Phase getCurrentPhase() {
    return phase;
  }

  @Override
  public ModifiableGameField getField() {
    return field;
  }

  @Override
  public ModifiablePlayerManagement getPlayerManagement() {
    return manager;
  }

  @Override
  public ModifiableGameState makeCopy() {
    GameStateImpl copy = new GameStateImpl();
    copy.setCurrentPhase(getCurrentPhase());
    if (getField() != null) {
      copy.setGameField(getField().makeCopy());
    }
    if (getPlayerManagement() != null) {
      copy.setPlayerManagement(getPlayerManagement().makeCopy());
    }
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
    return Objects.equals(phase, other.phase)
        && Objects.equals(field, other.field)
        && Objects.equals(manager, other.manager);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phase, field, manager);
  }
}
