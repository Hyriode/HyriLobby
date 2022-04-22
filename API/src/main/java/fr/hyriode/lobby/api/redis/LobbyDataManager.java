package fr.hyriode.lobby.api.redis;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public abstract class LobbyDataManager<D> {

    private final String key;
    private final Class<D> dataClass;
    private final IHyriRedisProcessor redisProcessor;

    public LobbyDataManager(String key, Class<D> dataClass) {
        this.redisProcessor = HyriAPI.get().getRedisProcessor();

        this.key = key;
        this.dataClass = dataClass;
    }

    public D get(String key) {
        return this.redisProcessor.get(jedis -> LobbyAPI.GSON.fromJson(jedis.get(this.key + key), this.dataClass));
    }

    public void save(D data, String key) {
        this.redisProcessor.process(jedis -> jedis.set(this.key + key, LobbyAPI.GSON.toJson(data)));
    }

    public void delete(String key) {
        this.redisProcessor.process(jedis -> jedis.del(this.key + key));
    }

    public Set<String> getKeys() {
        return this.redisProcessor.get(jedis -> jedis.keys(this.key + "*"));
    }

    public Set<D> getValues() {
        final Set<D> values = new HashSet<>();

        this.getKeys().forEach(key -> values.add(this.get(key.split(":")[key.split(":").length - 1])));

        return values;
    }

    public void process(Consumer<Jedis> action) {
        this.redisProcessor.process(action);
    }
}
