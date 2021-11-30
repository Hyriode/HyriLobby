package fr.hyriode.lobby.listener;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.HotbarManager;
import fr.hyriode.lobby.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 25/08/2021 at 19:12
 */
public class PlayerHandler implements Listener {

    private final HyriLobby plugin;

    public PlayerHandler(HyriLobby plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        new PlayerManager(player, this.plugin).onLogin();
        new HotbarManager(this.plugin, player).onLogin();
        this.plugin.getScoreboardManager().onLogin(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        PlayerManager.getByUuid(player.getUniqueId()).onLogout();
        this.plugin.getScoreboardManager().onLogout(player);
    }
}
