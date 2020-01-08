package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Represents a client on the server. A ClientConnection is responsible for communicating with the
 * client.
 *
 * @author Leonard Ganz
 */
public class ClientConnection implements Runnable {

  private int connectionID;
  private final Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;
  private final ServerImpl server;
  private boolean connectionRunning;
  private GameLobby lobby;

  /**
   * Initialize a new client connection. Data streams for communication will be set up.
   *
   * @param clientSocket end point for connection with client
   * @param connectionID integer used to identify connections
   * @param server server that manages all client connections
   */
  public ClientConnection(Socket clientSocket, int connectionID, ServerImpl server) {
    this.clientSocket = clientSocket;
    this.connectionID = connectionID;
    this.server = server;
    try {
      out = new PrintWriter(clientSocket.getOutputStream(), true, StandardCharsets.UTF_8);
      out.flush();
      in =
          new BufferedReader(
              new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
    } catch (@SuppressWarnings("unused") IOException e) {
      // connecting not possible
      terminate();
    }
  }

  /**
   * Get this connections identifier.
   *
   * @return integer id used to identify the client connection
   */
  public int getConnectionID() {
    return connectionID;
  }

  @Override
  public void run() {
    connectionRunning = true;
    while (server.isRunning() && connectionRunning) {
      try {
        if (in == null) {
          terminate();
          return;
        }
        String receivedData = in.readLine();
        if (receivedData == null) { //EOF
          terminate();
        }
        processReceivedData(receivedData);
      } catch (@SuppressWarnings("unused") IOException e) {
        // client probably lost connection. Clean up can be done.
        terminate();
      }
    }
  }

  private void processReceivedData(String receivedData) {
    // TODO deserialize JSON and call specific handler for received data type
    // forward game data to lobby
    // enqueue for lobby building if requested and not yet in lobby
  }

  /**
   * Shutdown the connection to a client. This will close all open data streams
   * and sockets and unregister this connection from the server.
   */
  void terminate() {
    connectionRunning = false;
    lobby.leave(connectionID);
    lobby = null;
    try {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }

      clientSocket.close();
    } catch (@SuppressWarnings("unused") IOException e) {
      // do nothing, as connection is already trying to shut down
    }

    server.connectionTerminated(connectionID);
  }

  /**
   * Send a game state to the client.
   *
   * @param state the state to send
   */
  public void sendState(GameState state) {
    // TODO generate Json and send
  }

  /**
   * Send a server notification to the client.
   *
   * @param notification the notification to send
   */
  public void sendNotification(ServerNotification notification) {
    // TODO generate Json and send
  }

  /**
   * Set the lobby this client connection is in.
   *
   * @param lobby the game lobby this connection is in
   */
  public void setLobby(GameLobby lobby) {
    this.lobby = lobby;
  }
}
