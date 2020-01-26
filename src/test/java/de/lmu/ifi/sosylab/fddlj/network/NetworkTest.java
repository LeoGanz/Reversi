package de.lmu.ifi.sosylab.fddlj.network;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import de.lmu.ifi.sosylab.fddlj.model.CellImpl;
import de.lmu.ifi.sosylab.fddlj.model.DiskImpl;
import de.lmu.ifi.sosylab.fddlj.model.GameField;
import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response.ResponseType;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement.Reason;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import java.io.IOException;
import java.net.InetAddress;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for network communication. Real connections are build and then tests will be executed to
 * check whether sent data from the client is correctly responded to by the server.
 *
 * @author Leonard Ganz
 */
public class NetworkTest {

  private static final int TIMEOUT = 3000;
  private Server server;
  private final Player dummy = new PlayerImpl("Dummy", Color.BLUEVIOLET);
  private final Player dummyTwo = new PlayerImpl("DummyTwo", Color.ALICEBLUE);
  private final Player dummyThree = new PlayerImpl("DummyThree", Color.SADDLEBROWN);
  private Client client;
  private Client clientTwo;
  private Client clientThree;
  private ClientCompatibleGui mockedGui;
  private ClientCompatibleGui mockedGuiTwo;
  private ClientCompatibleGui mockedGuiThree;

  @BeforeEach
  private void setup() throws IOException {
    server = new ServerImpl();
    server.startServer();

    mockedGui = mock(ClientCompatibleGui.class);
    mockedGuiTwo = mock(ClientCompatibleGui.class);
    mockedGuiThree = mock(ClientCompatibleGui.class);

    client = new ClientImpl(mockedGui, InetAddress.getLocalHost(), dummy);
    client.startClient();

    clientTwo = new ClientImpl(mockedGuiTwo, InetAddress.getLocalHost(), dummyTwo);
    clientTwo.startClient();

    clientThree = new ClientImpl(mockedGuiThree, InetAddress.getLocalHost(), dummyThree);
    clientThree.startClient();
  }

  @AfterEach
  private void teardown() {
    server.initiateShutdown();
    client.terminate();
  }

  @Test
  public void testAcceptGameRestart_NoLobby() {
    client.acceptGameRestart();
    verify(mockedGui, timeout(TIMEOUT))
        .receivedServerNotification(ServerNotification.NO_LOBBY_JOINED);
  }

  @Test
  public void testJoinAnyRandomPublicLobby_asPlayer() {
    client.joinAnyRandomPublicLobby(false);
    verifyJoinResponse(mockedGui, ResponseType.JOIN_SUCCESSFUL);
  }

  @Test
  public void testJoinAnyRandomPublicLobby_asSpectator_NoLobby() {
    client.joinAnyRandomPublicLobby(true);
    verifyJoinResponse(mockedGui, ResponseType.NO_LOBBY_AVAILABLE);
  }

  @Test
  public void testJoinAnyRandomPublicLobby_asSpectator_GameNotStarted() {
    clientTwo.joinAnyRandomPublicLobby(false);
    verifyJoinResponse(mockedGuiTwo, ResponseType.JOIN_SUCCESSFUL);
    client.joinAnyRandomPublicLobby(true);
    verifyJoinResponse(mockedGui, ResponseType.JOIN_SUCCESSFUL);

    verify(mockedGui, timeout(TIMEOUT)).receivedSpectator(any(Spectators.class));
  }

