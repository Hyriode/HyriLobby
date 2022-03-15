package fr.hyriode.lobby.api.packet.model.jump;

import fr.hyriode.lobby.api.packet.LobbyPacket;

/**
 * A packet sent when a jump has been updated.
 */
public class JumpUpdatedPacket extends LobbyPacket {

    /**
     * The update reason.
     */
    private final Reason reason;

    /**
     * The constructor of the packet.
     * @param name The name of the jump.
     * @param reason The update reason.
     */
    public JumpUpdatedPacket(String name, Reason reason) {
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
         * The start location of the jump has changed.
         */
        START_MOVED,
        /**
         * The end location of the jump has changed.
         */
        END_MOVED,
        /**
         * The name of the jump has changed.
         */
        NAME_CHANGED
    }
}
