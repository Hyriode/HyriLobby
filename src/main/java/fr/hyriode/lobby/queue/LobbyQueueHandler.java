package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyDisbandEvent;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.hyggdrasil.api.queue.HyggQueueGroup;
import fr.hyriode.hyggdrasil.api.queue.HyggQueueInfo;
import fr.hyriode.hyggdrasil.api.queue.HyggQueuePlayer;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueAddPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueInfoPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueRemovePacket;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.GameSelectorItem;
import fr.hyriode.lobby.item.queue.LeaveQueueItem;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.protocol.LobbyProtocol;
import fr.hyriode.lobby.protocol.queue.PartyLeftQueuePacket;
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
public class LobbyQueueHandler implements IHyriQueueHandler, Listener, IHyriPacketReceiver {

    private final Map<UUID, ActionBar> bars;

    private final HyriLobby plugin;
    private final IHyrame hyrame;

    public LobbyQueueHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.hyrame = this.plugin.getHyrame();
        this.bars = new HashMap<>();

        HyriAPI.get().getPubSub().subscribe(LobbyProtocol.CHANNEL, this);
        HyriAPI.get().getNetworkManager().getEventBus().register(this);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @HyriEventHandler
    public void onPartyLeave(HyriPartyLeaveEvent event) {
        final UUID playerId = event.getMember();
        final Player player = Bukkit.getPlayer(playerId);

        if (player != null) {
            this.removeActionBar(playerId);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final IHyriPlayer account = IHyriPlayer.get(playerId);
        final IHyriParty party = account.hasParty() ? HyriAPI.get().getPartyManager().getParty(account.getParty()) : null;
        final IHyriQueueManager queueManager = HyriAPI.get().getQueueManager();
        final IHyriQueue queue = party != null ? queueManager.getPartyQueue(party.getId()) : queueManager.getPlayerQueue(playerId);

        if (queue != null) {
            this.createActionBar(player, new HyggQueueInfo(queue.getGame(), queue.getGameType(), queue.getGame(), -1, -1));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.removeActionBar(event.getPlayer().getUniqueId());
    }

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof PartyLeftQueuePacket) {
            final PartyLeftQueuePacket queuePacket = (PartyLeftQueuePacket) packet;

            for (Player player : Bukkit.getOnlinePlayers()) {
                final UUID playerId = player.getUniqueId();

                if (IHyriPlayer.get(playerId).getParty().equals(queuePacket.getPartyId())) {
                    this.removeActionBar(playerId);
                }
            }
        }
    }

    @Override
    public void onQueueInfo(HyggQueueInfoPacket packet) {
        final UUID playerId = packet.getPlayer().getUniqueId();
        final Player player = Bukkit.getPlayer(playerId);
        final HyggQueueInfo info = packet.getQueueInfo();

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

        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

        actionBar.setMessage(LobbyMessage.QUEUE_DISPLAY_BAR.asString(player)
                .replace("%game%", gameInfo.getDisplayName())
                .replace("%game_type%", gameInfo.getType(info.getGameType()).getDisplayName())
                .replace("%position%", String.valueOf(packet.getPlace()))
                .replace("%total%", String.valueOf(packet.getQueueInfo().getTotalGroups())));
        actionBar.send(player);
    }

