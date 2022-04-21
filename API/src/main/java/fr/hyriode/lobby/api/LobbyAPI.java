package fr.hyriode.lobby.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;

import java.util.function.Consumer;

/**
 * The API to interact with the lobby.
 */
public class LobbyAPI {

    /**
     * The {@link Gson} instance.
     * Its very important to serialize null values.
     */
    public static final Gson GSON = new GsonBuilder().serializeNulls().create();

    /**
     * The instance of the API.
     */
    private static final LobbyAPI INSTANCE = new LobbyAPI();

    /**
     * The {@link LobbyJumpManager} instance.
     */
    private final LobbyJumpManager jump;
    /**
     * The {@link LobbyPacketManager} instance.
     */
    private final LobbyPacketManager packet;
    /**
     * The {@link LobbyPlayerManager} instance.
     */
    private final LobbyPlayerManager player;
    /**
     * The {@link LobbyLeaderboardManager} instance.
     */
    private final LobbyLeaderboardManager leaderboard;

    /**
     * The constructor of the API.
     */
    LobbyAPI() {
        this.jump = new LobbyJumpManager();
        this.packet = new LobbyPacketManager();
        this.player = new LobbyPlayerManager();
        this.leaderboard = new LobbyLeaderboardManager();
    }

    public void start(Consumer<String[]> consumer) {
        consumer.accept(new String[]{
                "  _           _     _                    _____ _____ ",
                " | |         | |   | |             /\\   |  __ \\_   _|",
                " | |     ___ | |__ | |__  _   _   /  \\  | |__) || |  ",
                " | |    / _ \\| '_ \\| '_ \\| | | | / /\\ \\ |  ___/ | |  ",
                " | |___| (_) | |_) | |_) | |_| |/ ____ \\| |    _| |_ ",
                " |______\\___/|_.__/|_.__/ \\__, /_/    \\_\\_|   |_____|",
                "                           __/ |                     ",
                "                          |___/                      "
        });
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
        return this.jump;
    }

    /**
     * Get the {@link LobbyPacketManager} instance.
     * @return The {@link LobbyPacketManager} instance.
     */
    public LobbyPacketManager getPacketManager() {
        return this.packet;
    }

    /**
     * Get the {@link LobbyPlayerManager} instance.
     * @return The {@link LobbyPlayerManager} instance.
     */
    public LobbyPlayerManager getPlayerManager() {
        return this.player;
    }

    /**
     * Get the {@link LobbyLeaderboardManager} instance.
     * @return The {@link LobbyLeaderboardManager} instance.
     */
    public LobbyLeaderboardManager getLeaderboardManager() {
        return this.leaderboard;
    }
}
