package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.*;
import de.lmu.ifi.sosylab.fddlj.network.communication.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientImpl implements Client {

  public static final int PORT = 43200;
  private ClientCompatibleGui compatibleGUI;
  private InetAddress serverAddress;
  private Model model;
  private Player clientPlayer;
  private Socket connection;
  private BufferedReader in;
  private PrintWriter out;
  private UUID lastDiskPlacement;
  private boolean connectionEstablished;
  private boolean running;


  public ClientImpl(ClientCompatibleGui compatibleGUI, InetAddress inetAddress, Player player) {
    this.compatibleGUI = compatibleGUI;
    this.serverAddress = inetAddress;
    this.clientPlayer = player;
  }

  public void startClient() {
    this.running = true;

    Thread connectorThread = new Thread(this::communicateWithServer);
  }

  public void joinAnyRandomPublicLobby() {
    JoinRequest joinRequest = JoinRequest.generateJoinAnyPublicLobbyRequest(this.clientPlayer);

    this.sendMessage(joinRequest);
  }

  public void joinSpecificLobby(boolean asSpectator, int lobbyId) {
    JoinRequest joinRequest = JoinRequest.generateJoinSpecificLobbyRequest(
            this.clientPlayer, asSpectator, lobbyId);

    this.sendMessage(joinRequest);
  }

  public void createNewPrivateLobby() {
    JoinRequest joinRequest = JoinRequest.generateJoinNewPrivateLobbyRequest(this.clientPlayer);

    this.sendMessage(joinRequest);
  }

  public DiskPlacement placeDisk(Disk disk, Cell cell) {
    DiskPlacement diskPlacement = new DiskPlacement(this.lastDiskPlacement, disk, cell);

    this.sendMessage(diskPlacement);
    return diskPlacement;
  }

  public void requestGameRestart() {
    this.sendMessage(ClientNotification.REQUEST_RESTART);
  }

  public void acceptGameRestart() {
    this.sendMessage(ClientNotification.ACCEPT_RESTART_REQUEST);
  }

  /**
   * Send a message to a server.
   *
   * @param messageObject message object do send to server
   * @return Whether the message can be delivered successfully
   */
  private void sendMessage(Object messageObject) {
    this.out.println(new Message<>(messageObject).toJson());
  }

  private void requestGameStateWithLastPlacementUUID() {
    this.sendMessage(ClientNotification.REQUEST_CURRENT_GAMESTATE_WITH_LAST_PLACEMENT_UUID);
  }

  private void notifyClientAboutUpdatedModel() {
    this.compatibleGUI.modelUpdated(this.model);
  }

  private void communicateWithServer() {
    try {
      this.connection = new Socket(this.serverAddress, PORT);
      this.out = new PrintWriter(connection.getOutputStream(), true, StandardCharsets.UTF_8);
      this.out.flush();
      this.in = new BufferedReader(
                      new InputStreamReader(this.connection.getInputStream(), StandardCharsets.UTF_8));

      this.connectionEstablished = true;
    } catch (@SuppressWarnings("unused") IOException e) {
      terminate();
    }

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
    Message message = Message.fromJson(receivedLine);

    if(message.getData() instanceof JoinRequest.Response) {
      this.processJoinRequestResponse((JoinRequest.Response) message.getData());
    } else if (message.getData() instanceof RejectedPlacement) {
      this.processRejectedPlacement((RejectedPlacement) message.getData());
    } else if(message.getData() instanceof ServerNotification) {
      this.processServerNotification((ServerNotification) message.getData());
    } else if(message.getData() instanceof DiskPlacement) {
      this.processDiskPlacement((DiskPlacement) message.getData());
    } else if(message.getData() instanceof Spectators) {
      this.processSpectators((Spectators) message.getData());
    } else if(message.getData() instanceof GameState) {
      this.processGamestate((GameState) message.getData());
    } else if(message.getData() instanceof GameStateWithLastPlacementUuid) {
      this.processGameStateWithLastPlacementUuid((GameStateWithLastPlacementUuid) message.getData());
    }
  }

  private void processJoinRequestResponse(JoinRequest.Response response) {
    this.compatibleGUI.receivedJoinRequestResponse(response);
  }

  private void processRejectedPlacement(RejectedPlacement rejectedPlacement) {
    if (rejectedPlacement.getReason() == RejectedPlacement.Reason.INVALID_PLACEMENT ||
            rejectedPlacement.getReason() == RejectedPlacement.Reason.INVALID_PREVIOUS_UUID) {
      this.requestGameStateWithLastPlacementUUID();
    }

    this.compatibleGUI.receivedRejectedPlacementReason(rejectedPlacement.getReason());
  }

  private void processServerNotification(ServerNotification serverNotification) {
    if(serverNotification == ServerNotification.SERVER_SHUTTING_DOWN) {
      this.terminate();
    } else if(serverNotification == ServerNotification.PLAYER_ONE_LEFT ||
            serverNotification == ServerNotification.PLAYER_TWO_LEFT) {
      this.model.setWaiting();
    }

    this.compatibleGUI.receivedServerNotification(serverNotification);
  }

  private void processDiskPlacement(DiskPlacement diskPlacement) {
    if(this.lastDiskPlacement != diskPlacement.getPrevious()) {
      this.requestGameStateWithLastPlacementUUID();
      return;
    }

    if(this.model.placeDisk(diskPlacement.getDisk(), diskPlacement.getLocation())) {
      this.lastDiskPlacement = diskPlacement.getUuid();

      this.notifyClientAboutUpdatedModel();
    } else { // DiskPlacement from Server is not valid with current game state
      this.requestGameStateWithLastPlacementUUID();
    }
  }

  private void processSpectators(Spectators spectators) {
    this.compatibleGUI.receivedSpectator(spectators);
  }

  private void processGamestate(GameState gameState) {
    this.model = new ModelImpl(gameState, GameMode.MULTIPLAYER);

    this.notifyClientAboutUpdatedModel();
  }

  private void processGameStateWithLastPlacementUuid(GameStateWithLastPlacementUuid gameStateWithLastPlacementUuid) {
    this.processGamestate(gameStateWithLastPlacementUuid.getGameState());
    this.lastDiskPlacement = gameStateWithLastPlacementUuid.getLastPlacementUuid();
  }

  /**
   * Terminate the client by closing with the connection to the server.
   */
  public void terminate() {
    this.running = false;
    this.connectionEstablished = false;
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
      e.printStackTrace();
    }
  }

}
