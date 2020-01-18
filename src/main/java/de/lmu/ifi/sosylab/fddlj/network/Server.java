package de.lmu.ifi.sosylab.fddlj.network;

/**
 * A server is responsible for managing clients and games. This interface only provides methods for
 * controlling the server.
 *
 * @author Leonard Ganz
 */
public interface Server {

  /**
   * Start the server. The server will begin to listen for clients trying to connect and then
   * establish a connection with those clients.
   *
   * <p>It is never legal to start a server more than once. In particular, a server may not be
   * restarted once a shutdown has been initiated.
   *
   * @throws IllegalStateException if server was already started before
   * @see #initiateShutdown()
   */
  public void startServer();

  /**
   * This method initiates a shutdown. This is only a signal to the server thread that it should
   * stop what ever it is doing and terminate all connections. That means that no immediate shutdown
   * is forced.
   */
  public void initiateShutdown();

  /**
   * Indicates whether the server is running.
   *
   * @return {@code true} if the server is running, otherwise {@code false}
   */
  public boolean isRunning();
}
