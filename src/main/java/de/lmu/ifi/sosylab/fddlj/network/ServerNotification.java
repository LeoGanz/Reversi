package de.lmu.ifi.sosylab.fddlj.network;

/**
 * Predefined notifications the server can send to communicate with its clients.
 *
 * @author Leonard Ganz
 */
public enum ServerNotification {
  SERVER_SHUTTING_DOWN,
  PARTNER_LEFT,
  START, // request that the client shall start the game by sending the first disk placement
  LOBBY_CLOSED,
  RECEIVED_INVALID_DATA,
  PARTNER_REQUESTED_RESTART,
  PARTNER_ACCEPTED_RESTART
}
