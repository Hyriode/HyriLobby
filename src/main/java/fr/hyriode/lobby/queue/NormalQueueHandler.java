package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.hylios.api.queue.QueueGroup;
import fr.hyriode.hylios.api.queue.QueueInfo;
import fr.hyriode.hylios.api.queue.QueuePlayer;
import fr.hyriode.hylios.api.queue.event.QueueAddEvent;
import fr.hyriode.hylios.api.queue.event.QueueEventType;
import fr.hyriode.hylios.api.queue.event.QueueRemoveEvent;
import fr.hyriode.hylios.api.queue.packet.QueueInfoPacket;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 24/06/2022 at 14:45
 */
public class NormalQueueHandler implements IHyriQueueHandler, Listener {

    private final Map<UUID, ActionBar> bars;

    private final HyriLobby plugin;
    private final IHyrame hyrame;

    public NormalQueueHandler(HyriLobby plugin) {
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
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayer account = IHyriPlayer.get(playerId);
        final IHyriParty party = account.hasParty() ? IHyriParty.get(account.getParty()) : null;
        final IHyriQueueManager queueManager = HyriAPI.get().getQueueManager();
        final IHyriQueue queue = party != null ? queueManager.getPartyQueue(party.getId()) : queueManager.getPlayerQueue(playerId);

        if (queue != null) {
            this.createActionBar(player, new QueueInfo(queue.getGame(), queue.getGameType(), queue.getGame(), -1, -1));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.removeActionBar(event.getPlayer().getUniqueId());
    }

    @Override
    public void onQueueInfo(QueueInfoPacket packet) {
        final UUID playerId = packet.getPlayer().getUniqueId();
        final Player player = Bukkit.getPlayer(playerId);
        final QueueInfo info = packet.getQueueInfo();

        if (player == null) {
            return;
        }

        ActionBar actionBar = this.bars.get(playerId);

        if (actionBar == null) {
            actionBar = this.createActionBar(player, info);

            if (packet.getGroup().getLeader().getUniqueId().equals(playerId)) {
                this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);
            }
        }

        if (actionBar == null) {
            return;
        }

        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

        actionBar.setMessage(LobbyMessage.QUEUE_NORMAL_DISPLAY_BAR.asString(player)
                .replace("%game%", gameInfo.getDisplayName())
                .replace("%game_type%", gameInfo.getType(info.getGameType()).getDisplayName())
                .replace("%position%", String.valueOf(packet.getPlace()))
                .replace("%total%", String.valueOf(packet.getQueueInfo().getTotalGroups())));
        actionBar.send(player);
    }

    @Override
    public void onPlayerAdd(QueueAddEvent event) {
        final QueueEventType type = event.getType();
        final QueuePlayer leader = event.getGroup().getLeader();
        final QueueInfo info = event.getQueueInfo();

        if (type == QueueEventType.OK) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_JOINED_MESSAGE);
        } else if (type == QueueEventType.ALREADY_IN) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_ALREADY_IN_MESSAGE);
        }
    }

    @Override
    public void onPlayerRemove(QueueRemoveEvent event) {
        final QueueEventType type = event.getType();
        final QueueGroup group = event.getGroup();

        if (group == null) {
            return;
        }

        final QueuePlayer leader = group.getLeader();
        final QueueInfo info = event.getQueueInfo();

        if (type == QueueEventType.OK) {
            final UUID playerId = leader.getUniqueId();
            final Player player = Bukkit.getPlayer(playerId);

            if (player != null) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

                this.removeActionBar(playerId);
            }

            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_LEFT_MESSAGE);
        } else if (type == QueueEventType.NOT_IN_QUEUE) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_NOT_IN_QUEUE_MESSAGE);
        }
    }

    @Override
    public void onPartyAdd(QueueAddEvent event) {
        final QueueEventType type = event.getType();
        final QueueGroup group = event.getGroup();
        final QueueInfo info = event.getQueueInfo();

        if (type == QueueEventType.OK) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_JOINED_MESSAGE);
        } else if (type == QueueEventType.ALREADY_IN) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_ALREADY_IN_MESSAGE);
        }
    }

    @Override
    public void onPartyRemove(QueueRemoveEvent event) {
        final QueueEventType type = event.getType();
        final QueueGroup group = event.getGroup();

        if (group == null) {
            return;
        }

        final QueueInfo info = event.getQueueInfo();

        if (type == QueueEventType.OK) {
            final Player player = Bukkit.getPlayer(group.getLeader().getUniqueId());

            if (player != null) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);
            }

            for (QueuePlayer member : group.getPlayers()) {
                this.removeActionBar(member.getUniqueId());
            }

            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_LEFT_MESSAGE);
        } else if (type == QueueEventType.NOT_IN_QUEUE) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_NOT_IN_QUEUE_MESSAGE);
        }
    }

    private void handlePlayer(QueuePlayer leader, QueueInfo info, LobbyMessage message) {
        final Player player = Bukkit.getPlayer(leader.getUniqueId());

        if (player == null) {
            return;
        }

        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

        player.sendMessage(this.formatMessage(message.asString(player), gameInfo, info.getGameType()));
    }

    private void handleParty(QueueGroup group, QueueInfo info, LobbyMessage message) {
        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(group.getId());

        if (party != null) {
            final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

            for (QueuePlayer member : group.getPlayers()) {
                final Player player = Bukkit.getPlayer(member.getUniqueId());

                if (player == null) {
                    continue;
                }

                player.sendMessage(this.formatMessage(message.asString(player), gameInfo, info.getGameType()));
            }
        }
    }

    private String formatMessage(String message, IHyriGameInfo gameInfo, String gameType) {
        return message.replace("%game%", gameInfo.getDisplayName()).replace("%game_type%", gameInfo.getType(gameType).getDisplayName());
    }

    private ActionBar createActionBar(Player player, QueueInfo info) {
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());
        final int groups = info.getTotalGroups();

        if (groups == -1) {
            return null;
        }

        final String formattedGroups = String.valueOf(groups);
        final ActionBar actionBar = new ActionBar(LobbyMessage.QUEUE_NORMAL_DISPLAY_BAR.asString(player)
                .replace("%game%", gameInfo.getDisplayName())
                .replace("%game_type%", gameInfo.getType(info.getGameType()).getDisplayName())
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
