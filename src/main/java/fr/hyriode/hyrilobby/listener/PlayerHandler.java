package fr.hyriode.hyrilobby.listener;

import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.PlayerManager;
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
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        PlayerManager playerManager = new PlayerManager(player, plugin);
        playerManager.onLogin();
        this.plugin.getScoreboardManager().onLogin(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        PlayerManager playerManager = PlayerManager.getByUuid(player.getUniqueId());
        playerManager.onLogout();
        this.plugin.getScoreboardManager().onLogout(player);
    }

}
