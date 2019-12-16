package de.lmu.ifi.sosylab.fddlj.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

public class GameStateReversiTest {

  GameStateReversi state;

  @BeforeAll
  void setUpState() {
    state = new GameStateReversi();
  }

  @Test
  void testSetAndGetPhase_RUNNING() {
    state.setCurrentPhase(Phase.RUNNING);
    Assertions.assertEquals(Phase.RUNNING, state.getCurrentPhase());
  }

  @Test
  void testSetAndGetPhase_FINISHED() {
    state.setCurrentPhase(Phase.FINISHED);
    Assertions.assertEquals(Phase.FINISHED, state.getCurrentPhase());
  }

  @Test
  void testSetAndGetPhase_null() {
    state.setCurrentPhase(null);
    Assertions.assertEquals(null, state.getCurrentPhase());
  }

  @Test
  void testSetAndGetPhase_WAITING() {
    state.setCurrentPhase(Phase.WAITING);
    Assertions.assertEquals(Phase.FINISHED, state.getCurrentPhase());
  }

  @Test
  void testSetAndGetGameField_notNull() {
    GameField field;
    state.setGameField(field);
    Assertions.assertEquals(field, state.getField());
  }

  @Test
  void testSetAndGetGameField_null() {
    GameField field = null;
    state.setGameField(field);
    Assertions.assertEquals(null, state.getField());
  }

  @Test
  void testSetAndGetPlayerManagement_notNull() {
    PlayerManagement manager;
    state.setPlayerManagement(manager);
    Assertions.assertEquals(manager, state.getPlayerManagement());
  }

  @Test
  void testSetAndGetPlayerManagement_null() {
    PlayerManagement manager = null;
    state.setPlayerManagement(manager);
    Assertions.assertEquals(null, state.getPlayerManagement());
  }

  @Test
  void testMakeCopy_notNull() {}

  @Test
  void testMakeCopy_PhaseNull() {}

  @Test
  void testMakeCopy_GameFieldNull() {}

  @Test
  void testMakeCopy_PlayerManagementNull() {}

  @Test
  void testMakeCopy_stateNull() {}
}
