package de.lmu.ifi.sosylab.fddlj.model;

/**
 * A concrete Implementation of {@link ModifiableGameState} and thus by extension an Implementation
 * of {@link GameState}.
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
}
