package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class LeaderboardDeletedPacket extends LobbyPacket {

    public LeaderboardDeletedPacket(String name) {
        super(name);
    }
}
