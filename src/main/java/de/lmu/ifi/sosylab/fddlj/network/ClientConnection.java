package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.GameState;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
  private BufferedWriter out;
  private final Server server;
  private boolean connectionRunning;
  private GameLobby lobby;

  /**
   * Initialize a new client connection. Data streams for communication will be set up.
   *
   * @param clientSocket end point for connection with client
   * @param connectionID integer used to identify connections
   * @param server server that manages all client connections
   */
  public ClientConnection(Socket clientSocket, int connectionID, Server server) {
    this.clientSocket = clientSocket;
    this.connectionID = connectionID;
    this.server = server;
    try {
      out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
      out.flush();
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (@SuppressWarnings("unused") IOException e) {
      // connecting not possible
      terminate();
    }
  }

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
  }

  /**
   * Shutdown the connection to a client. This will close all open data streams
   * and sockets and unregister this connection from the server.
   */
  void terminate() {
    connectionRunning = false;
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

    server.connectionTerminated(this);
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

  public void setLobby(GameLobby lobby) {
    this.lobby = lobby;
  }
}
