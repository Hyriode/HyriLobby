package fr.hyriode.lobby.api;

import com.google.gson.Gson;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import redis.clients.jedis.JedisPool;

public class LobbyAPI {

    public static final String REDIS_KEY = "lobby:";

    private final Gson gson;
    private final JedisPool jedisPool;
    private final LobbyPlayerManager playerManager;

    /**
     * Constructor of {@link LobbyAPI}
     * @param gson Instance of {@link Gson} to serialize/deserialize data
     * @param jedis Instance of {@link JedisPool} to store data
     */
    public LobbyAPI(Gson gson, JedisPool jedis) {
        this.gson = gson;
        this.jedisPool = jedis;
        this.playerManager = new LobbyPlayerManager(this);
    }

    /**
     * Get {@link Gson}
     * @return {@link Gson}
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * Get the {@link JedisPool}
     * @return The {@link JedisPool}
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * Get the {@link LobbyPlayerManager}
     * @return The {@link LobbyPlayerManager}
     */
    public LobbyPlayerManager getPlayerManager() {
        return playerManager;
    }
}
