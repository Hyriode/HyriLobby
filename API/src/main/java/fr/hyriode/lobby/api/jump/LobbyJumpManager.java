package fr.hyriode.lobby.api.jump;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Represents the manager of the jump system.
 */
public class LobbyJumpManager implements ILobbyDataManager<LobbyJump, String> {

    /**
     * The base key for storing data in Redis.
     */
    private static final String REDIS_KEY = "jump:";
    /**
     * A map containing timer tasks ids, where {@link UUID} is the player's UUID and {@link Integer} is the task id.
     */
    private final Map<UUID, Integer> taskIds;
    /**
     * A map containing the jump timers, where {@link UUID} is the player's UUID and {@link Integer} is the jump timer.
     */
    private final Map<UUID, Integer> timers;
    /**
     * The {@link IHyriRedisProcessor} instance.
     */
    private final IHyriRedisProcessor redisProcessor;

    /**
     * The constructor of the jump manager.
     */
    public LobbyJumpManager() {
        this.taskIds = new HashMap<>();
        this.timers = new HashMap<>();
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }

    /**
     * Get a jump by its start location.
     * @param start The start location.
     * @return The jump with the given start location, or null if not found.
     */
    public LobbyJump getJumpByStart(LobbyLocation start) {
        for (final LobbyJump jump : this.getAllKeysAsValues()) {
            if (LobbyLocation.isEquals(jump.getStart(), start)) {
                return jump;
            }
        }
        return null;
    }

    /**
     * Get a jump by its end location.
     * @param end The end location.
     * @return The jump with the given end location, or null if not found.
     */
    public LobbyJump getJumpByEnd(LobbyLocation end) {
        for (final LobbyJump jump : this.getAllKeysAsValues()) {
            if (LobbyLocation.isEquals(jump.getEnd(), end)) {
                return jump;
            }
        }
        return null;
    }

    /**
     * Get a checkpoint by its location.
     * @param location The checkpoint location.
     * @return The checkpoint with the given location, or null if not found.
     */
    public LobbyCheckpoint getCheckpointByLocation(LobbyLocation location) {
        for (final LobbyJump jump : this.getAllKeysAsValues()) {
            for (final LobbyCheckpoint checkpoint : jump.getCheckpoints()) {
                if (LobbyLocation.isEquals(checkpoint.getLocation(), location)) {
                    return checkpoint;
                }
            }
        }
        return null;
    }

    /**
     * Get the jump with the given name.
     * @param key The jump name.
     * @return The jump with the given name.
     */
    @Override
    public LobbyJump get(String key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + REDIS_KEY + key), LobbyJump.class);
        }
    }

    /**
     * Save the given jump in the database.
     * @param data The jump to save.
     */
    @Override
    public void save(LobbyJump data) {
        this.redisProcessor.process(jedis -> {
            final String key = LobbyAPI.REDIS_KEY + REDIS_KEY + data.getName();
            if (jedis.exists(key)) {
                throw new IllegalArgumentException("A jump with the same name '" + data.getName() + "' already exists");
            }
            jedis.set(key, LobbyAPI.GSON.toJson(data));
        });
    }

    /**
     * Delete the given jump from the database.
     * @param data The jump to delete.
     */
    @Override
    public void delete(LobbyJump data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getName()));
    }

    /**
     * Get all the jumps stored in the database as keys.
     * @return The jumps as keys.
     */
    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + REDIS_KEY + "*");
        }
    }

    /**
     * Get all the jumps stored in the database as values.
     * @return The jumps as values.
     */
    @Override
    public Set<LobbyJump> getAllKeysAsValues() {
        final Set<LobbyJump> jumps = new HashSet<>();
        this.getAllKeys().forEach(key -> jumps.add(this.get(key.split(":")[key.split(":").length - 1])));
        return jumps;
    }

    /**
     * Add a task id in the manager.
     * @param uuid The player uuid.
     * @param taskId The task id.
     */
    public void addTask(UUID uuid, int taskId) {
        this.taskIds.put(uuid, taskId);
    }

    /**
     * Add a timer in the manager.
     * @param uuid The player uuid.
     * @param timer The timer value.
     */
    public void addTimer(UUID uuid, int timer) {
        this.timers.put(uuid, timer);
    }

    /**
     * Get the tasks map.
     * @return The tasks map.
     */
    public Map<UUID, Integer> getTaskIds() {
        return this.taskIds;
    }

    /**
     * Get the timers map.
     * @return The timers map.
     */
    public Map<UUID, Integer> getTimers() {
        return this.timers;
    }
}
