package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Phase;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.communication.DiskPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.GameStateWithLastPlacementUuid;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement.Reason;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
  private Model masterGame;
  private ClientConnection connOne;
  private ClientConnection connTwo;
  private Player playerOne;
  private Player playerTwo;
  private Set<ClientConnection> spectatorsConnections;
  private Map<Integer, Player> spectatorsPlayers;
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
    spectatorsConnections = new HashSet<>();
    spectatorsPlayers = new HashMap<>();
    freshStart = true;
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
      broadcast(requestedDiskPlacement);
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
    publishLobbyUpdate();
    return true;
  }

  private void handleStart() {
    lastPlacementID = null;
    masterGame = new ModelImpl(GameMode.HOTSEAT, playerOne, playerTwo);
    freshStart = false;
    broadcast(getGameStateWithLastPlacementUuid());
  }

  private void handleResume() {
    lastPlacementID = null;
    masterGame.substitutePlayersWith(playerOne, playerTwo);
    masterGame.unsetWaiting();
    broadcast(getGameStateWithLastPlacementUuid());
  }

  /**
   * Try to join into the lobby as a spectating player.
   *
   * @param conn the connection trying to join
   */
  public synchronized void joinAsSpectator(ClientConnection conn, Player player) {
    spectatorsConnections.add(conn);
    spectatorsPlayers.put(conn.getConnectionID(), player);
    broadcast(getSpectators());
    conn.setLobby(this);
    broadcast(getGameStateWithLastPlacementUuid());
  }

  /**
   * Called to signal that a client has left the lobby. If both participating players have left, the
   * lobby will be closed. Else another player can join. If leaving player was spectating the game
   * will also continue.
   *
   * @param connectionID integer id used to reference the leaving client connection
   */
  public synchronized void leave(int connectionID) {
    ClientConnection leavingConn = server.getConnection(connectionID);
    if (leavingConn == null) {
      return;
    } else {
      leavingConn.setLobby(null);
    }

    if (connOne.getConnectionID() == connectionID) {
      connOne = null;
      broadcast(ServerNotification.PLAYER_ONE_LEFT);
    } else if (connTwo.getConnectionID() == connectionID) {
      connTwo = null;
      broadcast(ServerNotification.PLAYER_TWO_LEFT);
    } else {
      spectatorsConnections.remove(server.getConnection(connectionID));
      spectatorsPlayers.remove(connectionID);
      broadcast(getSpectators());
      return;
    }

    // only reached if leaving connection was conn 1 or conn 2
    if (masterGame != null) {
      masterGame.setWaiting();
    }
    publishLobbyUpdate();
    resetRestartRequests();

    if ((connOne == null) && (connTwo == null)) {
      closeLobby();
    }
  }

  /**
   * Whether the lobby needs more players for the game to run.
   *
   * @return whether players are needed
   */
  public synchronized boolean needsPlayers() {
    return (connOne == null) || (connTwo == null);
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
      restartRequestOne = true; // needed for not firing "partner denied" when resetting
      restartGame();
    } else if ((connTwo.getConnectionID() == connectionID) && restartRequestOne) {
      connOne.sendMessageWith(ServerNotification.PARTNER_ACCEPTED_RESTART);
      restartRequestTwo = true; // needed for not firing "partner denied" when resetting
      restartGame();
    }
  }

  /**
   * Deny the partner's request of restarting the game. Has no effect if no request has been made by
   * the partner.
   *
   * @param connectionID integer to reference the connection denying the request
   */
  public synchronized void denyRestart(int connectionID) {
    if (((connOne.getConnectionID() == connectionID) && restartRequestTwo)
        || ((connTwo.getConnectionID() == connectionID) && restartRequestOne)) {
      resetRestartRequests();
    }
  }

  /**
   * Get a {@link GameStateWithLastPlacementUuid}.
   *
   * @return the GameStateWithLastPlacementUuid with current game data
   */
  public synchronized GameStateWithLastPlacementUuid getGameStateWithLastPlacementUuid() {
    return new GameStateWithLastPlacementUuid(masterGame.getState(), lastPlacementID);
  }

  private void restartGame() {
    freshStart = true;
    resetRestartRequests();
    broadcast(ServerNotification.RESTARTING);
    handleStart();
  }

  private void resetRestartRequests() {
    // if both were true, reset has been accepted
    if (restartRequestOne ^ restartRequestTwo) {
      if (restartRequestOne && (connTwo != null)) {
        connTwo.sendMessageWith(ServerNotification.PARTNER_DENIED_RESTART);
      } else if (restartRequestTwo && (connOne != null)) {
        connOne.sendMessageWith(ServerNotification.PARTNER_DENIED_RESTART);
      }
    }

    restartRequestOne = false;
    restartRequestTwo = false;
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
   * Is the game of this lobby running.
   *
   * @return whether the game is in phase running
   */
  public boolean isGameRunning() {
    return masterGame.getState().getCurrentPhase() == Phase.RUNNING;
  }

  /**
   * Get a {@link LobbyRepresentation} for the current state of this lobby.
   *
   * @return a representation of this lobby containing public information
   */
  public synchronized LobbyRepresentation getRepresentation() {
    Phase phase = Phase.WAITING;
    if (masterGame != null) {
      phase = masterGame.getState().getCurrentPhase();
    }
    return new LobbyRepresentation(lobbyID, playerOne, playerTwo, phase);
  }

  private void broadcast(Object objectToBrodcast) {
    Stream.concat(Stream.of(connOne, connTwo), spectatorsConnections.stream())
        .filter(Objects::nonNull)
        .forEach(conn -> conn.sendMessageWith(objectToBrodcast));
  }

  private Object getSpectators() {
    return new Spectators(lobbyID, spectatorsPlayers.values());
  }

  private void closeLobby() {
    broadcast(ServerNotification.LOBBY_CLOSED);
    server.lobbyClosed(lobbyID);
  }

  private void publishLobbyUpdate() {
    server.lobbyUpdated(lobbyID);
  }
}
