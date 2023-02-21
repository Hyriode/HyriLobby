package fr.hyriode.lobby.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.party.event.HyriPartyLeaveEvent;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.api.queue.event.PlayerJoinQueueEvent;
import fr.hyriode.api.queue.event.PlayerLeaveQueueEvent;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.item.hotbar.GameSelectorItem;
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
        final IHyriPlayerSession account = IHyriPlayerSession.get(playerId);
        final IHyriParty party = account.hasParty() ? IHyriParty.get(account.getParty()) : null;
        final IHyriQueueManager queueManager = HyriAPI.get().getQueueManager();
        final IHyriQueue queue = queueManager.getQueue(playerId);

        if (queue != null && party.isLeader(account.getPlayerId())) {
            this.createActionBar(player, queue);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.removeActionBar(event.getPlayer().getUniqueId());
    }


    @Override
    public void onJoinQueue(PlayerJoinQueueEvent event) {
        final IHyriPlayerSession account = IHyriPlayerSession.get(event.getPlayerId());

        final IHyriQueue info = event.getQueue();

        final UUID leader = IHyriParty.get(account.getParty()).getLeader();

        this.handlePlayer(leader, info, LobbyMessage.QUEUE_PLAYER_JOINED_MESSAGE);
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

        this.handlePlayer(leaderId, group, LobbyMessage.QUEUE_PLAYER_LEFT_MESSAGE);

    }

    private void handlePlayer(UUID leaderId, IHyriQueue info, LobbyMessage message) {
        final Player player = Bukkit.getPlayer(leaderId);

        if (player == null) {
            return;
        }

        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());

        player.sendMessage(this.formatMessage(message.asString(player), gameInfo, info.getGameType()));
    }

    private String formatMessage(String message, IHyriGameInfo gameInfo, String gameType) {
        return message.replace("%game%", gameInfo.getDisplayName()).replace("%game_type%", gameInfo.getType(gameType).getDisplayName());
    }

    private ActionBar createActionBar(Player player, IHyriQueue info) {
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(info.getGame());
        final int groups = info.getTotalPlayers().size();

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
