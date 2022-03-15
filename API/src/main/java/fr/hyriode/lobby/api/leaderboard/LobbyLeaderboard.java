package fr.hyriode.lobby.api.leaderboard;

import fr.hyriode.lobby.api.redis.ILobbyData;
import fr.hyriode.lobby.api.utils.LobbyLocation;

public class LobbyLeaderboard implements ILobbyData {

    private final String name;
    private int topRange;
    private LobbyLocation location;

    public LobbyLeaderboard(String name, int topRange, LobbyLocation location) {
        this.name = name;
        this.topRange = topRange;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public int getTopRange() {
        return this.topRange;
    }

    public LobbyLocation getLocation() {
        return this.location;
    }

    public void setTopRange(int topRange) {
        this.topRange = topRange;
    }

    public void setLocation(LobbyLocation location) {
        this.location = location;
    }
}
