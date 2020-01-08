package de.lmu.ifi.sosylab.fddlj.network;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
  private Queue<Integer> waitingForLobby; // ClientConnections referenced by ID
  private Map<Integer, GameLobby> lobbies;

  /**
   * Initialize a new server. The server will be ready but needs to be started by calling {@link
   * #startServer()}.
   */
  public ServerImpl() {
    connections = new HashMap<>();
    waitingForLobby = new LinkedList<>();
    lobbies = new HashMap<>();
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
   * Enqueue a connection in the queue of connections waiting to be assigned to a lobby.
   *
   * @param connectionID id that specifies the connection
   */
  public synchronized void enqueueForLobbyBuilding(int connectionID) {
    waitingForLobby.offer(connectionID);
    buildLobbies();
  }

  private void buildLobbies() {
    while (waitingForLobby.size() >= 2) {
      Integer connOneID = waitingForLobby.remove();
      ClientConnection connOne = connections.get(connOneID);
      Integer connTwoID = waitingForLobby.remove();
      ClientConnection connTwo = connections.get(connTwoID);

      GameLobby lobby = new GameLobby(connOne, connTwo, nextLobbyID++, this);
      lobbies.put(lobby.getLobbyID(), lobby);
    }
  }

  /**
   * Called when a {@link ClientConnection} is terminated. This is needed to maintain the list of
   * connected clients.
   *
   * @param clientConnection the connection that has (been) terminated
   */
  synchronized void connectionTerminated(int connectionID) {
    connections.remove(connectionID);
    waitingForLobby.remove(connectionID);
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
              conn.sendNotification(ServerNotification.SERVER_SHUTTING_DOWN);
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
  public void lobbyClosed(int lobbyID) {
    lobbies.remove(lobbyID);
  }
}
