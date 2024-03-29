package de.lmu.ifi.sosylab.fddlj.network;

import com.google.gson.JsonSyntaxException;
import de.lmu.ifi.sosylab.fddlj.model.Cell;
import de.lmu.ifi.sosylab.fddlj.model.Disk;
import de.lmu.ifi.sosylab.fddlj.model.GameMode;
import de.lmu.ifi.sosylab.fddlj.model.GameState;
import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.communication.ClientNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.DiskPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.GameStateWithLastPlacementUuid;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest;
import de.lmu.ifi.sosylab.fddlj.network.communication.Message;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * The client enables a communication with a {@link Server}. It can connect to a given address on
 * the port 43200. The client makes callbacks on a {@link ClientCompatibleGui} with the data
 * received from the server.
 */
public class ClientImpl implements Client {

  public static final int PORT = 43200;
  private ClientCompatibleGui compatibleGui;
  private InetAddress serverAddress;
  private Model model;
  private Player clientPlayer;
  private Socket connection;
  private BufferedReader in;
  private PrintWriter out;
  private UUID lastDiskPlacement;
  private boolean connectionEstablished;
  private boolean running;

  /**
   * Creates a new Client. The client will connect to a server, after it's started. To identify
   * within matches a {@link Player} is required. For callbacks for notifications, which are sent by
   * the server a {@link ClientCompatibleGui} must be provided.
   *
   * @param compatibleGui compatibleGui to make callbacks
   * @param inetAddress Address of server to connect
   * @param player Player to identify in matches
   */
  public ClientImpl(ClientCompatibleGui compatibleGui, InetAddress inetAddress, Player player) {
    this.compatibleGui = compatibleGui;
    serverAddress = inetAddress;
    clientPlayer = player;
  }

  @Override
  public void startClient() {
    running = true;

    initServerConnection();

    Thread connectorThread = new Thread(this::communicateWithServer);
    connectorThread.setDaemon(true);
    connectorThread.start();
  }

