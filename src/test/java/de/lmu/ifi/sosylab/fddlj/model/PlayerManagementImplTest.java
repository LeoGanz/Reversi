package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Optional;

import javafx.scene.paint.Color;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests for {@link PlayerManagementImpl}.
 *
 * @author Leonard Ganz
 */
public class PlayerManagementImplTest {

  private Player playerOne;
  private Player playerTwo;
  private ModifiablePlayerManagement pm;

  /** Set up environment. */
  @BeforeEach
  public void setUp() {
    playerOne = new PlayerImpl("P1", Color.BLUE);
    playerTwo = new PlayerImpl("P2", Color.RED);
    pm = new PlayerManagementImpl(playerOne, playerTwo);
  }

  @SuppressWarnings("unused")
  @Test
  public void testPlayerManagementConstructor_Null() {
    try {
      new PlayerManagementImpl(null, null);
      Assertions.fail("NullPointer should have occured when trying to create PM with null players");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testGetPlayerOne() {
    Assertions.assertEquals(
        playerOne,
        pm.getPlayerOne(),
        "Retrieved player should equal player used during construction.");
  }

  @Test
  public void testSetPlayerOne_Null() {
    try {
      pm.setPlayerOne(null);
      Assertions.fail("NullPointer should have occured when trying to set null player");
    } catch (@SuppressWarnings("unused") NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testSetPlayerOne_Regular() {
    pm.setPlayerOne(playerTwo);
    Assertions.assertEquals(
        playerTwo,
        pm.getPlayerOne(),
        "Player that has been just set, should be equal to the player set.");
  }

  @Test
  public void testGetPlayerTwo() {
    Assertions.assertEquals(
        playerTwo,
        pm.getPlayerTwo(),
        "Retrieved player should equal player used during construction.");
  }

  @Test
  public void testSetPlayerTwo_Null() {
    try {
      pm.setPlayerTwo(null);
      Assertions.fail("NullPointer should have occured when trying to set null player");
    } catch (@SuppressWarnings("unused") NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testSetPlayerTwo_Regular() {
    pm.setPlayerTwo(playerOne);
    Assertions.assertEquals(
        playerOne,
        pm.getPlayerTwo(),
        "Player that has been just set, should be equal to the player set.");
  }

  @Test
  public void testGetCurrentPlayer_DefaultAfterCreation() {
    Assertions.assertEquals(
        playerOne, pm.getCurrentPlayer(), "Current player should be player one by default");
  }

  @Test
  public void testSwitchCurrentPlayer_SingleSwitch() {
    Player before = pm.getCurrentPlayer();
    pm.switchCurrentPlayer();
    Player after = pm.getCurrentPlayer();
    Assertions.assertNotEquals(
        before, after, "After switch, current player should not be the same player anymore");
  }

  @Test
  public void testSwitchCurrentPlayer_DoubleSwitch() {
    Player before = pm.getCurrentPlayer();
    pm.switchCurrentPlayer();
    pm.switchCurrentPlayer();
    Player after = pm.getCurrentPlayer();
    Assertions.assertEquals(
        before, after, "After two switch, current player should be the same player again");
  }

  @Test
  public void testGetAndSetWinner_PlayerOne() {
    pm.setWinner(Optional.of(playerOne));
    Assertions.assertTrue(
        pm.getWinner().isPresent(), "Winner should exist if winner has just been set.");
    Assertions.assertEquals(
        playerOne,
        pm.getWinner().get(),
        "The player just set as winner should be equal to player set.");
  }

  @Test
  public void testGetAndSetWinner_PlayerTwo() {
    pm.setWinner(Optional.of(playerTwo));
    Assertions.assertTrue(
        pm.getWinner().isPresent(), "Winner should exist if winner has just been set.");
    Assertions.assertEquals(
        playerTwo,
        pm.getWinner().get(),
        "The player just set as winner should be equal to player set.");
  }

  @Test
  public void testGetAndSetWinner_Draw() {
    pm.setWinner(Optional.empty());
    Assertions.assertTrue(
        pm.getWinner().isEmpty(), "Winner should not exist if draw has just been set.");
  }

  @Test
  public void testGetAndSetWinner_Null() {
    pm.setWinner(null);
    Assertions.assertTrue(
        pm.getWinner().isEmpty(),
        "Winner should not exist if null has just been set as winner, as draw is default.");
  }

  @Test
  public void testEquals_Equal() {
    PlayerManagement pm1 = new PlayerManagementImpl(playerOne, playerTwo);
    PlayerManagement pm2 = new PlayerManagementImpl(playerOne, playerTwo);
    Assertions.assertTrue(
        pm1.equals(pm2), "Two PlayerManagements should be equal if created with same players.");
  }

  @Test
  public void testEquals_SwitchedPlayers() {
    PlayerManagement pm1 = new PlayerManagementImpl(playerOne, playerTwo);
    PlayerManagement pm2 = new PlayerManagementImpl(playerTwo, playerOne);
    Assertions.assertFalse(
        pm1.equals(pm2),
        "Two PlayerManagements should not be equal if created with player 1 and 2 switched.");
  }

  @Test
  public void testHashCode_Equal() {
    PlayerManagement pm1 = new PlayerManagementImpl(playerOne, playerTwo);
    PlayerManagement pm2 = new PlayerManagementImpl(playerOne, playerTwo);
    Assertions.assertEquals(
        pm1.hashCode(), pm2.hashCode(), "HashCode should be equal for equal PlayerManagements.");
  }

  @Test
  public void testHashCode_SwitchedPlayers() {
    PlayerManagement pm1 = new PlayerManagementImpl(playerOne, playerTwo);
    PlayerManagement pm2 = new PlayerManagementImpl(playerTwo, playerOne);
    Assertions.assertNotEquals(
        pm1.hashCode(),
        pm2.hashCode(),
        "HashCode should not be equal for PlayerManagements with switched Players.");
  }

  @Test
  public void testMakeCopy() {
    PlayerManagement copy = pm.makeCopy();
    Assertions.assertEquals(pm, copy, "A copied PlayerManagement should be equal to the original.");
  }
}
