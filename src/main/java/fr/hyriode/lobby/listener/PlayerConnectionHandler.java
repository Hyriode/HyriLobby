package fr.hyriode.lobby.listener;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 00:13
 */
public class PlayerConnectionHandler extends HyriListener<HyriLobby> {

    public PlayerConnectionHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);
        this.plugin.getPlayerManager().handleLogin(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        event.setQuitMessage(null);
        this.plugin.getPlayerManager().handleLogout(player.getUniqueId());
    }
}
