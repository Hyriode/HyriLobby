package fr.hyriode.lobby.api.jump;

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
     * The checkpoint location.
     */
    private final LobbyLocation location;

    /**
     * The constructor of the checkpoint.
     * @param id The checkpoint id.
     * @param location The checkpoint location.
     */
    public LobbyCheckpoint(int id, LobbyLocation location) {
        this.id = id;
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
     * Gets the checkpoint location.
     * @return The checkpoint location.
     */
    public LobbyLocation getLocation() {
        return this.location;
    }
}
