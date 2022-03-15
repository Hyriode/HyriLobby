package fr.hyriode.lobby.api.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardUpdatedPacket;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LobbyLeaderboardManager implements ILobbyDataManager<LobbyLeaderboard, String> {

    private static final String REDIS_KEY = "leaderboard:";
    private static final String TOP_REDIS_KEY = "leaderboard-top:";

    private final LobbyPacketManager pm;
    private final IHyriRedisProcessor redisProcessor;

    public LobbyLeaderboardManager() {
        this.pm = LobbyAPI.get().getPacketManager();
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }

    public void addToLeaderboard(String leaderboard, String player, int score) {
        this.redisProcessor.process(jedis -> jedis.zadd(LobbyAPI.REDIS_KEY + TOP_REDIS_KEY + leaderboard, score, player));
        this.pm.sendPacket(new LeaderboardUpdatedPacket(leaderboard, LeaderboardUpdatedPacket.Reason.SCORE_UPDATED));
    }

    public Map<Integer, String> getTopLeaderboard(LobbyLeaderboard leaderboard) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            final Map<Integer, String> topPlayers = new HashMap<>();
            jedis.zrevrangeWithScores(LobbyAPI.REDIS_KEY + TOP_REDIS_KEY + leaderboard, 0, leaderboard.getTopRange() - 1)
                    .forEach(tuple -> topPlayers.put((int) tuple.getScore(), tuple.getElement()));
            return topPlayers;
        }
    }

    @Override
    public LobbyLeaderboard get(String key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + REDIS_KEY + key), LobbyLeaderboard.class);
        }
    }

    @Override
    public void save(LobbyLeaderboard data) {
        this.redisProcessor.process(jedis -> {
            final String key = LobbyAPI.REDIS_KEY + REDIS_KEY + data.getName();
            if (jedis.exists(key)) {
                throw new IllegalArgumentException("A leaderboard with the same name '" + data.getName() + "' already exists");
            }
            jedis.set(key, LobbyAPI.GSON.toJson(data));
        });
    }

    @Override
    public void delete(LobbyLeaderboard data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + REDIS_KEY + data.getName()));
    }

    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + REDIS_KEY + "*");
        }
    }

    @Override
    public Set<LobbyLeaderboard> getAllKeysAsValues() {
        final Set<LobbyLeaderboard> jumps = new HashSet<>();
        this.getAllKeys().forEach(key -> jumps.add(this.get(key.split(":")[key.split(":").length - 1])));
        return jumps;
    }
}
