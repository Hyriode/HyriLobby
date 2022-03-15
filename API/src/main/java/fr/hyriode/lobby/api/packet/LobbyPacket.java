package fr.hyriode.lobby.api.packet;

import fr.hyriode.api.packet.HyriPacket;

public abstract class LobbyPacket extends HyriPacket {

    private final String name;

    public LobbyPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
