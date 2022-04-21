package fr.hyriode.lobby.api.redis;

public enum RedisKey {

    JUMP("jump"),
    PLAYERS("player"),
    LEADERBOARDS("leaderboard"),
    LEADERBOARDS_RANKS("leaderboard:" + "ranks");

    private final String key;

    RedisKey(String key) {
        this.key = "lobby" + ":" + key + ":";
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
