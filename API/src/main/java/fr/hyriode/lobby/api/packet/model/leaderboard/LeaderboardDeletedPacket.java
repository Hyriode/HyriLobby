package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

/**
 * Packet sent when a leaderboard is deleted.
 */
public class LeaderboardDeletedPacket extends LobbyPacket {

    /**
     * The constructor of the packet.
     * @param name The name of the leaderboard.
     */
    public LeaderboardDeletedPacket(String name) {
        super(name);
    }
}
