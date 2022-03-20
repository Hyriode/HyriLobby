package fr.hyriode.lobby.api.jump;

import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.utils.LobbyLocation;

/**
 * Represents a checkpoint in a jump.
 */
public class LobbyCheckpoint {

    /**
     * The checkpoint id.
     */
    private final int id;
    /**
     * The jump name.
     */
    private final String jumpName;
    /**
     * The checkpoint location.
     */
    private final LobbyLocation location;

    /**
     * The constructor of the checkpoint.
     * @param id The checkpoint id.
     * @param location The checkpoint location.
     */
    public LobbyCheckpoint(int id, String jumpName, LobbyLocation location) {
        this.id = id;
        this.jumpName = jumpName;
        this.location = location;
    }

    /**
     * Gets the checkpoint id.
     * @return The checkpoint id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the jump name.
     * @return The jump name.
     */
    public String getJumpName() {
        return this.jumpName;
    }

    /**
     * Gets the checkpoint location.
     * @return The checkpoint location.
     */
    public LobbyLocation getLocation() {
        return this.location;
    }

    @Override
    public String toString() {
        return LobbyAPI.GSON.toJson(this);
    }
}