  @Test
  public void testJoinAnyRandomPublicLobby_asSpectator_GameStarted() {
    letOneAndTwoJoinLobby0();
    clientThree.joinAnyRandomPublicLobby(true);
    verifyJoinResponse(mockedGuiThree, ResponseType.JOIN_SUCCESSFUL);

    verify(mockedGuiThree, timeout(TIMEOUT)).receivedSpectator(any(Spectators.class));

    verify(mockedGuiThree, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
  }

  @Test
  public void testJoinSpecificLobby_Invalid_Neg5() {
    client.joinSpecificLobby(false, -5);
    verifyJoinResponse(mockedGui, ResponseType.LOBBY_NOT_FOUND);
    clientTwo.joinSpecificLobby(true, -5);
    verifyJoinResponse(mockedGuiTwo, ResponseType.LOBBY_NOT_FOUND);
  }

  @Test
  public void testJoinSpecificLobby_Invalid_0() {
    client.joinSpecificLobby(false, 0);
    verifyJoinResponse(mockedGui, ResponseType.LOBBY_NOT_FOUND);
    clientTwo.joinSpecificLobby(true, 0);
    verifyJoinResponse(mockedGuiTwo, ResponseType.LOBBY_NOT_FOUND);
  }

  @Test
  public void testJoinSpecificLobby_Invalid_5() {
    client.joinSpecificLobby(false, -5);
    verifyJoinResponse(mockedGui, ResponseType.LOBBY_NOT_FOUND);
    clientTwo.joinSpecificLobby(true, 5);
    verifyJoinResponse(mockedGuiTwo, ResponseType.LOBBY_NOT_FOUND);
  }

  @Test
  public void testNewLobbyAndSpecificLobbyRegular() {
    client.createNewPrivateLobby();
    verifyJoinResponse(mockedGui, ResponseType.JOIN_SUCCESSFUL);
    clientTwo.joinSpecificLobby(false, 0);
    verifyJoinResponse(mockedGuiTwo, ResponseType.JOIN_SUCCESSFUL);
    clientThree.joinSpecificLobby(true, 0);
    verifyJoinResponse(mockedGuiThree, ResponseType.JOIN_SUCCESSFUL);

    verify(mockedGui, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
    verify(mockedGuiTwo, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
    verify(mockedGuiThree, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
  }

  @Test
  public void testJoinLobby_NoPlayersNeeded() {
    letOneAndTwoJoinLobby0();
    clientThree.joinSpecificLobby(false, 0);
    verifyJoinResponse(mockedGuiThree, ResponseType.NO_PLAYERS_NEEDED);
  }

  private void verifyJoinResponse(ClientCompatibleGui gui, ResponseType type) {
    ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
    verify(gui, timeout(TIMEOUT)).receivedJoinRequestResponse(responseCaptor.capture());
    Assertions.assertEquals(type, responseCaptor.getValue().getType());
  }

  @Test
  public void testServerShutdown() {
    client.joinAnyRandomPublicLobby(false);
    clientTwo.joinAnyRandomPublicLobby(false);
    server.initiateShutdown();
    verify(mockedGui, timeout(TIMEOUT))
        .receivedServerNotification(ServerNotification.SERVER_SHUTTING_DOWN);
    verify(mockedGuiTwo, timeout(TIMEOUT))
        .receivedServerNotification(ServerNotification.SERVER_SHUTTING_DOWN);
  }

  @Test
  public void testLeavingAndResume() {
    letOneAndTwoJoinLobby0();

    client.terminate();
    verify(mockedGuiTwo, timeout(TIMEOUT))
        .receivedServerNotification(ServerNotification.PLAYER_ONE_LEFT);

    clientThree.joinSpecificLobby(false, 0);
    verifyJoinResponse(mockedGuiThree, ResponseType.JOIN_SUCCESSFUL);

    verify(mockedGui, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
    verify(mockedGuiThree, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
  }

  @Test
  public void testDiskPlacement_Valid() {
    letOneAndTwoJoinLobby0();

    client.placeDisk(
        new DiskImpl(dummy),
        new CellImpl(GameField.STANDARD_SIZE / 2, GameField.STANDARD_SIZE / 2));

    verify(mockedGui, after(TIMEOUT).never())
        .receivedRejectedPlacementReason(any(RejectedPlacement.Reason.class));
    verify(mockedGuiTwo, after(TIMEOUT).never())
        .receivedRejectedPlacementReason(any(RejectedPlacement.Reason.class));

  }

  @Test
  public void testDiskPlacement_NotYourTurn() {
    letOneAndTwoJoinLobby0();

    clientTwo.placeDisk(
        new DiskImpl(dummyTwo),
        new CellImpl(GameField.STANDARD_SIZE / 2, GameField.STANDARD_SIZE / 2));

    verify(mockedGuiTwo, timeout(TIMEOUT)).receivedRejectedPlacementReason(Reason.NOT_YOUR_TURN);
  }

  @Test
  public void testDiskPlacement_Invalid() {
    letOneAndTwoJoinLobby0();

    client.placeDisk(new DiskImpl(dummy), new CellImpl(0, 0));

    verify(mockedGui, timeout(TIMEOUT)).receivedRejectedPlacementReason(Reason.INVALID_PLACEMENT);
  }

  private void letOneAndTwoJoinLobby0() {
    client.joinAnyRandomPublicLobby(false);
    verifyJoinResponse(mockedGui, ResponseType.JOIN_SUCCESSFUL);
    clientTwo.joinAnyRandomPublicLobby(false);
    verifyJoinResponse(mockedGuiTwo, ResponseType.JOIN_SUCCESSFUL);

  }
}
