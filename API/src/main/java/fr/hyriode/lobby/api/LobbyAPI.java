package fr.hyriode.lobby.api;

import com.google.gson.Gson;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;

public class LobbyAPI {

    public static final Gson GSON = new Gson();
    public static final String REDIS_KEY = "lobby:";
    private static final LobbyAPI INSTANCE = new LobbyAPI();

    private final LobbyJumpManager jumpManager;
    private final LobbyLeaderboardManager leaderboardManager;
    private final LobbyPacketManager packetManager;
    private final LobbyPlayerManager playerManager;

    public LobbyAPI() {
        this.jumpManager = new LobbyJumpManager();
        this.leaderboardManager = new LobbyLeaderboardManager();
        this.packetManager = new LobbyPacketManager();
        this.playerManager = new LobbyPlayerManager();
    }

    public static LobbyAPI get() {
        return INSTANCE;
    }

    public LobbyJumpManager getJumpManager() {
        return this.jumpManager;
    }

    public LobbyLeaderboardManager getLeaderboardManager() {
        return this.leaderboardManager;
    }

    public LobbyPacketManager getPacketManager() {
        return this.packetManager;
    }

    public LobbyPlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
