package fr.hyriode.lobby.game.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.queue.IHyriQueue;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.event.PlayerJoinQueueEvent;
import fr.hyriode.api.queue.event.PlayerLeaveQueueEvent;
import fr.hyriode.api.queue.event.QueueDisabledEvent;
import fr.hyriode.api.queue.event.QueueUpdatedEvent;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.item.hotbar.GameSelectorItem;
import fr.hyriode.lobby.ui.item.queue.LeaveQueueItem;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.player.LobbyPlayer;
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
public class NormalQueueHandler implements IHyriQueueHandler, Listener {

    private final Map<UUID, ActionBar> bars = new HashMap<>();

    private final HyriLobby plugin;
    private final IHyrame hyrame;

    public NormalQueueHandler(HyriLobby plugin) {
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

        if (queue.getType() != IHyriQueue.Type.GAME) {
            return;
        }

        final Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player != null) {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if (!lobbyPlayer.hasJump() && !lobbyPlayer.isInPvp()) {
                this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);
            }

            this.createActionBar(player, queue);
            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_NORMAL_PLAYER_JOINED_MESSAGE);
        }
    }

    @Override
    public void onLeaveQueue(PlayerLeaveQueueEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.GAME) {
            return;
        }

        final Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player != null) {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if (!lobbyPlayer.hasJump() && !lobbyPlayer.isInPvp()) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);
            }

            this.removeActionBar(player);
            this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_NORMAL_PLAYER_LEFT_MESSAGE);
        }
    }

    @Override
    public void onUpdate(QueueUpdatedEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.GAME) {
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

            final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(queue.getGame());

            bar.setMessage(LobbyMessage.QUEUE_NORMAL_DISPLAY_BAR.asString(player)
                    .replace("%game%", gameInfo.getDisplayName())
                    .replace("%game_type%", gameInfo.getType(queue.getGameType()).getDisplayName()));
            bar.send(player);

            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(playerId);

            if (!lobbyPlayer.hasJump() && !lobbyPlayer.isInPvp()) {
                this.hyrame.getItemManager().giveItem(player, 0, LeaveQueueItem.class);
            }
        }
    }

    @Override
    public void onDisable(QueueDisabledEvent event) {
        final IHyriQueue queue = event.getQueue();

        if (queue.getType() != IHyriQueue.Type.GAME) {
            return;
        }

        for (UUID playerId : queue.getPlayers()) {
             final Player player = Bukkit.getPlayer(playerId);

             if (player == null) {
                 continue;
             }

            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(playerId);

            if (!lobbyPlayer.hasJump() && !lobbyPlayer.isInPvp()) {
                this.hyrame.getItemManager().giveItem(player, 0, GameSelectorItem.class);
            }


             this.removeActionBar(player);
             this.sendQueueMessage(player, queue, LobbyMessage.QUEUE_NORMAL_PLAYER_LEFT_MESSAGE);
        }
    }

    private void createActionBar(Player player, IHyriQueue queue) {
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(queue.getGame());
        final ActionBar actionBar = new ActionBar(LobbyMessage.QUEUE_NORMAL_DISPLAY_BAR.asString(player)
                .replace("%game%", gameInfo.getDisplayName())
                .replace("%game_type%", gameInfo.getType(queue.getGameType()).getDisplayName()));

        actionBar.sendPermanent(this.plugin, player);

        this.bars.put(player.getUniqueId(), actionBar);
    }

    private void sendQueueMessage(Player player, IHyriQueue queue, LobbyMessage message) {
        final IHyriGameInfo gameInfo = HyriAPI.get().getGameManager().getGameInfo(queue.getGame());

        player.sendMessage(message.asString(player).replace("%game%", gameInfo.getDisplayName()).replace("%game_type%", gameInfo.getType(queue.getGameType()).getDisplayName()));
    }

    private void removeActionBar(Player player) {
        final ActionBar actionBar = this.bars.remove(player.getUniqueId());

        if (actionBar != null) {
            actionBar.remove();
        }
    }

}
