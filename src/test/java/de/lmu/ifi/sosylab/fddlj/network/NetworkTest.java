package de.lmu.ifi.sosylab.fddlj.network;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import de.lmu.ifi.sosylab.fddlj.model.ModelImpl;
import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.model.PlayerImpl;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest.Response.ResponseType;
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

public class NetworkTest {

  private static final int TIMEOUT = 1000;
  private final Player dummy = new PlayerImpl("Dummy", Color.BLUEVIOLET);
  private Server server;
  private Client client;
  private ClientCompatibleGui mockedGui;

  @BeforeEach
  private void setup() throws IOException {
    server = new ServerImpl();
    server.startServer();

    mockedGui = mock(ClientCompatibleGui.class);

    client = new ClientImpl(mockedGui, InetAddress.getLocalHost(), dummy);
    client.startClient();
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
    verifySuccessfulJoin();
  }

  @Test
  public void testJoinAnyRandomPublicLobby_asSpectator() {
    client.joinAnyRandomPublicLobby(true);
    verifySuccessfulJoin();

    verify(mockedGui, timeout(TIMEOUT)).receivedSpectator(any(Spectators.class));

    verify(mockedGui, timeout(TIMEOUT)).modelExchanged(any(ModelImpl.class));
  }

  private void verifySuccessfulJoin() {
    ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
    verify(mockedGui, timeout(TIMEOUT)).receivedJoinRequestResponse(responseCaptor.capture());
    Assertions.assertEquals(ResponseType.JOIN_SUCCESSFUL, responseCaptor.getValue().getType());
  }
}
