package fr.hyriode.lobby.api;

import com.google.gson.Gson;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import redis.clients.jedis.Jedis;

public class LobbyAPI {

    public static final String REDIS_KEY = "lobby:";

    private final LobbyPlayerManager playerManager;

    /**
     * Constructor of {@link LobbyAPI}
     * @param gson Instance of {@link Gson} to serialize/deserialize data
     * @param jedis Instance of {@link Jedis} to store data
     */
    public LobbyAPI(Gson gson, Jedis jedis) {
        this.playerManager = new LobbyPlayerManager(gson, jedis);
    }

    /**
     * Get the {@link LobbyPlayerManager}
     * @return The {@link LobbyPlayerManager}
     */
    public LobbyPlayerManager getPlayerManager() {
        return playerManager;
    }
}
