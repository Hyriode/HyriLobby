package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hylios.api.host.HostData;
import fr.hyriode.hylios.api.queue.QueueGroup;
import fr.hyriode.hylios.api.queue.QueueInfo;
import fr.hyriode.hylios.api.queue.QueuePlayer;
import fr.hyriode.hylios.api.queue.event.QueueAddEvent;
import fr.hyriode.hylios.api.queue.event.QueueEventType;
import fr.hyriode.hylios.api.queue.event.QueueRemoveEvent;
import fr.hyriode.hylios.api.queue.packet.QueueInfoPacket;
import fr.hyriode.hylios.api.queue.server.SQueueInfo;
import fr.hyriode.hylios.api.queue.server.event.SQueueAddEvent;
import fr.hyriode.hylios.api.queue.server.event.SQueueRemoveEvent;
import fr.hyriode.hylios.api.queue.server.packet.SQueueInfoPacket;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.GameSelectorItem;
import fr.hyriode.lobby.item.queue.LeaveQueueItem;
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
    public void onQueueInfo(SQueueInfoPacket packet) {
        final UUID playerId = packet.getPlayer().getUniqueId();
        final Player player = Bukkit.getPlayer(playerId);
        final SQueueInfo info = packet.getQueueInfo();
        final HyggServer server = HyriAPI.get().getServerManager().getServer(info.getServerName());

        if (player == null || server == null) {
            return;
        }

        ActionBar actionBar = this.bars.get(playerId);

        if (actionBar == null) {
            if (packet.getGroup().getLeader().getUniqueId().equals(playerId)) {
                this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);
            }

            actionBar = this.createActionBar(player, info);
        }

        if (actionBar == null) {
            return;
        }

        actionBar.setMessage(LobbyMessage.QUEUE_HOST_DISPLAY_BAR.asString(player)
                .replace("%host%", HyriAPI.get().getHyliosAPI().getHostAPI().getHostData(server).getName())
                .replace("%position%", String.valueOf(packet.getPlace()))
                .replace("%total%", String.valueOf(packet.getQueueInfo().getTotalGroups())));
        actionBar.send(player);
    }

    @Override
    public void onPlayerRemove(SQueueRemoveEvent event) {
        final QueueEventType type = event.getType();
        final QueueGroup group = event.getGroup();

        if (group == null) {
            return;
        }

        final QueuePlayer leader = group.getLeader();

        if (type == QueueEventType.OK) {
            final UUID playerId = leader.getUniqueId();
            final Player player = Bukkit.getPlayer(playerId);

            if (player != null) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

                this.removeActionBar(playerId);
            }
        }
    }

    @Override
    public void onPartyRemove(SQueueRemoveEvent event) {
        final QueueEventType type = event.getType();
        final QueueGroup group = event.getGroup();

        if (group == null) {
            return;
        }

        if (type == QueueEventType.OK) {
            final Player player = Bukkit.getPlayer(group.getLeader().getUniqueId());

            if (player != null) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);
            }

            for (QueuePlayer member : group.getPlayers()) {
                this.removeActionBar(member.getUniqueId());
            }
        }
    }

    private ActionBar createActionBar(Player player, SQueueInfo info) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(info.getServerName());
        final int groups = info.getTotalGroups();

        if (groups == -1 || server == null) {
            return null;
        }

        final HostData hostData = HyriAPI.get().getHyliosAPI().getHostAPI().getHostData(server);
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
