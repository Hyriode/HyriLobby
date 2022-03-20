package fr.hyriode.lobby.api.packet.model.jump;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class JumpDeletedPacket extends LobbyPacket {

    /**
     * The constructor of the packet.
     * @param name The name of the data sent in the packet.
     */
    public JumpDeletedPacket(String name) {
        super(name);
    }
}
