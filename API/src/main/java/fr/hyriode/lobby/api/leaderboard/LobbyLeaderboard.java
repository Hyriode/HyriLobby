package fr.hyriode.lobby.api.leaderboard;

import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.redis.ILobbyData;
import fr.hyriode.lobby.api.utils.LobbyLocation;

/**
 * Represents a leaderboard in the lobby.
 */
public class LobbyLeaderboard implements ILobbyData {

    /**
     * The name of the leaderboard.
     */
    private final String name;
    /**
     * The top range of the leaderboard. If the range is 10, the top 10 players will be displayed.
     */
    private int topRange;
    /**
     * The location of the leaderboard.
     */
    private LobbyLocation location;

    /**
     * The constructor of the leaderboard.
     * @param name The name of the leaderboard.
     * @param topRange The top range of the leaderboard.
     * @param location The location of the leaderboard.
     */
    public LobbyLeaderboard(String name, int topRange, LobbyLocation location) {
        this.name = name;
        this.topRange = topRange;
        this.location = location;
    }

    /**
     * Gets the name of the leaderboard.
     * @return The name of the leaderboard.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the top range of the leaderboard.
     * @return The top range of the leaderboard.
     */
    public int getTopRange() {
        return this.topRange;
    }

    /**
     * Sets the top range of the leaderboard.
     * @param topRange The top range of the leaderboard.
     */
    public void setTopRange(int topRange) {
        this.topRange = topRange;
    }

    /**
     * Gets the location of the leaderboard.
     * @return The location of the leaderboard.
     */
    public LobbyLocation getLocation() {
        return this.location;
    }

    /**
     * Sets the location of the leaderboard.
     * @param location The location of the leaderboard.
     */
    public void setLocation(LobbyLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return LobbyAPI.GSON.toJson(this);
    }
}
