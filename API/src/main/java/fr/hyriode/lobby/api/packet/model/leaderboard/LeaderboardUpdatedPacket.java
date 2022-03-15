package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class LeaderboardUpdatedPacket extends LobbyPacket {

    private final Reason reason;

    public LeaderboardUpdatedPacket(String name, Reason reason) {
        super(name);

        this.reason = reason;
    }

    public Reason getReason() {
        return this.reason;
    }

    public enum Reason {
        MOVED,
        SCORE_UPDATED
    }
}
