package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class LeaderboardCreatedPacket extends LobbyPacket {

    public LeaderboardCreatedPacket(String name) {
        super(name);
    }
}
