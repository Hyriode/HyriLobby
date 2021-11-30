package fr.hyriode.lobby.scoreboard;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.tools.scoreboard.Scoreboard;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private final Map<UUID, LobbyScoreboard> lobbyScoreboards;

    private final HyriLobby plugin;

    public ScoreboardManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.lobbyScoreboards = new HashMap<>();
    }

    public void onLogin(Player player) {
        if (!lobbyScoreboards.containsKey(player.getUniqueId())) {
            final LobbyScoreboard scoreboard = new LobbyScoreboard(this.plugin, player);

            this.lobbyScoreboards.put(player.getUniqueId(), scoreboard);

            scoreboard.show();
        }
    }

    public void onLogout(Player player) {
        final Scoreboard scoreboard = this.lobbyScoreboards.get(player.getUniqueId());

        if (scoreboard != null) {
            this.lobbyScoreboards.remove(player.getUniqueId()).hide();
        }
    }

}
