package fr.hyriode.lobby.api.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.function.Consumer;

public abstract class LobbyDataManager<D> {

    private final RedisKey key;
    private final Class<D> dataClass;
    private final IHyriRedisProcessor redisProcessor;

    public LobbyDataManager(RedisKey key, Class<D> dataClass) {
        this.redisProcessor = HyriAPI.get().getRedisProcessor();

        this.key = key;
        this.dataClass = dataClass;
    }

    public D get(String key) {
        return this.redisProcessor.get(jedis ->  LobbyAPI.GSON.fromJson(jedis.get(this.key.getKey() + key), this.dataClass));
    }

    public void save(D data, String key) {
        this.redisProcessor.process(jedis -> jedis.set(this.key.getKey() + key, LobbyAPI.GSON.toJson(data)));
    }

    public void delete(String key) {
        this.redisProcessor.process(jedis -> jedis.del(this.key.getKey() + key));
    }

    public Set<String> getKeys() {
        return this.redisProcessor.get(jedis -> jedis.keys(this.key.getKey() + "*"));
    }

    public void process(Consumer<Jedis> action) {
        this.redisProcessor.process(action);
    }
}
