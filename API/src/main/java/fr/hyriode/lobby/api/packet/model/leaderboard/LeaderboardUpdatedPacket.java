package fr.hyriode.lobby.api.packet.model.leaderboard;

import fr.hyriode.lobby.api.packet.LobbyPacket;

/**
 * A packet sent when a leaderboard is updated.
 */
public class LeaderboardUpdatedPacket extends LobbyPacket {

    /**
     * The update reason.
     */
    private final Reason reason;

    /**
     * The constructor of the packet
     * @param name The name of the leaderboard.
     * @param reason The update reason.
     */
    public LeaderboardUpdatedPacket(String name, Reason reason) {
        super(name);

        this.reason = reason;
    }

    /**
     * Gets the update reason.
     * @return The update reason.
     */
    public Reason getReason() {
        return this.reason;
    }

    /**
     * The update reason.
     */
    public enum Reason {
        /**
         * The leaderboard location has changed.
         */
        MOVED,
        /**
         * The leaderboard score has been updated, usually a new data has been stored.
         */
        SCORE_UPDATED
    }
}
