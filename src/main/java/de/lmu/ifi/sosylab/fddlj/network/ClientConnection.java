package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.JsonParseException;
import de.lmu.ifi.sosylab.fddlj.network.communication.ClientNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.DiskPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest;
import de.lmu.ifi.sosylab.fddlj.network.communication.Message;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement.Reason;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
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
        } else {
          processReceivedData(receivedData);
        }
      } catch (@SuppressWarnings("unused") IOException e) {
        // client probably lost connection. Clean up can be done.
        terminate();
      }
    }
  }

  private void processReceivedData(String receivedData) {
    try {
      Message<?> receivedMessage = Message.fromJson(receivedData);
      Object data = receivedMessage.getData();
      if (data instanceof DiskPlacement) {
        handleDiskPlacement((DiskPlacement) data);
      } else if (data instanceof JoinRequest) {
        handleJoinRequest((JoinRequest) data);
      } else if (data instanceof ClientNotification) {
        handleClientNotification((ClientNotification) data);
      } else {
        System.out.println("Received unknown message type: " + data.getClass().getName());
        sendMessageWith(ServerNotification.RECEIVED_INVALID_DATA);
      }
    } catch (@SuppressWarnings("unused") JsonParseException e) {
      sendMessageWith(ServerNotification.RECEIVED_INVALID_DATA);
    }
  }

  private void handleDiskPlacement(DiskPlacement requestedPlacement) {
    if (lobby == null) {
      sendMessageWith(new RejectedPlacement(requestedPlacement.getUuid(), Reason.NO_LOBBY_JOINED));
      return;
    }
    if (!lobby.isGameRunning()) {
      sendMessageWith(new RejectedPlacement(requestedPlacement.getUuid(), Reason.GAME_NOT_RUNNING));
      return;
    }
    RejectedPlacement response = lobby.tryDiskPlacement(requestedPlacement);
    if (response != null) {
      sendMessageWith(response);
    }
  }

  private void handleJoinRequest(JoinRequest joinRequest) {
    if (lobby != null) {
      lobby.leave(connectionID);
    }
    server.joinLobby(joinRequest, connectionID);
  }

  private void handleClientNotification(ClientNotification notification) {
    if (lobby == null) {
      sendMessageWith(ServerNotification.NO_LOBBY_JOINED);
      return;
    }

    switch (notification) {
      case REQUEST_RESTART:
        lobby.requestRestart(connectionID);
        break;
      case ACCEPT_RESTART_REQUEST:
        lobby.acceptRestart(connectionID);
        break;
      case DENY_RESTART_REQUEST:
        lobby.denyRestart(connectionID);
        break;
      case REQUEST_CURRENT_GAMESTATE_WITH_LAST_PLACEMENT_UUID:
        sendMessageWith(lobby.getGameStateWithLastPlacementUuid());
        break;

      default:
        sendMessageWith(ServerNotification.RECEIVED_INVALID_DATA);
        break;
    }
  }

  /**
   * Shutdown the connection to a client. This will close all open data streams and sockets and
   * unregister this connection from the server.
   */
  public void terminate() {
    connectionRunning = false;
    if (lobby != null) {
      lobby.leave(connectionID);
      lobby = null;
    }
    try {
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }

      clientSocket.close();
    } catch (@SuppressWarnings("unused") IOException e) {
      // do nothing, as connection is already doing its best to shut down
    }

    server.connectionTerminated(connectionID);
  }

  /**
   * Send data to the client. The data will be packaged as a {@link Message}.
   *
   * @param data data to be sent to the client in a message
   */
  public void sendMessageWith(Object data) {
    out.println(new Message<>(data).toJson());
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
