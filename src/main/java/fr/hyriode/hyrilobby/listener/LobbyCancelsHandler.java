package fr.hyriode.hyrilobby.listener;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

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
        if(event.getItem().getType() != Material.GOLDEN_APPLE) {
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

        if(player.getLocation().getY() <= 90) {
            lobbyPlayer.teleportToSpawn();
        }
    }
}
