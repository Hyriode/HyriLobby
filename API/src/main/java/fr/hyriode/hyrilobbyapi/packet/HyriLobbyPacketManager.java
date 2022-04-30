package fr.hyriode.hyrilobbyapi.packet;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.pubsub.IHyriPubSub;
import fr.hyriode.hyrilobbyapi.HyriLobbyAPI;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 00:45
 */
public class HyriLobbyPacketManager {

    private final IHyriPubSub pubSub;

    public HyriLobbyPacketManager() {
        this.pubSub = HyriAPI.get().getPubSub();
    }

    public void sendPacket(HyriLobbyPacket packet) {
        this.pubSub.send(HyriLobbyAPI.LOBBY_DATA_KEY, packet);
    }
}
