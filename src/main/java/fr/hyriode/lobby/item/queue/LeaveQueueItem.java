package fr.hyriode.lobby.item.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 25/06/2022 at 13:37
 */
public class LeaveQueueItem extends LobbyItem {

    public LeaveQueueItem(HyriLobby plugin) {
        super(plugin, "leave_queue", "item.queue.leave", "item.queue.leave.description", Material.BARRIER);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriParty party = HyriAPI.get().getPartyManager().getPlayerParty(playerId);

        if (party != null && party.isLeader(playerId)) {
            HyriAPI.get().getQueueManager().removePlayerFromQueue(party.getId());
        } else {
            HyriAPI.get().getQueueManager().removePlayerFromQueue(playerId);
        }
    }

}
