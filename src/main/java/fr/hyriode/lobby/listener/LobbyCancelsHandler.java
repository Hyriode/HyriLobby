package fr.hyriode.lobby.listener;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 18:43
 */
public class LobbyCancelsHandler extends HyriListener<HyriLobby> {

    public LobbyCancelsHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onFoodLevelChanges(final FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());

        if(event.getAction() == Action.PHYSICAL) return;
        
        if(!lobbyPlayer.isInPvp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(final PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onVoid(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

        if (player.getLocation().getY() <= 90) {
            lobbyPlayer.handleLogin(false, true);
        }
    }

}
