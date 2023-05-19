package fr.hyriode.lobby.player;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.scoreboard.LobbyScoreboard;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:35
 */
public class LobbyPlayerManager {

    private final HyriLobby plugin;

    private final Set<LobbyPlayer> players;

    public LobbyPlayerManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.players = new HashSet<>();

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (LobbyScoreboard scoreboard : plugin.getHyrame().getScoreboardManager().getScoreboards(LobbyScoreboard.class)) {
                scoreboard.update(false);
            }
        }, 20L, 20L);
    }

    public LobbyPlayer getLobbyPlayer(UUID uuid) {
        return this.players.stream().filter(lobbyPlayer -> lobbyPlayer.getUniqueId().compareTo(uuid) == 0).findFirst().orElse(null);
    }

    public void handleLogin(UUID uuid) {
        final LobbyPlayer lobbyPlayer = new LobbyPlayer(uuid, this.plugin);

        this.players.add(lobbyPlayer);

        lobbyPlayer.handleLogin(true, true);
    }

    public void handleLogout(UUID uuid) {
        final LobbyPlayer lobbyPlayer = this.getLobbyPlayer(uuid);

        lobbyPlayer.handleLogout();

        this.players.remove(lobbyPlayer);
        this.plugin.getHostHandler().removeWaitingPlayer(uuid);
    }

    public void handleStop() {
        this.players.forEach(LobbyPlayer::handleLogout);
    }
}
