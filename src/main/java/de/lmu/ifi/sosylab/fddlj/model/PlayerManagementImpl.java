package de.lmu.ifi.sosylab.fddlj.model;

import java.util.Objects;
import java.util.Optional;

/**
 * A concrete implementation of {@link PlayerManagement}. Provides equals and hashCode methods.
 * Equals depends on {@link Player#equals(Object)}.
 *
 * @author Leonard Ganz
 */
public class PlayerManagementImpl implements ModifiablePlayerManagement {

  private Player playerOne;
  private Player playerTwo;
  private PlayerEnum current;
  private PlayerEnum winner;

  /**
   * Create a new {@link PlayerManagement} with the provided players. Initially it will be player
   * one's turn and nobody has won yet.
   *
   * @param playerOne the first player
   * @param playerTwo the second player
   * @throws NullPointerException if playerOne or playerTwo is null
   */
  public PlayerManagementImpl(Player playerOne, Player playerTwo) {
    this.playerOne = Objects.requireNonNull(playerOne);
    this.playerTwo = Objects.requireNonNull(playerTwo);
    current = PlayerEnum.PLAYER_ONE;
    winner = PlayerEnum.NONE;
  }

  private PlayerManagementImpl(PlayerManagementImpl original) {
    playerOne = original.playerOne.makeCopy();
    playerTwo = original.playerTwo.makeCopy();
    current = original.current;
    winner = original.winner;
  }

  @Override
  public Player getPlayerOne() {
    return playerOne;
  }

  @Override
  public Player getPlayerTwo() {
    return playerTwo;
  }

  @Override
  public Player getCurrentPlayer() {
    if (current == PlayerEnum.PLAYER_ONE) {
      return playerOne;
    }
    if (current == PlayerEnum.PLAYER_TWO) {
      return playerTwo;
    }
    return null;
  }

  @Override
  public Player getOpponentPlayer(Player player) {
    if (player.equals(playerOne)) {
      return playerTwo;
    }
    if (player.equals(playerTwo)) {
      return playerOne;
    }
    return null;
  }

  @Override
  public Optional<Player> getWinner() {
    if (winner == PlayerEnum.PLAYER_ONE) {
      return Optional.of(playerOne);
    } else if (winner == PlayerEnum.PLAYER_TWO) {
      return Optional.of(playerTwo);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void setPlayerOne(Player playerOne) {
    this.playerOne = Objects.requireNonNull(playerOne);
  }

  @Override
  public void setPlayerTwo(Player playerTwo) {
    this.playerTwo = Objects.requireNonNull(playerTwo);
  }

  @Override
  public void switchCurrentPlayer() {
    if (current == PlayerEnum.PLAYER_ONE) {
      current = PlayerEnum.PLAYER_TWO;
    } else if (current == PlayerEnum.PLAYER_TWO) {
      current = PlayerEnum.PLAYER_ONE;
    }
  }

  @Override
  public void setWinner(Optional<Player> winner) {
    if ((winner == null) || winner.isEmpty()) {
      this.winner = PlayerEnum.NONE;
    } else if (winner.get().equals(playerOne)) {
      this.winner = PlayerEnum.PLAYER_ONE;
    } else if (winner.get().equals(playerTwo)) {
      this.winner = PlayerEnum.PLAYER_TWO;
    } else {
      throw new IllegalArgumentException("Winner must be equal to player one or player two!");
    }
  }

  @Override
  public ModifiablePlayerManagement makeCopy() {
    return new PlayerManagementImpl(this);
  }



  @Override
  public int hashCode() {
    return Objects.hash(playerOne, playerTwo, current, winner);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PlayerManagementImpl)) {
      return false;
    }

    PlayerManagementImpl other = (PlayerManagementImpl) obj;
    return Objects.equals(playerOne, other.playerOne)
        && Objects.equals(playerTwo, other.playerTwo)
        && (current == other.current)
        && (winner == other.winner);

  }

  private enum PlayerEnum {
    PLAYER_ONE,
    PLAYER_TWO,
    NONE;
  }
}