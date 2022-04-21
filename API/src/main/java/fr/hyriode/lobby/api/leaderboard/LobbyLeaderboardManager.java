package fr.hyriode.lobby.api.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.redis.IHyriRedisProcessor;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.packet.LobbyPacketManager;
import fr.hyriode.lobby.api.packet.model.leaderboard.LeaderboardUpdatedPacket;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.redis.LobbyDataManager;
import fr.hyriode.lobby.api.redis.RedisKey;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ZAddParams;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Represents the manager for the leaderboards in the lobby.
 */
public class LobbyLeaderboardManager extends LobbyDataManager<LobbyLeaderboard> {

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
        super(RedisKey.LEADERBOARDS, LobbyLeaderboard.class);
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
            System.out.println("Old score: " + jedis.zscore(RedisKey.LEADERBOARDS_RANKS.getKey() + leaderboard.toLowerCase(), player));
            jedis.zadd(RedisKey.LEADERBOARDS_RANKS.getKey() + leaderboard.toLowerCase(), score, player, ZAddParams.zAddParams().lt());
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
        this.redisProcessor.process(jedis -> jedis.zrem(RedisKey.LEADERBOARDS_RANKS.getKey() + leaderboard.toLowerCase(), player.getName()));
    }

    /**
     * Get the top players of a leaderboard.
     * @param leaderboard The leaderboard to get the top players of.
     * @return The top players of the leaderboard, with {@link Integer} representing the score and {@link String} representing the player.
     */
    public Map<Integer, String> getTopLeaderboard(LobbyLeaderboard leaderboard) {
        try (final Jedis jedis = HyriAPI.get().getRedisResource()) {
            final Map<Integer, String> topPlayers = new HashMap<>();
            jedis.zrangeWithScores(RedisKey.LEADERBOARDS_RANKS.getKey() + leaderboard.getName().toLowerCase(), 0, leaderboard.getTopRange() - 1)
                    .forEach(tuple -> topPlayers.put((int) tuple.getScore(), tuple.getElement()));
            return topPlayers;
        }
    }
}
