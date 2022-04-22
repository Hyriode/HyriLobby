package fr.hyriode.lobby.api.jump;

import fr.hyriode.lobby.api.redis.LobbyDataManager;
import fr.hyriode.lobby.api.redis.RedisKey;
import fr.hyriode.lobby.api.utils.LobbyLocation;

import java.util.*;

/**
 * Represents the manager of the jump system.
 */
public class LobbyJumpManager extends LobbyDataManager<LobbyJump> {

    /**
     * A map containing timer tasks ids, where {@link UUID} is the player's UUID and {@link Integer} is the task id.
     */
    private final Map<UUID, Integer> taskIds;
    /**
     * A map containing the jump timers, where {@link UUID} is the player's UUID and {@link Integer} is the jump timer.
     */
    private final Map<UUID, Integer> timers;

    /**
     * The constructor of the jump manager.
     */
    public LobbyJumpManager() {
        super(RedisKey.JUMP.getKey(), LobbyJump.class);

        this.taskIds = new HashMap<>();
        this.timers = new HashMap<>();
    }

    /**
     * Get a jump by its start location.
     * @param start The start location.
     * @return The jump with the given start location, or null if not found.
     */
    public LobbyJump getJumpByStart(LobbyLocation start) {
        for (final LobbyJump jump : this.getValues()) {
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
        for (final LobbyJump jump : this.getValues()) {
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
        for (final LobbyJump jump : this.getValues()) {
            for (final LobbyCheckpoint checkpoint : jump.getCheckpoints()) {
                if (LobbyLocation.isEquals(checkpoint.getLocation(), location)) {
                    return checkpoint;
                }
            }
        }
        return null;
    }

    /**
     * Get all the jumps stored in the database as values.
     * @return The jumps as values.
     */
    public Set<LobbyJump> getValues() {
        final Set<LobbyJump> jumps = new HashSet<>();
        this.getKeys().forEach(key -> jumps.add(this.get(key.split(":")[key.split(":").length - 1])));
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
     * Increment the timer of the given player.
     * @param uuid The player uuid.
     * @param seconds The seconds to increment.
     */
    public void increaseTimer(UUID uuid, int seconds) {
        this.timers.put(uuid, this.timers.get(uuid) + seconds);
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
