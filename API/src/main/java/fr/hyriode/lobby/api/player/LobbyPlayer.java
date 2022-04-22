package fr.hyriode.lobby.api.player;

import fr.hyriode.api.player.HyriPlayerData;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a player in the lobby.
 */
public class LobbyPlayer extends HyriPlayerData {

    public static final String DATA_KEY = "lobby";

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
     */
    public LobbyPlayer() {
        this.startedJump = null;
        this.lastCheckpoint = -1;
        this.finishedJumps = new ArrayList<>();
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

    public void update(IHyriPlayer account) {
        account.addData(DATA_KEY, this);
    }

    public static LobbyPlayer get(IHyriPlayer account) {
        LobbyPlayer player = account.getData(DATA_KEY, LobbyPlayer.class);

        if (player == null) {
            player = new LobbyPlayer();

            account.addData(DATA_KEY, player);
        }
        return player;
    }

}
