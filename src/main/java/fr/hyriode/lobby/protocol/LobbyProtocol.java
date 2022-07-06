package fr.hyriode.lobby.protocol;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.lobby.protocol.queue.PartyLeftQueuePacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/06/2022 at 17:52
 */
public class LobbyProtocol {

    public static final String CHANNEL = "lobby";

    public void partyLeftQueue(UUID partyId) {
        HyriAPI.get().getPubSub().send(CHANNEL, new PartyLeftQueuePacket(partyId));
    }

}
