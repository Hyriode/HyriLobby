package fr.hyriode.lobby.api.packet;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.pubsub.IHyriPubSub;

/**
 * Represents a packet manager for the lobby.
 */
public class LobbyPacketManager {

    /**
     * The channel used by the lobby on PubSub.
     */
    public static final String CHANNEL = "lobby";

    /**
     * The {@link IHyriPubSub} instance.
     */
    private final IHyriPubSub pubSub;

    /**
     * The constructor of the packet manager.
     */
    public LobbyPacketManager() {
        this.pubSub = HyriAPI.get().getPubSub();
    }

    /**
     * Publishes a packet on the lobby channel.
     * @param packet The packet to publish.
     */
    public void sendPacket(LobbyPacket packet) {
        this.pubSub.send(CHANNEL, packet);
    }
}
