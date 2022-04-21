package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player in the lobby.
 */
public class LobbyPlayer {

    /**
     * The player's UUID.
     */
    private final UUID uuid;
    /**
     * The player's name.
     */
    private final String name;

    /**
     * The name of the {@link LobbyJump} started, if any.
     */
    private String startedJump;
    /**
     * The last {@link LobbyCheckpoint} id reached, if any.
     */
    private int lastCheckpoint;

    /**
     * All the jumps name completed by the player.
     */
    private final List<String> finishedJumps;

    /**
     * The constructor of the player.
     * @param uuid The player's UUID.
     */
    public LobbyPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.startedJump = null;
        this.lastCheckpoint = -1;
        this.finishedJumps = new ArrayList<>();
    }

    /**
     * Gets the player's UUID.
     * @return The player's UUID.
     */
    public UUID getUniqueId() {
        return this.uuid;
    }

    /**
     * Gets the player's name.
     * @return The player's name.
     */
    public String getName() {
        return this.name;
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
     * Gets the last {@link LobbyCheckpoint} id reached, if any.
     * @return The last {@link LobbyCheckpoint} id reached, if any.
     */
    public int getLastCheckpoint() {
        return this.lastCheckpoint;
    }

    /**
     * Sets the last {@link LobbyCheckpoint} id reached.
     * @param lastCheckpoint The last {@link LobbyCheckpoint} id reached.
     */
    public void setLastCheckpoint(int lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    /**
     * Gets all the jumps name completed by the player.
     * @return All the jumps name completed by the player.
     */
    public List<String> getFinishedJumps() {
        return this.finishedJumps;
    }

    @Override
    public String toString() {
        return LobbyAPI.GSON.toJson(this);
    }
}
