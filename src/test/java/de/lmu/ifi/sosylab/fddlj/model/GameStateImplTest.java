package de.lmu.ifi.sosylab.fddlj.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit Test for {@link GameStateImpl}.
 *
 * @author Daniel Leidreiter
 */
public class GameStateImplTest {

  ModifiablePlayerManagement setUpManager() {
    Player playerOne = new PlayerImpl("Jon", Color.BLACK);
    Player playerTwo = new PlayerImpl("Catelyn", Color.BLUE);
    return new PlayerManagementImpl(playerOne, playerTwo);
  }

  ModifiableGameField setUpField() {
    ModifiableGameField field = new GameFieldImpl();
    PlayerManagement manager = setUpManager();
    field.set(new CellImpl(0, 3), new DiskImpl(manager.getPlayerOne()));
    field.set(new CellImpl(3, 4), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(5, 5), new DiskImpl(manager.getPlayerTwo()));
    field.set(new CellImpl(1, 4), new DiskImpl(manager.getPlayerTwo()));
    return field;
  }

  @Test
  void testSetAndGetPhase_Running() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    Assertions.assertEquals(
        Phase.RUNNING, state.getCurrentPhase(), "Phase unsuccessfully set to RUNNING!");
  }

  @Test
  void testSetAndGetPhase_Finished() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.FINISHED);
    Assertions.assertEquals(
        Phase.FINISHED, state.getCurrentPhase(), "Phase unsuccessfully set to FINISHED!");
  }

  @Test
  void testSetAndGetPhase_Waiting() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.WAITING);
    Assertions.assertEquals(
        Phase.WAITING, state.getCurrentPhase(), "Phase unsuccessfully set to WAITING!");
  }

  @Test
  void testSetAndGetPhase_null() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(null);
    Assertions.assertEquals(null, state.getCurrentPhase(), "Phase unsuccessfully set to null!");
  }

  @Test
  void testSetAndGetGameField_notNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiableGameField field = setUpField();
    state.setGameField(field);
    Assertions.assertSame(
        field,
        state.getField(),
        "getField should return the same GameField that was set by setGameField!");
  }

  @Test
  void testSetAndGetGameField_null() {
    GameStateImpl state = new GameStateImpl();
    state.setGameField(null);
    Assertions.assertSame(null, state.getField(), "GameField unsuccessfully set to null!");
  }

  @Test
  void testSetAndGetPlayerManagement_notNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiablePlayerManagement manager = setUpManager();
    state.setPlayerManagement(manager);
    Assertions.assertSame(
        manager,
        state.getPlayerManagement(),
        "getPlayerManagement should return the same"
            + " PlayerManagement that was set by setPlayerManagement!");
  }

  @Test
  void testSetAndGetPlayerManagement_null() {
    GameStateImpl state = new GameStateImpl();
    state.setPlayerManagement(null);
    Assertions.assertSame(
        null, state.getPlayerManagement(), "PlayerManagement unsuccessfully set to null!");
  }

  @Test
  void testMakeCopy_notNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiableGameField field = setUpField();
    ModifiablePlayerManagement manager = setUpManager();
    Phase phase = Phase.RUNNING;

    state.setCurrentPhase(phase);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    GameState stateCopy;
    stateCopy = state.makeCopy();

    Assertions.assertNotSame(
        field, stateCopy.getField(), "GameField was not deep copied by makeCopy!");
    Assertions.assertNotSame(
        manager,
        stateCopy.getPlayerManagement(),
        "PlayerManagement was not deep copied by makeCopy!");

    Assertions.assertEquals(
        phase, stateCopy.getCurrentPhase(), "makeCopy should have copied Phase!");
    Assertions.assertEquals(field, stateCopy.getField(), "makeCopy should have copied GameField!");
    Assertions.assertEquals(
        manager, stateCopy.getPlayerManagement(), "makeCopy should have copied PlayerManagement!");
  }

  @Test
  void testMakeCopy_PhaseNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiableGameField field = setUpField();
    ModifiablePlayerManagement manager = setUpManager();

    state.setCurrentPhase(null);
    state.setGameField(field);
    state.setPlayerManagement(manager);

    GameState stateCopy;
    stateCopy = state.makeCopy();

    Assertions.assertNotSame(
        field, stateCopy.getField(), "GameField was not deep copied by makeCopy!");
    Assertions.assertNotSame(
        manager,
        stateCopy.getPlayerManagement(),
        "PlayerManagement was not deep copied by makeCopy!");

    Assertions.assertEquals(
        null, stateCopy.getCurrentPhase(), "makeCopy should have copied Phase!");
    Assertions.assertEquals(field, stateCopy.getField(), "makeCopy should have copied GameField!");
    Assertions.assertEquals(
        manager, stateCopy.getPlayerManagement(), "makeCopy should have copied PlayerManagement!");
  }

  @Test
  void testMakeCopy_GameFieldNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiablePlayerManagement manager = setUpManager();
    Phase phase = Phase.WAITING;

    state.setCurrentPhase(phase);
    state.setGameField(null);
    state.setPlayerManagement(manager);

    GameState stateCopy;
    stateCopy = state.makeCopy();

    Assertions.assertNotSame(
        manager,
        stateCopy.getPlayerManagement(),
        "PlayerManagement was not deep copied by makeCopy!");

    Assertions.assertEquals(
        phase, stateCopy.getCurrentPhase(), "makeCopy should have copied Phase!");
    Assertions.assertEquals(null, stateCopy.getField(), "makeCopy should have copied GameField!");
    Assertions.assertEquals(
        manager, stateCopy.getPlayerManagement(), "makeCopy should have copied PlayerManagement!");
  }

  @Test
  void testMakeCopy_PlayerManagementNull() {
    GameStateImpl state = new GameStateImpl();
    ModifiableGameField field = setUpField();
    Phase phase = Phase.FINISHED;

    state.setCurrentPhase(phase);
    state.setGameField(field);
    state.setPlayerManagement(null);

    GameState stateCopy;
    stateCopy = state.makeCopy();

    Assertions.assertNotSame(
        field, stateCopy.getField(), "GameField was not deep copied by makeCopy!");

    Assertions.assertEquals(
        phase, stateCopy.getCurrentPhase(), "makeCopy should have copied Phase!");
    Assertions.assertEquals(field, stateCopy.getField(), "makeCopy should have copied GameField!");
    Assertions.assertEquals(
        null, stateCopy.getPlayerManagement(), "makeCopy should have copied PlayerManagement!");
  }

  @Test
  void testEquals_allEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.RUNNING);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(setUpField());

    Assertions.assertEquals(state, state2, "state and state2 should be equal!");
  }

  @Test
  void testEquals_PhaseNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.FINISHED);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(setUpField());

    Assertions.assertNotEquals(
        state, state2, "state and state2 should not be equal, since their Phases are not equal!");
  }

  @Test
  void testEquals_GameFieldNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.WAITING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.WAITING);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(new GameFieldImpl());

    Assertions.assertNotEquals(
        state,
        state2,
        "state and state2 shoould not be equal, since their GameFields are not equal!");
  }

  @Test
  void testEquals_PlayerManagementNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.FINISHED);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.FINISHED);
    state2.setPlayerManagement(
        new PlayerManagementImpl(
            new PlayerImpl("Eddard", Color.GREY), new PlayerImpl("Daenerys", Color.RED)));
    state2.setGameField(setUpField());

    Assertions.assertNotEquals(
        state,
        state2,
        "state and state2 should not be equal, since their PlayerManagements are not equal!");
  }

  @Test
  void testHashCode_allEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.RUNNING);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(setUpField());

    Assertions.assertEquals(
        state.hashCode(),
        state2.hashCode(),
        "hashCode() should be equal for equal GameStateImpl instances!");
  }

  @Test
  void testHashCode_PhaseNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.RUNNING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.FINISHED);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(setUpField());

    Assertions.assertNotEquals(state, state2);
    Assertions.assertNotEquals(
        state.hashCode(),
        state2.hashCode(),
        "hashCode() should not be equal for different GameStateImpl instances!");
  }

  @Test
  void testHashCode_GameFieldNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.WAITING);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.WAITING);
    state2.setPlayerManagement(setUpManager());
    state2.setGameField(new GameFieldImpl());

    Assertions.assertNotEquals(state, state2);
    Assertions.assertNotEquals(
        state.hashCode(),
        state2.hashCode(),
        "hashCode() should not be equal for different GameStateImpl instances!");
  }

  @Test
  void testHashCode_PlayerManagementNotEqual() {
    GameStateImpl state = new GameStateImpl();
    state.setCurrentPhase(Phase.FINISHED);
    state.setPlayerManagement(setUpManager());
    state.setGameField(setUpField());

    GameStateImpl state2 = new GameStateImpl();
    state2.setCurrentPhase(Phase.FINISHED);
    state2.setPlayerManagement(
        new PlayerManagementImpl(
            new PlayerImpl("Eddard", Color.GREY), new PlayerImpl("Daenerys", Color.RED)));
    state2.setGameField(setUpField());

    Assertions.assertNotEquals(state, state2);
    Assertions.assertNotEquals(
        state.hashCode(),
        state2.hashCode(),
        "hashCode() should not be equal for different GameStateImpl instances!");
  }
}
