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
public class Server {

  private static final int PORT = 43200;
  private int nextConnectionID = 0;
  private int nextLobbyID = 0;
  private boolean serverRunning;

  private Map<Integer, ClientConnection> connections;
  private Queue<Integer> waitingForLobby; //ClientConnections referenced by ID
  private Map<Integer, GameLobby> lobbies;

  /**
   * Initialize a new server. The server will be ready but needs to be started by
   * calling {@link #startServer()}.
   */
  public Server() {
    connections = new HashMap<>();
    waitingForLobby = new LinkedList<>();
    lobbies = new HashMap<>();
  }

  /**
   * Start the server. The server will begin to listen for clients trying to
   * connect and then establish a connection with those clients.
   */
  public void startServer() {
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
    } catch (
        @SuppressWarnings("unused") IOException e) {
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

      GameLobby lobby = new GameLobby(connOne, connTwo, nextLobbyID++);
      lobbies.put(lobby.getLobbyID(), lobby);
    }
  }

  /**
   * Called when a {@link ClientConnection} is terminated. This is needed to maintain the list of
   * connected clients.
   *
   * @param clientConnection the connection that has (been) terminated
   */
  synchronized void connectionTerminated(ClientConnection clientConnection) {
    connections.remove(clientConnection.getConnectionID());
    waitingForLobby.remove(clientConnection.getConnectionID());
  }

  /**
   * This method initiates a shutdown. This is only a signal to the server thread that it should
   * stop what ever it is doing. This means that no immediate shutdown is forced.
   */
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

  /**
   * Indicates whether the server is running.
   *
   * @return {@code true} if the server is running, otherwise {@code false}
   */
  public boolean isRunning() {
    return serverRunning;
  }

}