    @Override
    public void onPlayerAddResponse(HyggQueueAddPacket.Response response) {
        final HyggQueueAddPacket.ResponseType type = response.getType();
        final HyggQueuePlayer leader = response.getGroup().getLeader();
        final HyggQueueInfo info = response.getQueueInfo();

        if (type == HyggQueueAddPacket.ResponseType.ADDED) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_JOINED_MESSAGE);
        } else if (type == HyggQueueAddPacket.ResponseType.ALREADY_IN) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_ALREADY_IN_MESSAGE);
        }
    }

    @Override
    public void onPlayerRemoveResponse(HyggQueueRemovePacket.Response response) {
        final HyggQueueRemovePacket.ResponseType type = response.getType();
        final HyggQueueGroup group = response.getGroup();

        if (group == null) {
            return;
        }

        final HyggQueuePlayer leader = group.getLeader();
        final HyggQueueInfo info = response.getQueueInfo();

        if (type == HyggQueueRemovePacket.ResponseType.REMOVED) {
            final UUID playerId = leader.getUniqueId();
            final Player player = Bukkit.getPlayer(playerId);

            if (player != null) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

                this.removeActionBar(playerId);
            }

            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_LEFT_MESSAGE);
        } else if (type == HyggQueueRemovePacket.ResponseType.NOT_IN_QUEUE) {
            this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_NOT_IN_QUEUE_MESSAGE);
        }
    }

    @Override
    public void onPartyAddResponse(HyggQueueAddPacket.Response response) {
        final HyggQueueAddPacket.ResponseType type = response.getType();
        final HyggQueueGroup group = response.getGroup();
        final HyggQueueInfo info = response.getQueueInfo();

        if (type == HyggQueueAddPacket.ResponseType.ADDED) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_JOINED_MESSAGE);
        } else if (type == HyggQueueAddPacket.ResponseType.ALREADY_IN) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_ALREADY_IN_MESSAGE);
        }
    }

    @Override
    public void onPartyRemoveResponse(HyggQueueRemovePacket.Response response) {
        final HyggQueueRemovePacket.ResponseType type = response.getType();
        final HyggQueueGroup group = response.getGroup();

        if (group == null) {
            return;
        }

        final HyggQueueInfo info = response.getQueueInfo();

        if (type == HyggQueueRemovePacket.ResponseType.REMOVED) {
            final Player player = Bukkit.getPlayer(group.getLeader().getUniqueId());

            this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);

            this.plugin.getProtocol().partyLeftQueue(group.getId());

            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_LEFT_MESSAGE);
        } else if (type == HyggQueueRemovePacket.ResponseType.NOT_IN_QUEUE) {
            this.handleParty(group, info, LobbyMessage.QUEUE_GROUP_NOT_IN_QUEUE_MESSAGE);
        }
    }

    private void handlePlayer(HyggQueuePlayer leader, HyggQueueInfo info, LobbyMessage message) {
        final IHyriPlayer account = IHyriPlayer.get(leader.getUniqueId());
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

        if (account == null) {
            return;
        }

        HyriAPI.get().getPlayerManager().sendMessage(account.getUniqueId(), this.formatMessage(message.asString(account), gameInfo, info.getGameType()));
    }

    private void handleParty(HyggQueueGroup group, HyggQueueInfo info, LobbyMessage message) {
        final IHyriParty party = HyriAPI.get().getPartyManager().getParty(group.getId());

        if (party != null) {
            final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

            for (HyggQueuePlayer member : group.getPlayers()) {
                final IHyriPlayer account = IHyriPlayer.get(member.getUniqueId());

                if (account == null) {
                    continue;
                }

                HyriAPI.get().getPlayerManager().sendMessage(member.getUniqueId(), this.formatMessage(message.asString(account), gameInfo, info.getGameType()));
            }
        }
    }

    private String formatMessage(String message, IHyriGameInfo gameInfo, String gameType) {
        return message.replace("%game%", gameInfo.getDisplayName()).replace("%game_type%", gameInfo.getType(gameType).getDisplayName());
    }

    private ActionBar createActionBar(Player player, HyggQueueInfo info) {
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());
        final int groups = info.getTotalGroups();
        final String formattedGroups = groups != -1 ? String.valueOf(groups) : "?";
        final ActionBar actionBar = new ActionBar(LobbyMessage.QUEUE_DISPLAY_BAR.asString(player)
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
        final ActionBar actionBar = this.bars.remove(playerId);

        if (actionBar != null) {
            actionBar.remove();
        }
    }

}
