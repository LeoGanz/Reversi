package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Model;
import de.lmu.ifi.sosylab.fddlj.network.communication.JoinRequest;
import de.lmu.ifi.sosylab.fddlj.network.communication.RejectedPlacement;
import de.lmu.ifi.sosylab.fddlj.network.communication.ServerNotification;
import de.lmu.ifi.sosylab.fddlj.network.communication.Spectators;
import de.lmu.ifi.sosylab.fddlj.view.OnlineView;

/**
 * This interface defines callback methods for a GUI. The client uses those defined methods to
 * notify the GUI about join requests, rejected placements by the server, general server
 * notifications, list of spectators and when the model gets updated.
 */
public interface ClientCompatibleGui extends OnlineView {

  /**
   * The client received a {@link JoinRequest.Response} from the server. It can decide, which
   * information should be displayed to the user.
   *
   * @param response Response from the server
   */
  void receivedJoinRequestResponse(JoinRequest.Response response);

  /**
   * The client received a {@link RejectedPlacement.Reason} from the server. It can decide, which
   * information should be displayed to the user.
   *
   * @param rejectedPlacement A reason, why the placement got rejected
   */
  void receivedRejectedPlacementReason(RejectedPlacement.Reason rejectedPlacement);

  /**
   * The client received a {@link ServerNotification} from the server. It can decide, which
   * notifications should be displayed to the user.
   *
   * @param serverNotification Notification from the server
   */
  void receivedServerNotification(ServerNotification serverNotification);

  /**
   * The {@link Model} has been exchanged. This happens, when the client joins a new lobby or the
   * current gamestate gets invalid and needs to be refreshed from the server.
   *
   * @param model updated model
   */
  void modelExchanged(Model model);

  /**
   * The list of spectators, which are currently watching the game.
   *
   * @param spectators list of spectators watching the game
   */
  void receivedSpectator(Spectators spectators);

  /** Called when the client can't connect to the server. */
  void handleConnectionError();
}
