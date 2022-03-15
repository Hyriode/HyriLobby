package fr.hyriode.lobby.api.jump;

import fr.hyriode.lobby.api.utils.LobbyLocation;

public class LobbyCheckpoint {

    private final int id;
    private final LobbyLocation location;

    public LobbyCheckpoint(int id, LobbyLocation location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public LobbyLocation getLocation() {
        return this.location;
    }
}
