package de.lmu.ifi.sosylab.fddlj.network.communication;

/**
 * Predefined notifications the server can send to communicate with its clients.
 *
 * @author Leonard Ganz
 */
public enum ServerNotification {
  SERVER_SHUTTING_DOWN,
  PLAYER_ONE_LEFT,
  PLAYER_TWO_LEFT,
  RESTARTING,
  LOBBY_CLOSED,
  RECEIVED_INVALID_DATA,
  PARTNER_REQUESTED_RESTART,
  PARTNER_ACCEPTED_RESTART
}
