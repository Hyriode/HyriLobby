package fr.hyriode.lobby.api.player;

import java.util.UUID;

/**
 * Represents a player in the Lobby
 */
public class LobbyPlayer {

    private final UUID uuid;
    private final Games menu;

    /**
     * Constructor of {@link LobbyPlayer}
     * @param uuid The player {@link UUID}
     */
    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.menu = new Games();
    }

    /**
     * Constructor of {@link LobbyPlayer}
     * @param uuid The player {@link UUID}
     * @param menu The player {@link Games}, with game items in custom slots
     */
    public LobbyPlayer(UUID uuid, Games menu) {
        this.uuid = uuid;
        this.menu = menu;
    }

    /**
     * Get the player {@link UUID}
     * @return The player {@link UUID}
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Get the player game menu
     * @return The player {@link Games}
     */
    public Games getMenu() {
        return menu;
    }
}
