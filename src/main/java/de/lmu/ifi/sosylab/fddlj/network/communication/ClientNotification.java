package de.lmu.ifi.sosylab.fddlj.network.communication;

/**
 * Predefined notifications a client can send to communicate with the server.
 *
 * @author Leonard Ganz
 */
public enum ClientNotification {
  REQUEST_RESTART,
  ACCEPT_RESTART_REQUEST,
  REQUEST_CURRENT_GAMESTATE_WITH_LAST_PLACEMENT_UUID
}
