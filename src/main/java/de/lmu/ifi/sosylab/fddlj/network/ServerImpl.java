package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response.ResponseType;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;

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
  private final ServerSocket serverSocket;
  private int nextConnectionID = 0;
  private int nextLobbyID = 0;
  private boolean serverRunning;
  private boolean serverHasBeenStarted;
  private Set<ServerListener> serverListeners;

  private Map<Integer, ClientConnection> connections;
  private ObservableMap<Integer, GameLobby> lobbies;
  private Map<Integer, LobbyRepresentation> lobbyRepresentations;
  private Optional<Integer> freshPublicLobbyWaitingForPlayer;

  /**
   * Initialize a new server. The server will be ready but needs to be started by calling {@link
   * #startServer()}.
   *
   * @throws IOException if a new server cannot be created on the default port or if other problems
   *     occur with necessary IO
   */
  public ServerImpl() throws IOException {
    serverSocket = new ServerSocket(PORT);
    serverListeners = new HashSet<>();
    connections = new HashMap<>();
    lobbies =
        FXCollections.checkedObservableMap(
            FXCollections.observableHashMap(), Integer.class, GameLobby.class);
    lobbies.addListener(this::processUpdateOnLobbyMap);
    lobbyRepresentations = new HashMap<>();
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
    try {
      while (serverRunning) {
        @SuppressWarnings("resource") // ClientConnection manages socket
        Socket newConnection = serverSocket.accept();
        if (newConnection != null) {
          if (serverRunning) {
            handleNewConnection(newConnection);
          } else {
            newConnection.close();
          }
        }
      }
    } catch (
        @SuppressWarnings("unused")
        IOException e) {
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
    boolean asSpectator = joinRequest.isAsSpectator();
    int lobbyID = joinRequest.getLobbyID();

    switch (joinRequest.getJoinType()) {
      case ANY_PUBLIC_LOBBY:
        handlePublicJoin(conn, player, asSpectator);
        break;
      case SPECIFIC_LOBBY:
        handleSpecificJoin(conn, player, asSpectator, lobbyID);
        break;
      case NEW_PRIVATE_LOBBY:
        handlePrivateJoin(conn, player);
        break;
      default:
        break;
    }
  }

  private void handlePublicJoin(ClientConnection conn, Player player, boolean asSpectator) {
    if (asSpectator) {
      GameLobby[] lobbiesArray = lobbies.values().toArray(new GameLobby[0]);
      GameLobby randomLobby = lobbiesArray[new Random().nextInt(lobbiesArray.length)];
      handleLobbyJoinAsSpectator(randomLobby, conn, player);
    } else {
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
    }
  }

  private void handleSpecificJoin(
      ClientConnection conn, Player player, boolean asSpectator, int lobbyID) {
    GameLobby lobby = lobbies.get(lobbyID);
    if (lobby == null) {
      conn.sendMessageWith(new JoinRequest.Response(ResponseType.LOBBY_NOT_FOUND));
    } else {
      if (asSpectator) {
        handleLobbyJoinAsSpectator(lobby, conn, player);
      } else {
        handleLobbyJoinAsPlayer(lobby, conn, player);
      }
    }
  }

  private void handlePrivateJoin(ClientConnection conn, Player player) {
    GameLobby privateLobby = new GameLobby(nextLobbyID++, this);
    handleLobbyJoinAsPlayer(privateLobby, conn, player);
    lobbies.put(privateLobby.getLobbyID(), privateLobby);
  }

  private void handleLobbyJoinAsPlayer(GameLobby lobby, ClientConnection conn, Player player) {
    boolean joinSuccessful = lobby.joinAsPlayer(conn, player);
    if (joinSuccessful) {
      conn.sendMessageWith(
          new JoinRequest.Response(ResponseType.JOIN_SUCCESSFUL, lobby.getLobbyID()));
    } else {
      conn.sendMessageWith(new JoinRequest.Response(ResponseType.NO_PLAYERS_NEEDED));
    }
  }

  private void handleLobbyJoinAsSpectator(GameLobby lobby, ClientConnection conn, Player player) {
    lobby.joinAsSpectator(conn, player);
    conn.sendMessageWith(
        new JoinRequest.Response(ResponseType.JOIN_SUCCESSFUL, lobby.getLobbyID()));
  }

  /**
   * Called when a {@link ClientConnection} is terminated. This is needed to maintain the list of
   * connected clients.
   *
   * @param connectionID integer to reference the connection that has (been) terminated
   */
  public synchronized void connectionTerminated(int connectionID) {
    if (serverRunning) {
      connections.remove(connectionID);
    }
  }

  @Override
  public synchronized void initiateShutdown() {
    serverRunning = false;
    serverListeners.forEach(listener -> listener.serverShuttingDown());
    serverListeners = Set.of();

    terminateAllConnections();
    // Trigger accept(), so that the main while loop stops
    try (Socket connection = new Socket(InetAddress.getLocalHost(), PORT)) {
      assert true; // conform with checkstyle
    } catch (
        @SuppressWarnings("unused")
        IOException e) {
      // do nothing as server is already trying to shutdown
    }
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (
        @SuppressWarnings("unused")
        IOException e) {
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
   * Called when a lobby updated.
   *
   * @param lobbyID integer id to reference the closed lobby
   */
  public void lobbyUpdated(int lobbyID) {
    GameLobby gameLobby = lobbies.get(lobbyID);
    if (gameLobby != null) {
      LobbyRepresentation lobbyRepresentation = gameLobby.getRepresentation();
      lobbyRepresentations.put(lobbyID, lobbyRepresentation);
      notifyListenersOfLobbyUpdate(lobbyID, lobbyRepresentation);
    }
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

  private void processUpdateOnLobbyMap(Change<? extends Integer, ? extends GameLobby> change) {
    Integer key = change.getKey();
    if (change.wasRemoved()) {
      lobbyRepresentations.remove(key);
      notifyListenersOfLobbyUpdate(key, null);
    } else if (change.wasAdded()) {
      lobbyRepresentations.put(key, change.getValueAdded().getRepresentation());
      notifyListenersOfLobbyUpdate(key, lobbyRepresentations.get(key));
    }
  }

  private void notifyListenersOfLobbyUpdate(int lobbyID, LobbyRepresentation lobbyRepresentation) {
    serverListeners.forEach(listener -> listener.lobbyUpdated(lobbyID, lobbyRepresentation));
  }

  @Override
  public void addListener(ServerListener listener) {
    serverListeners.add(listener);
    listener.allLobbies(lobbyRepresentations);
  }

  @Override
  public void removeListener(ServerListener listener) {
    serverListeners.remove(listener);
  }
}
