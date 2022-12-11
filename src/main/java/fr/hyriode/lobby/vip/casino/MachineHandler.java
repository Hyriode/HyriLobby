package fr.hyriode.lobby.vip.casino;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.config.LobbyConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

public class MachineHandler extends HyriListener<HyriLobby> {

    public MachineHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void interactEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        final Area casinoArea = new LobbyConfig.Zone(new LocationWrapper(-337D, 163D, 0D), new LocationWrapper(-372D, 197, -31)).asArea();

        if(block != null && casinoArea.isInArea(player.getLocation())) {

        }
    }
}
