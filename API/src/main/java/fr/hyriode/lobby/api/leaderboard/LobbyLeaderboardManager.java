package fr.hyriode.lobby.api.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardUpdatedPacket;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.redis.ILobbyDataManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZAddParams;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents the manager for the leaderboards in the lobby.
 */
public class LobbyLeaderboardManager implements ILobbyDataManager<LobbyLeaderboard, String> {

    /**
     * The base key for storing leaderboards data in Redis.
     */
    private static final String LEADERBOARD_REDIS_KEY = "leaderboards:" + "leaderboard:";
    /**
     * The base key for storing rankings data in Redis.
     */
    private static final String RANKINGS_REDIS_KEY = "leaderboards:" + "rankings:";

    /**
     * The {@link LobbyPacketManager} instance.
     */
    private final Supplier<LobbyPacketManager> pm;
    /**
     * The {@link IHyriRedisProcessor} instance.
     */
    private final IHyriRedisProcessor redisProcessor;

    /**
     * The constructor of the leaderboard manager.
     */
    public LobbyLeaderboardManager() {
        this.pm = () -> LobbyAPI.get().getPacketManager();
        this.redisProcessor = HyriAPI.get().getRedisProcessor();
    }

    /**
     * Add a player and his score to the leaderboard. <br>
     * If the player is already in the leaderboard, we add only if the new score is better (lower) than the old one.
     * @param leaderboard The leaderboard to add the player to.
     * @param player The player to add.
     * @param score The score of the player.
     */
    public void addToLeaderboard(String leaderboard, LobbyPlayer player, int score) {
        this.addToLeaderboard(leaderboard, player.getName(), score, false);
    }

    /**
     * Add a player and his score to the leaderboard. <br>
     * If the player is already in the leaderboard, we add only if the new score is better (lower) than the old one.
     * @param leaderboard The leaderboard to add the player to.
     * @param player The player to add.
     * @param score The score of the player.
     * @param silent <code>false</code> if the packet should be sent, <code>true</code> otherwise.
     */
    public void addToLeaderboard(String leaderboard, String player, int score, boolean silent) {
        this.redisProcessor.process(jedis -> {
            System.out.println("Adding " + player + " to " + leaderboard + " with score " + score);
            //TODO: always null
            System.out.println("Old score: " + jedis.zscore(LobbyAPI.REDIS_KEY + RANKINGS_REDIS_KEY + leaderboard.toLowerCase(), player));
            jedis.zadd(LobbyAPI.REDIS_KEY + RANKINGS_REDIS_KEY + leaderboard.toLowerCase(), score, player, ZAddParams.zAddParams().lt());
            if (!silent) {
                this.pm.get().sendPacket(new LeaderboardUpdatedPacket(leaderboard, LeaderboardUpdatedPacket.Reason.SCORE_UPDATED));
            }
        });
    }

    /**
     * Remove a player score from the leaderboard.
     * @param leaderboard The leaderboard to remove the player from.
     * @param player The player to remove.
     */
    public void removeFromLeaderboard(String leaderboard, LobbyPlayer player) {
        this.redisProcessor.process(jedis -> jedis.zrem(LobbyAPI.REDIS_KEY + RANKINGS_REDIS_KEY + leaderboard.toLowerCase(), player.getName()));
    }

    /**
     * Get the top players of a leaderboard.
     * @param leaderboard The leaderboard to get the top players of.
     * @return The top players of the leaderboard, with {@link Integer} representing the score and {@link String} representing the player.
     */
    public Map<Integer, String> getTopLeaderboard(LobbyLeaderboard leaderboard) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            final Map<Integer, String> topPlayers = new HashMap<>();
            jedis.zrangeWithScores(LobbyAPI.REDIS_KEY + RANKINGS_REDIS_KEY + leaderboard.getName().toLowerCase(), 0, leaderboard.getTopRange() - 1)
                    .forEach(tuple -> topPlayers.put((int) tuple.getScore(), tuple.getElement()));
            return topPlayers;
        }
    }

    /**
     * Get the leaderboard with the given name.
     * @param key The name of the leaderboard.
     * @return The leaderboard with the given name.
     */
    @Override
    public LobbyLeaderboard get(String key) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return LobbyAPI.GSON.fromJson(jedis.get(LobbyAPI.REDIS_KEY + LEADERBOARD_REDIS_KEY + key.toLowerCase()), LobbyLeaderboard.class);
        }
    }

    /**
     * Save the given leaderboard in the database.
     * @param data The leaderboard to save.
     */
    @Override
    public void save(LobbyLeaderboard data) {
        this.redisProcessor.process(jedis -> jedis.set(LobbyAPI.REDIS_KEY + LEADERBOARD_REDIS_KEY + data.getName().toLowerCase(), LobbyAPI.GSON.toJson(data)));
    }

    /**
     * Delete the given leaderboard from the database.
     * @param data The leaderboard to delete.
     */
    @Override
    public void delete(LobbyLeaderboard data) {
        this.redisProcessor.process(jedis -> jedis.del(LobbyAPI.REDIS_KEY + LEADERBOARD_REDIS_KEY + data.getName().toLowerCase()));
    }

    /**
     * Get all the leaderboards in the database as keys.
     * @return The leaderboards as keys.
     */
    @Override
    public Set<String> getAllKeys() {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            return jedis.keys(LobbyAPI.REDIS_KEY + LEADERBOARD_REDIS_KEY + "*");
        }
    }

    /**
     * Get all the leaderboards in the database as values.
     * @return The leaderboards as values.
     */
    @Override
    public Set<LobbyLeaderboard> getAllKeysAsValues() {
        final Set<LobbyLeaderboard> jumps = new HashSet<>();
        this.getAllKeys().forEach(key -> jumps.add(this.get(key.split(":")[key.split(":").length - 1])));
        return jumps;
    }
}
