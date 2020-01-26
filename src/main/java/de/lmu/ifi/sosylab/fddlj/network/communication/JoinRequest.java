package de.lmu.ifi.sosylab.fddlj.network.communication;

import de.lmu.ifi.sosylab.fddlj.model.Player;
import de.lmu.ifi.sosylab.fddlj.network.Server;
import java.util.Objects;

/**
 * Communication format for the client to request to join a lobby on the server.
 *
 * @author Leonard Ganz
 */
public class JoinRequest {
  private Player player;
  private JoinType joinType;
  private boolean asSpectator;
  private int lobbyID;

  private JoinRequest(Player player, JoinType joinType, boolean asSpectator, int lobbyID) {
    this.player = Objects.requireNonNull(player);
    this.joinType = joinType;
    this.asSpectator = asSpectator;
    this.lobbyID = lobbyID;
  }

  /**
   * Generate a request for the provided player to join any public lobby on the server. The player
   * is thereby asking to be included in automatic matchmaking.
   *
   * @param player the player that wants to join
   * @param asSpectator whether the player wants to join as a spectator or as a participating player
   * @return the request that can be sent to the {@link Server}
   */
  public static JoinRequest generateJoinAnyPublicLobbyRequest(Player player, boolean asSpectator) {
    return new JoinRequest(player, JoinType.ANY_PUBLIC_LOBBY, asSpectator, -1);
  }

  /**
   * Generate a request for the provided player to join a specific lobby on the server. This kind of
   * request can be used if a player that has previously lost connection wants to rejoin a lobby or
   * if he wants to join a pseudo-private lobby a partner has created. In both cases the player
   * needs to now or be told the ID of the lobby on the server.
   *
   * @param player the player that wants to join
   * @param asSpectator whether the player wants to join as a spectator or as a participating player
   * @param lobbyID the ID of the game lobby to join
   * @return the request that can be sent to the {@link Server}
   */
  public static JoinRequest generateJoinSpecificLobbyRequest(
      Player player, boolean asSpectator, int lobbyID) {
    return new JoinRequest(player, JoinType.SPECIFIC_LOBBY, asSpectator, lobbyID);
  }

  /**
   * Generate a request for a new pseudo-private lobby on the server, i.e. a lobby that is not
   * included in public matchmaking but can be accessed via the lobby ID the server will provide.
   * The ID needs to be shared via third party tools.
   *
   * @param player the player that wants to join
   * @return the request that can be sent to the {@link Server}
   */
  public static JoinRequest generateJoinNewPrivateLobbyRequest(Player player) {
    return new JoinRequest(player, JoinType.NEW_PRIVATE_LOBBY, false, -1);
  }

  /**
   * Get the {@link Player} making the request.
   *
   * @return the player that requests to join
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Get the {@link JoinType} of this request.This is mainly relevant for interpretation by the
   * {@link Server}.
   *
   * @return the request's join type
   */
  public JoinType getJoinType() {
    return joinType;
  }

  /**
   * Whether the player wants to join as a spectator. Only applicable for some {@link JoinType}s.
   *
   * @return whether the player wants to join as a spectator
   */
  public boolean isAsSpectator() {
    return asSpectator;
  }

  /**
   * Get the ID of the lobby the player wants to join. Only applicable for some {@link JoinType}s.
   *
   * @return the lobby ID
   */
  public int getLobbyID() {
    return lobbyID;
  }

  /**
   * Type of the {@link JoinRequest}. This is mainly relevant for interpretation by the {@link
   * Server}.
   *
   * @author Leonard Ganz
   */
  public enum JoinType {
    ANY_PUBLIC_LOBBY, // random match making
    SPECIFIC_LOBBY, // lobby by ID
    NEW_PRIVATE_LOBBY // new lobby not included in random matchmaking
  }

  /**
   * The responses a server will send upon processing of the {@link JoinRequest}.
   *
   * @author Leonard Ganz
   */
  public static class Response {

    /**
     * Type of the response.
     *
     * @author Leonard Ganz
     */
    public enum ResponseType {
      JOIN_SUCCESSFUL,
      LOBBY_NOT_FOUND,
      NO_LOBBY_AVAILABLE,
      NO_PLAYERS_NEEDED;
    }

    private ResponseType type;
    private int lobbyID;

    /**
     * Create a new response to a join request.
     *
     * @param type the kind of response, i.e. the actual response
     * @param lobbyID if join was successful, provide the ID of the lobby the player joined into
     */
    public Response(ResponseType type, int lobbyID) {
      this.type = type;
      this.lobbyID = lobbyID;
    }

    /**
     * Create a new response to a join request without lobbyID information.
     *
     * @param type the kind of response, i.e. the actual response
     */
    public Response(ResponseType type) {
      this(type, -1);
    }

    public ResponseType getType() {
      return type;
    }

    /**
     * For successful lobby joining a lobbyID should have been set that can be retrieved with this
     * method.
     *
     * @return the lobby id of the lobby that was joined, <code>-1</code> indicates that no
     *     information has been set
     */
    public int getLobbyID() {
      return lobbyID;
    }
  }
}
