package fr.hyriode.lobby.api.packet;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Represents a packet sent with data about the lobby.
 */
public abstract class LobbyPacket extends HyriPacket {

    /**
     * The name of the data sent in the packet.
     */
    private final String name;

    /**
     * The constructor of the packet.
     * @param name The name of the data sent in the packet.
     */
    public LobbyPacket(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the data sent in the packet.
     * @return The name of the data sent in the packet, which can be used in Redis to get it.
     */
    public String getName() {
        return this.name;
    }
}
