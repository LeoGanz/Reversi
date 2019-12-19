package de.lmu.ifi.sosylab.fddlj.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

/**
 * Unit Test for {@link GameStateImpl}
 *
 * @author Daniel Leidreiter
 */
public class GameStateImplTest {

  GameStateImpl state;

  @BeforeAll
  void setUpState() {
    state = new GameStateImpl();
  }

  @Test
  void testSetAndGetPhase_RUNNING() {
    state.setCurrentPhase(Phase.RUNNING);
    Assertions.assertEquals(
        Phase.RUNNING, state.getCurrentPhase(), "Phase unsuccessfully set to RUNNING!");
  }

  @Test
  void testSetAndGetPhase_FINISHED() {
    state.setCurrentPhase(Phase.FINISHED);
    Assertions.assertEquals(
        Phase.FINISHED, state.getCurrentPhase(), "Phase unsuccessfully set to FINISHED!");
  }

  @Test
  void testSetAndGetPhase_WAITING() {
    state.setCurrentPhase(Phase.WAITING);
    Assertions.assertEquals(
        Phase.WAITING, state.getCurrentPhase(), "Phase unsuccessfully set to WAITING!");
  }

  @Test
  void testSetAndGetPhase_null() {
    state.setCurrentPhase(null);
    Assertions.assertEquals(null, state.getCurrentPhase(), "Phase unsuccessfully set to null!");
  }

  @Test
  void testSetAndGetGameField_notNull() {
    GameField field;
    // set up field
    state.setGameField(field);
    Assertions.assertSame(
        field,
        state.getField(),
        "getField should return the same GameField that was set by setGameField!");
  }

  @Test
  void testSetAndGetGameField_null() {
    state.setGameField(null);
    Assertions.assertSame(null, state.getField(), "GameField unsuccessfully set to null!");
  }

  @Test
  void testSetAndGetPlayerManagement_notNull() {
    PlayerManagement manager;
    // set up manager
    state.setPlayerManagement(manager);
    Assertions.assertSame(
        manager,
        state.getPlayerManagement(),
        "getPlayerManagement should return the same PlayerManagement that was set by setPlayerManagement!");
  }

  @Test
  void testSetAndGetPlayerManagement_null() {
    state.setPlayerManagement(null);
    Assertions.assertSame(
        null, state.getPlayerManagement(), "PlayerManagement unsuccessfully set to null!");
  }

  @Test
  void testMakeCopy_notNull() {
    Phase phase = Phase.RUNNING;
    GameField field;
    // set up field
    PlayerManagement manager;
    // set up manager
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
    GameField field;
    // set up field
    PlayerManagement manager;
    // set up manager
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
    Phase phase = Phase.WAITING;
    PlayerManagement manager;
    // set up manager
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
    Phase phase = Phase.FINISHED;
    GameField field;
    // set up field
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
  void testEquals_allEqual() {}

  @Test
  void testEquals_PaseNotEqual() {}

  @Test
  void testEquals_GameFieldNotEqual() {}

  @Test
  void testEquals_PlayerManagementNotEqual() {}
}
