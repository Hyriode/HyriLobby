package fr.hyriode.lobby.api.packet;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.pubsub.IHyriPubSub;

public class LobbyPacketManager {

    public static final String CHANNEL = "lobby";

    private final IHyriPubSub pubSub;

    public LobbyPacketManager() {
        this.pubSub = HyriAPI.get().getPubSub();
    }

    public void sendPacket(LobbyPacket packet) {
        this.pubSub.send(CHANNEL, packet);
    }
}
