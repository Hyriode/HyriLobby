package fr.hyriode.lobby.config;

import fr.hyriode.hystia.api.config.IConfig;
import fr.hyriode.lobby.api.utils.LobbyLocation;

public class LobbyConfig implements IConfig {

    private final LobbyLocation location;

    public LobbyConfig(LobbyLocation location) {
        this.location = location;
    }

    public LobbyLocation getLocation() {
        return this.location;
    }

}
