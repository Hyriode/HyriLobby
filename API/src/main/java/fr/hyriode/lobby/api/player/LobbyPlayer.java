package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.chooser.GameMenuTemplate;

import java.util.UUID;

/**
 * Represents a player in the Lobby
 */
public class LobbyPlayer {

    private final UUID uuid;
    private boolean usingCustomMenu;
    private final GameMenuTemplate menu;

    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.usingCustomMenu = false;
        this.menu = new GameMenuTemplate();
    }

    public UUID getUuid() {
        return uuid;
    }
    
    public GameMenuTemplate getMenu() {
        return menu;
    }

    public boolean isUsingCustomMenu() {
        return this.usingCustomMenu;
    }

    public void setUsingCustomMenu(boolean usingCustomMenu) {
        this.usingCustomMenu = usingCustomMenu;
    }
}
