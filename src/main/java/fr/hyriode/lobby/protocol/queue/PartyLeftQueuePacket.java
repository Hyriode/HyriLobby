package fr.hyriode.lobby.protocol.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.party.IHyriParty;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/06/2022 at 17:52
 */
public class PartyLeftQueuePacket extends HyriPacket {

    private final UUID partyId;

    public PartyLeftQueuePacket(UUID partyId) {
        this.partyId = partyId;
    }

    public UUID getPartyId() {
        return this.partyId;
    }

    public IHyriParty getParty() {
        return HyriAPI.get().getPartyManager().getParty(this.partyId);
    }

}
