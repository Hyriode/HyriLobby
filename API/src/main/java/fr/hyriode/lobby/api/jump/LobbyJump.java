package fr.hyriode.lobby.api.jump;

import fr.hyriode.lobby.api.redis.ILobbyData;
import fr.hyriode.lobby.api.utils.LobbyLocation;

import java.util.List;

/**
 * Represents a jump in the lobby.
 */
public class LobbyJump implements ILobbyData {

    /**
     * The name of the jump.
     */
    private final String name;

    /**
     * The start location of the jump.
     */
    private LobbyLocation start;
    /**
     * The end location of the jump.
     */
    private LobbyLocation end;

    /**
     * All the checkpoints of the jump.
     */
    private final List<LobbyCheckpoint> checkpoints;

    /**
     * The constructor of the jump.
     * @param name The name of the jump.
     * @param start The start location of the jump.
     * @param end The end location of the jump.
     * @param checkpoints All the checkpoints of the jump.
     */
    public LobbyJump(String name, LobbyLocation start, LobbyLocation end, List<LobbyCheckpoint> checkpoints) {
        this.name = name;

        this.start = start;
        this.end = end;

        this.checkpoints = checkpoints;
    }

    /**
     * Get the name of the jump.
     * @return The name of the jump.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the start location of the jump.
     * @return The start location of the jump.
     */
    public LobbyLocation getStart() {
        return this.start;
    }

    /**
     * Set the start location of the jump.
     * @param start The start location of the jump.
     */
    public void setStart(LobbyLocation start) {
        this.start = start;
    }

    /**
     * Get the end location of the jump.
     * @return The end location of the jump.
     */
    public LobbyLocation getEnd() {
        return this.end;
    }

    /**
     * Set the end location of the jump.
     * @param end The end location of the jump.
     */
    public void setEnd(LobbyLocation end) {
        this.end = end;
    }

    /**
     * Get all the checkpoints of the jump.
     * @return All the checkpoints of the jump.
     */
    public List<LobbyCheckpoint> getCheckpoints() {
        return this.checkpoints;
    }
}
