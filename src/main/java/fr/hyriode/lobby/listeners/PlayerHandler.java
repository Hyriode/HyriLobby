package fr.hyriode.lobby.listeners;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.HotbarManager;
import fr.hyriode.lobby.leaderboard.LeaderboardHandler;
import fr.hyriode.lobby.player.PlayerManager;
import fr.hyriode.lobby.scoreboard.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Supplier;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 25/08/2021 at 19:12
 */
public class PlayerHandler extends HyriListener<HyriLobby> {

    private final PlayerManager pm;
    private final HotbarManager hotbar;
    private final ScoreboardManager scoreboard;
    private final Supplier<LeaderboardHandler> leaderboard;

    public PlayerHandler(HyriLobby plugin) {
        super(plugin);

        this.pm = new PlayerManager(plugin);
        this.hotbar = new HotbarManager(plugin);
        this.scoreboard = new ScoreboardManager(plugin);
        this.leaderboard = plugin::getLeaderboardHandler;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        this.pm.onLogin(player);
        this.hotbar.onLogin(player);
        this.scoreboard.onLogin(player);
        this.leaderboard.get().onLogin(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        this.pm.onLogout(player);
        this.scoreboard.onLogout(player);
        this.leaderboard.get().onLogout(player);
    }
}
