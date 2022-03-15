package fr.hyriode.lobby.api;

import com.google.gson.Gson;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;

/**
 * The API to interact with the lobby.
 */
public class LobbyAPI {

    /**
     * The {@link Gson} instance.
     */
    public static final Gson GSON = new Gson();
    /**
     * The base key for storing data in Redis.
     */
    public static final String REDIS_KEY = "lobby:";
    /**
     * The instance of the API.
     */
    private static final LobbyAPI INSTANCE = new LobbyAPI();

    /**
     * The {@link LobbyJumpManager} instance.
     */
    private final LobbyJumpManager jumpManager;
    /**
     * The {@link LobbyLeaderboardManager} instance.
     */
    private final LobbyLeaderboardManager leaderboardManager;
    /**
     * The {@link LobbyPacketManager} instance.
     */
    private final LobbyPacketManager packetManager;
    /**
     * The {@link LobbyPlayerManager} instance.
     */
    private final LobbyPlayerManager playerManager;

    /**
     * The constructor of the API.
     */
    private LobbyAPI() {
        this.jumpManager = new LobbyJumpManager();
        this.leaderboardManager = new LobbyLeaderboardManager();
        this.packetManager = new LobbyPacketManager();
        this.playerManager = new LobbyPlayerManager();
    }

    /**
     * Get the {@link LobbyAPI} instance.
     * @return The {@link LobbyAPI} instance.
     */
    public static LobbyAPI get() {
        return INSTANCE;
    }

    /**
     * Get the {@link LobbyJumpManager} instance.
     * @return The {@link LobbyJumpManager} instance.
     */
    public LobbyJumpManager getJumpManager() {
        return this.jumpManager;
    }

    /**
     * Get the {@link LobbyLeaderboardManager} instance.
     * @return The {@link LobbyLeaderboardManager} instance.
     */
    public LobbyLeaderboardManager getLeaderboardManager() {
        return this.leaderboardManager;
    }

    /**
     * Get the {@link LobbyPacketManager} instance.
     * @return The {@link LobbyPacketManager} instance.
     */
    public LobbyPacketManager getPacketManager() {
        return this.packetManager;
    }

    /**
     * Get the {@link LobbyPlayerManager} instance.
     * @return The {@link LobbyPlayerManager} instance.
     */
    public LobbyPlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
