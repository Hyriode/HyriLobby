package fr.hyriode.lobby.api.player;

import com.google.gson.Gson;
import fr.hyriode.lobby.api.LobbyAPI;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * The {@link LobbyPlayer} manager
 */
public class LobbyPlayerManager {

    private static final String REDIS_KEY = "player:";

    private final Gson gson;
    private final JedisPool pool;

    /**
     * Constructor of {@link LobbyPlayerManager}
     * @param gson Instance of {@link Gson} to serialize/deserialize data
     * @param pool Instance of {@link JedisPool} to store data
     */
    public LobbyPlayerManager(Gson gson, JedisPool pool) {
        this.gson = gson;
        this.pool = pool;
    }

    /**
     * Get a player from {@link Jedis} with his {@link UUID}
     * @param uuid The player {@link UUID}
     * @return The {@link LobbyPlayer} with the given {@link UUID}
     */
    public LobbyPlayer getPlayer(UUID uuid) {
        final Jedis jedis = this.pool.getResource();
        try {
            return this.gson.fromJson(jedis.get(this.getPlayersKey(uuid)), LobbyPlayer.class);
        } finally {
            jedis.close();
        }
    }

    /**
     * Create a player in {@link Jedis} if he doesn't exist
     * @param uuid The player {@link UUID}
     * @return The created {@link LobbyPlayer}
     */
    public LobbyPlayer createPlayer(UUID uuid) {
        final LobbyPlayer player = new LobbyPlayer(uuid);

        this.sendPlayer(player);
        return player;
    }

    /**
     * Save player in {@link Jedis}
     * @param player The {@link LobbyPlayer} to save
     */
    public void sendPlayer(LobbyPlayer player) {
        final Jedis jedis = this.pool.getResource();
        try {
            jedis.set(this.getPlayersKey(player.getUuid()), this.gson.toJson(player));
        } finally {
            jedis.close();
        }

    }

    /**
     * Delete a player in {@link Jedis}
     * @param uuid The player {@link UUID} to delete
     */
    public void removePlayer(UUID uuid) {
        final Jedis jedis = this.pool.getResource();
        try {
            jedis.del(this.getPlayersKey(uuid));
        } finally {
            jedis.close();
        }
    }

    /**
     * Get the {@link Jedis} key
     * @param uuid The {@link UUID} of the wanted key
     * @return A {@link Jedis} key
     */
    private String getPlayersKey(UUID uuid) {
        return LobbyAPI.REDIS_KEY + REDIS_KEY + uuid.toString();
    }
}
