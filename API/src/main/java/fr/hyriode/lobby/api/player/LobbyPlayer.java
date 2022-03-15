package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;
import fr.hyriode.lobby.api.redis.ILobbyData;

import java.util.UUID;

/**
 * Represents a player in the lobby.
 */
public class LobbyPlayer implements ILobbyData {

    /**
     * The player's UUID.
     */
    private final UUID uuid;
    /**
     * <code>true</code> if the player is using a custom mini-games chooser menu, <code>false</code> otherwise.
     */
    private boolean usingCustomMenu;

    /**
     * The name of the {@link LobbyJump} started, if any.
     */
    private String startedJump;
    /**
     * The last {@link LobbyCheckpoint} reached, if any.
     */
    private LobbyCheckpoint lastCheckpoint;

    /**
     * The constructor of the player.
     * @param uuid The player's UUID.
     */
    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.usingCustomMenu = false;

        this.startedJump = null;
        this.lastCheckpoint = null;
    }

    /**
     * Gets the player's UUID.
     * @return The player's UUID.
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Checks if the player is using a custom mini-games chooser menu.
     * @return <code>true</code> if the player is using a custom mini-games chooser menu, <code>false</code> otherwise.
     */
    public boolean isUsingCustomMenu() {
        return this.usingCustomMenu;
    }

    /**
     * Sets if the player is using a custom mini-games chooser menu.
     * @param usingCustomMenu <code>true</code> if the player is using a custom mini-games chooser menu, <code>false</code> otherwise.
     */
    public void setUsingCustomMenu(boolean usingCustomMenu) {
        this.usingCustomMenu = usingCustomMenu;
    }

    /**
     * Gets the name of the {@link LobbyJump} started, if any.
     * @return The name of the {@link LobbyJump} started, if any.
     */
    public String getStartedJump() {
        return this.startedJump;
    }

    /**
     * Sets the name of the {@link LobbyJump} started.
     * @param startedJump The name of the {@link LobbyJump} started.
     */
    public void setStartedJump(String startedJump) {
        this.startedJump = startedJump;
    }

    /**
     * Gets the last {@link LobbyCheckpoint} reached, if any.
     * @return The last {@link LobbyCheckpoint} reached, if any.
     */
    public LobbyCheckpoint getLastCheckpoint() {
        return this.lastCheckpoint;
    }

    /**
     * Sets the last {@link LobbyCheckpoint} reached.
     * @param lastCheckpoint The last {@link LobbyCheckpoint} reached.
     */
    public void setLastCheckpoint(LobbyCheckpoint lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }
}
