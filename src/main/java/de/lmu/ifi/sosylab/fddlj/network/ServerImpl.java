package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The server provides means of communication for clients. It will accept connections and pair them
 * to enable them to play against each other. Furthermore the server sends notifications to manage
 * the game flow.
 *
 * @see ServerNotification
 * @author Leonard Ganz
 */
public class ServerImpl implements Server {

  private static final int PORT = 43200;
  private int nextConnectionID = 0;
  private int nextLobbyID = 0;
  private boolean serverRunning;
  private boolean serverHasBeenStarted;

  private Map<Integer, ClientConnection> connections;
  private Map<Integer, GameLobby> lobbies;
  private Optional<Integer> freshPublicLobbyWaitingForPlayer;

  /**
   * Initialize a new server. The server will be ready but needs to be started by calling {@link
   * #startServer()}.
   */
  public ServerImpl() {
    connections = new HashMap<>();
    lobbies = new HashMap<>();
    freshPublicLobbyWaitingForPlayer = Optional.empty();
  }

  @Override
  public void startServer() {
    if (serverHasBeenStarted) { // server was started before
      throw new IllegalStateException("Server has been started before");
    }
    serverHasBeenStarted = true;
    serverRunning = true;
    Thread connectorThread = new Thread(this::acceptConnections);
    connectorThread.setDaemon(true);
    connectorThread.start();
  }

  private void acceptConnections() {
    try (ServerSocket socket = new ServerSocket(PORT)) {
      while (serverRunning) {
        @SuppressWarnings("resource") // ClientConnection manages socket
        Socket newConnection = socket.accept();
        if (newConnection != null) {
          if (serverRunning) {
            handleNewConnection(newConnection);
          } else {
            newConnection.close();
          }
        }
      }
    } catch (@SuppressWarnings("unused") BindException e) {
      // TODO Handle server already running
    } catch (@SuppressWarnings("unused") IOException e) {
      // TODO Inform about exception
      initiateShutdown();
    }
  }

  private void handleNewConnection(Socket clientSocket) {
    ClientConnection clientConnection =
        new ClientConnection(clientSocket, nextConnectionID++, this);

    connections.put(clientConnection.getConnectionID(), clientConnection);
    Thread thread =
        new Thread(clientConnection, "ClientConnection-" + clientConnection.getConnectionID());
    thread.start();
  }

  /**
   * Handle a join request from a specified connection. Lobbies are built if needed.
   *
   * @param joinRequest data needed for joining a lobby
   * @param connectionID integer to specify the joining connection
   */
  public synchronized void joinLobby(JoinRequest joinRequest, int connectionID) {
    ClientConnection conn = connections.get(connectionID);
    Player player = joinRequest.getPlayer();
    switch (joinRequest.getJoinType()) {
      case ANY_PUBLIC_LOBBY:
        if (freshPublicLobbyWaitingForPlayer.isPresent()) {
          GameLobby lobby = lobbies.get(freshPublicLobbyWaitingForPlayer.get());
          handleLobbyJoinAsPlayer(lobby, conn, player);
          freshPublicLobbyWaitingForPlayer = Optional.empty();
        } else {
          GameLobby lobby = new GameLobby(nextLobbyID++, this);
          handleLobbyJoinAsPlayer(lobby, conn, player);
          lobbies.put(lobby.getLobbyID(), lobby);
          freshPublicLobbyWaitingForPlayer = Optional.of(lobby.getLobbyID());
        }
        break;
      case SPECIFIC_LOBBY:
        GameLobby lobby = lobbies.get(joinRequest.getLobbyID());
        if (lobby == null) {
          conn.sendMessageWith(JoinRequest.Response.LOBBY_NOT_FOUND);
        } else {
          if (joinRequest.isAsSpectator()) {
            lobby.joinAsSpectator(conn, player);
            conn.sendMessageWith(JoinRequest.Response.JOIN_SUCCESSFUL);
          } else {
            handleLobbyJoinAsPlayer(lobby, conn, player);
          }
        }
        break;
      case NEW_PRIVATE_LOBBY:
        GameLobby privateLobby = new GameLobby(nextLobbyID++, this);
        privateLobby.joinAsPlayer(conn, player);
        lobbies.put(privateLobby.getLobbyID(), privateLobby);
        break;
      default:
        break;
    }
  }

  private void handleLobbyJoinAsPlayer(GameLobby lobby, ClientConnection conn, Player player) {
    boolean joinSuccessful = lobby.joinAsPlayer(conn, player);
    if (joinSuccessful) {
      conn.sendMessageWith(JoinRequest.Response.JOIN_SUCCESSFUL);
    } else {
      conn.sendMessageWith(JoinRequest.Response.NO_PLAYERS_NEEDED);
    }
  }

  /**
   * Called when a {@link ClientConnection} is terminated. This is needed to maintain the list of
   * connected clients.
   *
   * @param connectionID integer to reference the connection that has (been) terminated
   */
  public synchronized void connectionTerminated(int connectionID) {
    connections.remove(connectionID);
  }

  @Override
  public synchronized void initiateShutdown() {
    serverRunning = false;
    terminateAllConnections();
    // TODO terminate all lobbies needed?
    // Trigger accept(), so that the main while loop stops
    try (Socket connection = new Socket(InetAddress.getLocalHost(), PORT)) {
      assert true; // conform with checkstyle
    } catch (@SuppressWarnings("unused") IOException e) {
      // do nothing as server is already trying to shutdown
    }
  }

  private void terminateAllConnections() {
    connections
        .values()
        .forEach(
            conn -> {
              conn.sendMessageWith(ServerNotification.SERVER_SHUTTING_DOWN);
              conn.terminate();
            });
  }

  @Override
  public boolean isRunning() {
    return serverRunning;
  }

  /**
   * Called when a {@link GameLobby} closes.
   *
   * @param lobbyID integer id to reference the closed lobby
   */
  public synchronized void lobbyClosed(int lobbyID) {
    lobbies.remove(lobbyID);
  }

  /**
   * Get the connection with the specified ID if available.
   *
   * @param connectionID integer to reference the connection
   * @return the client connection
   */
  public ClientConnection getConnection(int connectionID) {
    return connections.get(connectionID);
  }
}
