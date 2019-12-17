package de.lmu.ifi.sosylab.fddlj.model;

public class GameStateImpl implements GameState, ModifiableGameState {

  private Phase phase;
  private GameField field;
  private PlayerManagement manager;

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
    ModifiablePlayerManagement managerCopy = null; // to be done: copy PlayerManagement !!
    copy.setPlayerManagement(managerCopy);
    return copy;
  }
}
