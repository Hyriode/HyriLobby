package fr.hyriode.lobby.api.player;

import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.redis.ILobbyData;

import java.util.UUID;

public class LobbyPlayer implements ILobbyData {

    private final UUID uuid;
    private boolean usingCustomMenu;

    private String startedJump;
    private LobbyCheckpoint lastCheckpoint;

    public LobbyPlayer(UUID uuid) {
        this.uuid = uuid;
        this.usingCustomMenu = false;

        this.startedJump = null;
        this.lastCheckpoint = null;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public boolean isUsingCustomMenu() {
        return this.usingCustomMenu;
    }

    public void setUsingCustomMenu(boolean usingCustomMenu) {
        this.usingCustomMenu = usingCustomMenu;
    }
    
    public String getStartedJump() {
        return this.startedJump;
    }
    
    public void setStartedJump(String startedJump) {
        this.startedJump = startedJump;
    }
    
    public LobbyCheckpoint getLastCheckpoint() {
        return this.lastCheckpoint;
    }
    
    public void setLastCheckpoint(LobbyCheckpoint lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }
}
