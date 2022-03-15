package fr.hyriode.lobby.api.jump;

import fr.hyriode.lobby.api.redis.ILobbyData;
import fr.hyriode.lobby.api.utils.LobbyLocation;

import java.util.List;

public class LobbyJump implements ILobbyData {

    private final String name;

    private LobbyLocation start;
    private LobbyLocation end;

    private final List<LobbyCheckpoint> checkpoints;

    public LobbyJump(String name, LobbyLocation start, LobbyLocation end, List<LobbyCheckpoint> checkpoints) {
        this.name = name;

        this.start = start;
        this.end = end;

        this.checkpoints = checkpoints;
    }

    public String getName() {
        return this.name;
    }

    public LobbyLocation getStart() {
        return this.start;
    }

    public LobbyLocation getEnd() {
        return this.end;
    }

    public void setStart(LobbyLocation start) {
        this.start = start;
    }

    public void setEnd(LobbyLocation end) {
        this.end = end;
    }

    public List<LobbyCheckpoint> getCheckpoints() {
        return this.checkpoints;
    }
}
