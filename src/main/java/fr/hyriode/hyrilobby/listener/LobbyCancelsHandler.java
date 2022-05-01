package fr.hyriode.hyrilobby.listener;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
    public void onEntityDamage(final EntityDamageEvent event) {
        event.setCancelled(true);

        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            final LobbyPlayer lp = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            if (lp.isInPvp()) {
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(final PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }
}
