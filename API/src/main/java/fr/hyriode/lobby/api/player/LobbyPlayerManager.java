package fr.hyriode.lobby.api.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LobbyPlayerManager implements ILobbyDataManager<LobbyPlayer, UUID> {

    private static final String REDIS_KEY = "player:";

    private final IHyriRedisProcessor redisProcessor;

    public LobbyPlayerManager() {
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }

    @Override
    public LobbyPlayer get(UUID key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + REDIS_KEY + key), LobbyPlayer.class);
        }
    }

    @Override
    public void save(LobbyPlayer data) {
        this.redisProcessor.process(jedis -> jedis.set(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getUniqueId(), LobbyAPI.GSON.toJson(data)));
    }

    @Override
    public void delete(LobbyPlayer data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getUniqueId()));
    }

    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + REDIS_KEY + "*");
        }
    }

    @Override
    public Set<LobbyPlayer> getAllKeysAsValues() {
        final Set<LobbyPlayer> players = new HashSet<>();
        this.getAllKeys().forEach(key -> players.add(this.get(UUID.fromString(key.split(":")[key.split(":").length - 1]))));
        return players;
    }
}
