package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.RejectedPlacement.Reason;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * A GameLobby manages a game between two players on the server side. It is responsible for
 * validating moves received from the clients.
 *
 * @author Leonard Ganz
 */
public class GameLobby {

  private int lobbyID;
  private ServerImpl server;
  private ClientConnection connOne;
  private ClientConnection connTwo;
  private Set<ClientConnection> spectators;
  private Model masterGame;
  private Player playerOne;
  private Player playerTwo;
  private boolean freshStart;
  private UUID lastPlacementID;
  private boolean restartRequestOne;
  private boolean restartRequestTwo;

  /**
   * Create a new lobby for two connected players.
   *
   * @param lobbyID integer used to identify the game lobby
   */
  public GameLobby(int lobbyID, ServerImpl server) {
    this.lobbyID = lobbyID;
    this.server = server;
    spectators = new HashSet<>();
    freshStart = true;
  }

  /**
   * Get the id of the game lobby.
   *
   * @return an integer id
   */
  public int getLobbyID() {
    return lobbyID;
  }

  /**
   * Try to perform a disk placement.
   *
   * @param requestedDiskPlacement disk placement that shall be performed
   */
  public synchronized RejectedPlacement tryDiskPlacement(DiskPlacement requestedDiskPlacement) {
    UUID uuid = requestedDiskPlacement.getUuid();
    if (!requestedDiskPlacement.getPrevious().equals(lastPlacementID)) {
      return new RejectedPlacement(uuid, Reason.INVALID_PREVIOUS_UUID);
    }
    if (requestedDiskPlacement.getDisk().getPlayer()
        != masterGame.getState().getPlayerManagement().getCurrentPlayer()) {
      return new RejectedPlacement(uuid, Reason.NOT_YOUR_TURN);
    }

    boolean valid =
        masterGame.placeDisk(
            requestedDiskPlacement.getDisk(), requestedDiskPlacement.getLocation());
    if (valid) {
      lastPlacementID = uuid;
      connOne.sendMessageWith(requestedDiskPlacement);
      connTwo.sendMessageWith(requestedDiskPlacement);
      resetRestartRequests();
      return null;
    } else {
      return new RejectedPlacement(uuid, Reason.INVALID_PLACEMENT);
    }
  }

  /**
   * Try to join into the lobby as a participating player.
   *
   * @param conn the connection trying to join
   * @return whether joining was successful
   */
  public synchronized boolean joinAsPlayer(ClientConnection conn, Player player) {
    if (!needsPlayers()) {
      return false;
    }
    if (connOne == null) {
      connOne = conn;
      playerOne = player;
    } else {
      connTwo = conn;
      playerTwo = player;
    }
    conn.setLobby(this);

    if ((connOne != null) && (connTwo != null)) {
      if (freshStart) {
        handleStart();
      } else {
        handleResume();
      }
    }
    return true;
  }

  private void handleStart() {
    lastPlacementID = null;
    masterGame = new ModelImpl(GameMode.HOTSEAT, playerOne, playerTwo);
    freshStart = false;
    connOne.sendMessageWith(playerTwo);
    connTwo.sendMessageWith(playerOne);
    connOne.sendMessageWith(ServerNotification.START);
  }

  private void handleResume() {
    masterGame.substitutePlayerOneWith(playerOne);
    masterGame.substitutePlayerTwoWith(playerTwo);
    masterGame.unsetWaiting();
    connOne.sendMessageWith(playerTwo);
    connTwo.sendMessageWith(playerOne);
    connOne.sendMessageWith(masterGame.getState());
    connTwo.sendMessageWith(masterGame.getState());
  }

  /**
   * Try to join into the lobby as a spectating player.
   *
   * @param conn the connection trying to join
   */
  public synchronized void joinAsSpectator(ClientConnection conn) {
    spectators.add(conn);
    // TODO send updated spectator set to clients
  }

  /**
   * Called to signal that a client has left the lobby. If both participating players have left, the
   * lobby will be closed. Else another player can join. If leaving player was spectating the game
   * will also continue.
   *
   * @param connectionID integer id used to reference the leaving client connection
   */
  public synchronized void leave(int connectionID) {
    ClientConnection partner;
    if (connOne.getConnectionID() == connectionID) {
      connOne = null;
      partner = connTwo;
      masterGame.setWaiting();
    } else if (connTwo.getConnectionID() == connectionID) {
      connTwo = null;
      partner = connOne;
      masterGame.setWaiting();
    } else {
      partner = null;
      spectators.remove(server.getConnection(connectionID));
      // TODO send updated spectator set to clients
    }

    if (partner != null) {
      partner.sendMessageWith(ServerNotification.PARTNER_LEFT);
      resetRestartRequests();
    }

    if ((connOne == null) && (connTwo == null)) {
      closeLobby();
    }
  }

  /**
   * Is the game of this lobby running.
   *
   * @return whether the game is in phase running
   */
  public boolean isGameRunning() {
    return masterGame.getState().getCurrentPhase() == Phase.RUNNING;
  }

  /**
   * Whether the lobby needs more players for the game to run.
   *
   * @return whether players are needed
   */
  public synchronized boolean needsPlayers() {
    return (connOne == null) || (connTwo == null);
  }

  private void closeLobby() {
    Stream.concat(Stream.of(connOne, connTwo), spectators.stream())
        .filter(Objects::nonNull)
        .forEach(conn -> conn.sendMessageWith(ServerNotification.LOBBY_CLOSED));
    server.lobbyClosed(lobbyID);
  }

  /**
   * Request a restart of the game. The partner has to accept the request before the restart will
   * take place. If a placement is made before the partner accepts, the request is canceled as the
   * game situation has changed.
   *
   * @param connectionID integer to reference the connection making the request
   */
  public synchronized void requestRestart(int connectionID) {
    if (connOne.getConnectionID() == connectionID) {
      restartRequestOne = true;
      connTwo.sendMessageWith(ServerNotification.PARTNER_REQUESTED_RESTART);
    } else if (connTwo.getConnectionID() == connectionID) {
      restartRequestTwo = true;
      connOne.sendMessageWith(ServerNotification.PARTNER_REQUESTED_RESTART);
    }
  }

  /**
   * Accept the partner's request of restarting the game. Has no effect if no request has been made
   * by the partner.
   *
   * @param connectionID integer to reference the connection accepting the request
   */
  public synchronized void acceptRestart(int connectionID) {
    if ((connOne.getConnectionID() == connectionID) && restartRequestTwo) {
      connTwo.sendMessageWith(ServerNotification.PARTNER_ACCEPTED_RESTART);
      restartGame();
    } else if ((connTwo.getConnectionID() == connectionID) && restartRequestOne) {
      connOne.sendMessageWith(ServerNotification.PARTNER_ACCEPTED_RESTART);
      restartGame();
    }
  }

  private void restartGame() {
    freshStart = true;
    resetRestartRequests();
    handleStart();
  }

  private void resetRestartRequests() {
    restartRequestOne = false;
    restartRequestTwo = false;
  }
}
