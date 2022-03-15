package fr.hyriode.lobby.api.packet.model.jump;

import fr.hyriode.lobby.api.packet.LobbyPacket;

/**
 * A packet sent when a jump has been created.
 */
public class JumpCreatedPacket extends LobbyPacket {

    /**
     * The constructor of the packet.
     * @param name The name of the jump.
     */
    public JumpCreatedPacket(String name) {
        super(name);
    }
}
