package fr.hyriode.hyrilobby.listener;

import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.tab.LobbyTab;
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

        this.plugin.getScoreboardManager().onLogin(player);
        this.plugin.getTabManager().onLogin(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.plugin.getScoreboardManager().onLogout(player);
    }

}
