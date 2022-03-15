package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

/**
 * A packet sent when a leaderboard has been created.
 */
public class LeaderboardCreatedPacket extends LobbyPacket {

    /**
     * The constructor of the packet.
     * @param name The name of the leaderboard.
     */
    public LeaderboardCreatedPacket(String name) {
        super(name);
    }
}
