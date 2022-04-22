package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.redis.LobbyDataManager;
import fr.hyriode.lobby.api.redis.RedisKey;

/**
 * Represents the manager for players in the lobby.
 */
public class LobbyPlayerManager extends LobbyDataManager<LobbyPlayer> {

    /**
     * The constructor of the player manager.
     */
    public LobbyPlayerManager() {
        super(RedisKey.PLAYERS.getKey(), LobbyPlayer.class);
    }
}
