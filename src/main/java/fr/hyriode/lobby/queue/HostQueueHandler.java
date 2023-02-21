package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.event.PlayerLeaveQueueEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.GameSelectorItem;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 24/06/2022 at 14:45
 */
public class HostQueueHandler implements IHyriQueueHandler, Listener {

    private final Map<UUID, ActionBar> bars;

    private final HyriLobby plugin;
    private final IHyrame hyrame;

    public HostQueueHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.hyrame = this.plugin.getHyrame();
        this.bars = new HashMap<>();

        HyriAPI.get().getNetworkManager().getEventBus().register(this);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @HyriEventHandler
    public void onPartyLeave(HyriPartyLeaveEvent event) {
        this.removeActionBar(event.getMember());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.removeActionBar(event.getPlayer().getUniqueId());
    }


    @Override
    public void onLeaveQueue(PlayerLeaveQueueEvent event) {
        final IHyriPlayerSession account = IHyriPlayerSession.get(event.getPlayerId());

        final IHyriQueue group = event.getQueue();

        if (group == null) {
            return;
        }

        final UUID leaderId = IHyriParty.get(account.getParty()).getLeader();

        final Player player = Bukkit.getPlayer(leaderId);

        if (player != null) {
            this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

            this.removeActionBar(leaderId);
        }

    }


    private ActionBar createActionBar(Player player, IHyriQueue info) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(info.getServer());
        final int groups = info.getTotalPlayers().size();

        if (groups == -1 || server == null) {
            return null;
        }

        final HostData hostData = HyriAPI.get().getHostManager().getHostData(server);
        final String formattedGroups = String.valueOf(groups);
        final ActionBar actionBar = new ActionBar(LobbyMessage.QUEUE_HOST_DISPLAY_BAR.asString(player)
                .replace("%host%", hostData.getName())
                .replace("%position%", formattedGroups)
                .replace("%total%", formattedGroups));
        final ActionBar oldBar = this.bars.put(player.getUniqueId(), actionBar);

        if (oldBar != null) {
            oldBar.remove();
        }

        actionBar.sendPermanent(this.plugin, player);

        return actionBar;
    }

    private void removeActionBar(UUID playerId) {
        if (playerId == null) {
            return;
        }

        final ActionBar actionBar = this.bars.remove(playerId);

        if (actionBar != null) {
            actionBar.remove();
        }
    }

}