  private void initServerConnection() {
    try {
      connection = new Socket(serverAddress, PORT);
      out = new PrintWriter(connection.getOutputStream(), true, StandardCharsets.UTF_8);
      out.flush();
      in =
          new BufferedReader(
              new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

      connectionEstablished = true;
    } catch (@SuppressWarnings("unused")IOException e) {
      compatibleGui.handleConnectionError();
      terminate();
    }
  }

  @Override
  public void joinAnyRandomPublicLobby(boolean asSpectator) {
    JoinRequest joinRequest =
        JoinRequest.generateJoinAnyPublicLobbyRequest(clientPlayer, asSpectator);

    sendMessage(joinRequest);
  }

  @Override
  public void joinSpecificLobby(boolean asSpectator, int lobbyId) {
    JoinRequest joinRequest =
        JoinRequest.generateJoinSpecificLobbyRequest(clientPlayer, asSpectator, lobbyId);

    sendMessage(joinRequest);
  }

  @Override
  public void createNewPrivateLobby() {
    JoinRequest joinRequest = JoinRequest.generateJoinNewPrivateLobbyRequest(clientPlayer);

    sendMessage(joinRequest);
  }

  @Override
  public DiskPlacement placeDisk(Disk disk, Cell cell) {
    DiskPlacement diskPlacement = new DiskPlacement(lastDiskPlacement, disk, cell);

    sendMessage(diskPlacement);
    return diskPlacement;
  }

  @Override
  public void requestGameRestart() {
    sendMessage(ClientNotification.REQUEST_RESTART);
  }

  @Override
  public void acceptGameRestart() {
    sendMessage(ClientNotification.ACCEPT_RESTART_REQUEST);
  }

  @Override
  public void denyGameRestart() {
    sendMessage(ClientNotification.DENY_RESTART_REQUEST);
  }

  /**
   * Send a message to a server.
   *
   * @param messageObject message object do send to server
   */
  private void sendMessage(Object messageObject) {
    if (connectionEstablished) {
      out.println(new Message<>(messageObject).toJson());
    }
  }

  private void requestGameStateWithLastPlacementUuid() {
    sendMessage(ClientNotification.REQUEST_CURRENT_GAMESTATE_WITH_LAST_PLACEMENT_UUID);
  }

  private void communicateWithServer() {

    while (running) {
      String receivedLine;
      try {
        receivedLine = in.readLine();
        processReceivedLine(receivedLine);
      } catch (IOException e) {
        terminate();
      }
    }
  }

  private void processReceivedLine(String receivedLine) {
    Message<?> message;

    try {
      message = Message.fromJson(receivedLine);
    } catch (JsonSyntaxException e) {
      System.out.println("Can not decode server answer: " + receivedLine);
      return;
    }

    if (message.getData() instanceof JoinRequest.Response) {
      processJoinRequestResponse((JoinRequest.Response) message.getData());
    } else if (message.getData() instanceof RejectedPlacement) {
      processRejectedPlacement((RejectedPlacement) message.getData());
    } else if (message.getData() instanceof ServerNotification) {
      processServerNotification((ServerNotification) message.getData());
    } else if (message.getData() instanceof DiskPlacement) {
      processDiskPlacement((DiskPlacement) message.getData());
    } else if (message.getData() instanceof Spectators) {
      processSpectators((Spectators) message.getData());
    } else if (message.getData() instanceof GameState) {
      processGamestate((GameState) message.getData());
    } else if (message.getData() instanceof GameStateWithLastPlacementUuid) {
      processGameStateWithLastPlacementUuid((GameStateWithLastPlacementUuid) message.getData());
    }
  }

  private void processJoinRequestResponse(JoinRequest.Response response) {
    compatibleGui.receivedJoinRequestResponse(response);
  }

  private void processRejectedPlacement(RejectedPlacement rejectedPlacement) {
    if ((rejectedPlacement.getReason() == RejectedPlacement.Reason.INVALID_PLACEMENT)
        || (rejectedPlacement.getReason() == RejectedPlacement.Reason.INVALID_PREVIOUS_UUID)) {
      requestGameStateWithLastPlacementUuid();
    }

    compatibleGui.receivedRejectedPlacementReason(rejectedPlacement.getReason());
  }

  private void processServerNotification(ServerNotification serverNotification) {
    if (serverNotification == ServerNotification.SERVER_SHUTTING_DOWN) {
      terminate();
    } else if ((serverNotification == ServerNotification.PLAYER_ONE_LEFT)
        || (serverNotification == ServerNotification.PLAYER_TWO_LEFT)) {
      model.setWaiting();
    }

    compatibleGui.receivedServerNotification(serverNotification);
  }

  private void processDiskPlacement(DiskPlacement diskPlacement) {
    if (!Objects.equals(lastDiskPlacement, diskPlacement.getPrevious())) {
      requestGameStateWithLastPlacementUuid();
      return;
    }

    if (model.placeDisk(diskPlacement.getDisk(), diskPlacement.getLocation())) {
      lastDiskPlacement = diskPlacement.getUuid();
    } else { // DiskPlacement from Server is not valid with current game state
      requestGameStateWithLastPlacementUuid();
    }
  }

  private void processSpectators(Spectators spectators) {
    compatibleGui.receivedSpectator(spectators);
  }

  private void processGamestate(GameState gameState) {
    model = new ModelImpl(gameState, GameMode.MULTIPLAYER);

    compatibleGui.modelExchanged(model);
  }

  private void processGameStateWithLastPlacementUuid(
      GameStateWithLastPlacementUuid gameStateWithLastPlacementUuid) {
    processGamestate(gameStateWithLastPlacementUuid.getGameState());
    lastDiskPlacement = gameStateWithLastPlacementUuid.getLastPlacementUuid();
  }

  @Override
  public void terminate() {
    running = false;
    connectionEstablished = false;
    try {
      if (connection != null) {
        connection.close();
      }
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
    } catch (IOException e) {
      System.out.println("Error due terminating client");
    }
  }
}
