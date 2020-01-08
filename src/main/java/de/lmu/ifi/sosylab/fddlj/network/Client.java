package de.lmu.ifi.sosylab.fddlj.network;

import de.lmu.ifi.sosylab.fddlj.model.Model;

/**
 * Represents a connected user to a server. The client is able to connect
 * to a server and communicate with it. The client listens to ProbertyChangeEvents,
 * so the actual {@link Model} needs to be provided.
 */
public interface Client {

  /**
   * Starts a connection to the server.
   */
  void startClient();

  /**
   * Closes the connection to the server.
   */
  void terminate();

}
