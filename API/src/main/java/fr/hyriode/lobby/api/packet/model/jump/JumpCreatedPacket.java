package fr.hyriode.lobby.api.packet.model.jump;

import fr.hyriode.lobby.api.packet.LobbyPacket;

public class JumpCreatedPacket extends LobbyPacket {

    public JumpCreatedPacket(String name) {
        super(name);
    }
}
