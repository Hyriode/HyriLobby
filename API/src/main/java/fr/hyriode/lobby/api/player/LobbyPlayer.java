package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.chooser.GameChooserMenu;

import java.util.UUID;

/**
 * Represents a player in the Lobby
 */
public class LobbyPlayer {

    private final UUID uuid;
    private final GameChooserMenu menu;

    /**
     * Constructor of {@link LobbyPlayer}
     * @param uuid The player {@link UUID}
     */
    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.menu = new GameChooserMenu();
    }

    /**
     * Constructor of {@link LobbyPlayer}
     * @param uuid The player {@link UUID}
     * @param menu The player {@link GameChooserMenu}, with game items in custom slots
     */
    public LobbyPlayer(UUID uuid, GameChooserMenu menu) {
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
     * @return The player {@link GameChooserMenu}
     */
    public GameChooserMenu getMenu() {
        return menu;
    }
}
