package fr.hyriode.lobby.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents the manager for players in the lobby.
 */
public class LobbyPlayerManager implements ILobbyDataManager<LobbyPlayer, UUID> {

    /**
     * The base key for storing data in Redis.
     */
    private static final String REDIS_KEY = "player:";

    /**
     * The {@link IHyriRedisProcessor} instance.
     */
    private final IHyriRedisProcessor redisProcessor;

    /**
     * The constructor of the player manager.
     */
    public LobbyPlayerManager() {
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }

    /**
     * Get a player from his UUID.
     * @param key The key of the data to get.
     * @return The player with the given UUID.
     */
    @Override
    public LobbyPlayer get(UUID key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + REDIS_KEY + key), LobbyPlayer.class);
        }
    }

    /**
     * Save a player in the database.
     * @param data The player to save.
     */
    @Override
    public void save(LobbyPlayer data) {
        this.redisProcessor.process(jedis -> jedis.set(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getUniqueId(), LobbyAPI.GSON.toJson(data)));
    }

    /**
     * Delete a player from the database.
     * @param data The player to delete.
     */
    @Override
    public void delete(LobbyPlayer data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getUniqueId()));
    }

    /**
     * Get all the players in the database as keys.
     * @return The players as keys.
     */
    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + REDIS_KEY + "*");
        }
    }

    /**
     * Get all the players in the database as values.
     * @return The players as values.
     */
    @Override
    public Set<LobbyPlayer> getAllKeysAsValues() {
        final Set<LobbyPlayer> players = new HashSet<>();
        this.getAllKeys().forEach(key -> players.add(this.get(UUID.fromString(key.split(":")[key.split(":").length - 1]))));
        return players;
    }
}
