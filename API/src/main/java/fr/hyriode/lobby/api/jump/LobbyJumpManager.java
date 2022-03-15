package fr.hyriode.lobby.api.jump;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import redis.clients.jedis.Jedis;

import java.util.*;

public class LobbyJumpManager implements ILobbyDataManager<LobbyJump, String> {

    private static final String REDIS_KEY = "jump:";

    private final Map<UUID, Integer> taskIds;
    private final Map<UUID, Integer> timers;
    private final IHyriRedisProcessor redisProcessor;

    public LobbyJumpManager() {
        this.taskIds = new HashMap<>();
        this.timers = new HashMap<>();
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }


    public LobbyJump createJump(String name, LobbyLocation start, LobbyLocation end, List<LobbyCheckpoint> checkpoints) {
        return new LobbyJump(name, start, end, checkpoints);
    }

    
    public LobbyJump createJump(String name, int startX, int startY, int startZ, int endX, int endY, int endZ, List<LobbyCheckpoint> checkpoints) {
        return new LobbyJump(name, new LobbyLocation(startX, startY, startZ), new LobbyLocation(endX, endY, endZ), checkpoints);
    }

    
    public LobbyJump getJumpByStart(LobbyLocation start) {
        for (final LobbyJump jump : this.getAllKeysAsValues()) {
            if (LobbyLocation.isEquals(jump.getStart(), start)) {
                return jump;
            }
        }
        return null;
    }

    
    public LobbyJump getJumpByEnd(LobbyLocation end) {
        for (final LobbyJump jump : this.getAllKeysAsValues()) {
            if (LobbyLocation.isEquals(jump.getEnd(), end)) {
                return jump;
            }
        }
        return null;
    }

    
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

    @Override
    public LobbyJump get(String key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + REDIS_KEY + key), LobbyJump.class);
        }
    }

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

    @Override
    public void delete(LobbyJump data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getName()));
    }

    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + REDIS_KEY + "*");
        }
    }

    @Override
    public Set<LobbyJump> getAllKeysAsValues() {
        final Set<LobbyJump> jumps = new HashSet<>();
        this.getAllKeys().forEach(key -> jumps.add(this.get(key.split(":")[key.split(":").length - 1])));
        return jumps;
    }

    public void addTask(UUID uuid, int taskId) {
        this.taskIds.put(uuid, taskId);
    }

    public void addTimer(UUID uuid, int timerId) {
        this.timers.put(uuid, timerId);
    }

    public Map<UUID, Integer> getTaskIds() {
        return this.taskIds;
    }

    public Map<UUID, Integer> getTimers() {
        return this.timers;
    }
}
