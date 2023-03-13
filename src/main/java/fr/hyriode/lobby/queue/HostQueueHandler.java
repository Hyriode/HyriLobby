package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.host.HostData;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.event.PlayerJoinQueueEvent;
import fr.hyriode.api.queue.event.PlayerLeaveQueueEvent;
import fr.hyriode.api.queue.event.QueueDisabledEvent;
import fr.hyriode.api.queue.event.QueueUpdatedEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
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

    private final Map<UUID, ActionBar> bars = new HashMap<>();

    private final HyriLobby plugin;
    private final IHyrame hyrame;

    public HostQueueHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.hyrame = this.plugin.getHyrame();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.removeActionBar(event.getPlayer());
    }

    @Override
    public void onJoinQueue(PlayerJoinQueueEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.SERVER) {
            return;
        }

        final Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player != null) {
            this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

            this.removeActionBar(player);
            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_HOST_PLAYER_JOINED_MESSAGE);
        }
    }

    @Override
    public void onLeaveQueue(PlayerLeaveQueueEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.SERVER) {
            return;
        }

        final Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player != null) {
            this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);

            this.createActionBar(player, queue);
            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_HOST_PLAYER_LEFT_MESSAGE);
        }
    }

    @Override
    public void onUpdate(QueueUpdatedEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.SERVER) {
            return;
        }

        for (UUID playerId : queue.getPlayers()) {
            final Player player = Bukkit.getPlayer(playerId);

            if (player == null) {
                continue;
            }

            final ActionBar bar = this.bars.get(playerId);

            if (bar == null) {
                continue;
            }

            final HyggServer server = HyriAPI.get().getServerManager().getServer(queue.getServer());

            if (server == null) {
                continue;
            }

            final HostData hostData = HyriAPI.get().getHostManager().getHostData(server);

            if (hostData == null) {
                continue;
            }

            bar.setMessage(LobbyMessage.QUEUE_HOST_DISPLAY_BAR.asString(player)
                    .replace("%host%", hostData.getName())
                    .replace("%server%", server.getName()));
            bar.send(player);

            this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);

            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_NORMAL_PLAYER_LEFT_MESSAGE);
        }
    }

    @Override
    public void onDisable(QueueDisabledEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.SERVER) {
            return;
        }

        for (UUID playerId : queue.getPlayers()) {
            final Player player = Bukkit.getPlayer(playerId);

            if (player == null) {
                continue;
            }

            this.removeActionBar(player);
            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_HOST_PLAYER_LEFT_MESSAGE);
        }
    }

    private void sendQueueMessage(Player player, IHyriQueue queue, LobbyMessage message) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(queue.getServer());

        if (server == null) {
            return;
        }

        final HostData hostData = HyriAPI.get().getHostManager().getHostData(server);

        if (hostData == null) {
            return;
        }

        player.sendMessage(message.asString(player)
                .replace("%host%", hostData.getName())
                .replace("%server%", server.getName()));
    }

    private void createActionBar(Player player, IHyriQueue info) {
        final HyggServer server = HyriAPI.get().getServerManager().getServer(info.getServer());

        if (server == null) {
            return;
        }

        final HostData hostData = HyriAPI.get().getHostManager().getHostData(server);

        if (hostData == null) {
            return;
        }

        final ActionBar actionBar = new ActionBar(LobbyMessage.QUEUE_HOST_DISPLAY_BAR.asString(player)
                .replace("%host%", hostData.getName())
                .replace("%server%", server.getName()));
        final ActionBar oldBar = this.bars.put(player.getUniqueId(), actionBar);

        if (oldBar != null) {
            oldBar.remove();
        }

        actionBar.sendPermanent(this.plugin, player);
    }

    private void removeActionBar(Player player) {
        final ActionBar actionBar = this.bars.remove(player.getUniqueId());

        if (actionBar != null) {
            actionBar.remove();
        }
    }

}
