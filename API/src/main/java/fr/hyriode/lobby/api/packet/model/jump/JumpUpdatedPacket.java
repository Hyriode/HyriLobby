package fr.hyriode.lobby.api.packet.model.jump;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class JumpUpdatedPacket extends LobbyPacket {

    private final Reason reason;

    public JumpUpdatedPacket(String name, Reason reason) {
        super(name);

        this.reason = reason;
    }

    public Reason getReason() {
        return this.reason;
    }

    public enum Reason {
        START_MOVED,
        END_MOVED,
        NAME_CHANGED
    }
}
