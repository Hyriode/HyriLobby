package fr.hyriode.lobby.leaderboard;

import fr.hyriode.api.leveling.NetworkLeveling;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.leaderboard.HyriLeaderboardDisplay;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AstFaster
 * on 04/07/2022 at 20:38
 */
public class LobbyLeaderboardManager implements Listener {

    private final List<HyriLeaderboardDisplay> displays;

    private final HyriLobby plugin;
    private final LobbyJumpLeaderboard jumpLeaderboard;

    public LobbyLeaderboardManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.displays = new ArrayList<>();
        this.jumpLeaderboard = new LobbyJumpLeaderboard(this.plugin);

        this.displays.add(this.jumpLeaderboard.createDisplay());

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.addLevelingDisplays();
    }

    public void refreshLeaderboards(Player player) {
        for (HyriLeaderboardDisplay display : this.displays) {
            display.update(player);
        }
    }

    private void addLevelingDisplays() {
        final HyriLeaderboardDisplay display = new HyriLeaderboardDisplay.Builder(this.plugin, NetworkLeveling.LEADERBOARD_TYPE, NetworkLeveling.LEADERBOARD_NAME, new Location(IHyrame.WORLD.get(), -14.5, 185.5, 8.5))
                .withHeader(LobbyMessage.LEADERBOARD_LEVELING_HEADER::asString)
                .withUpdateTime(20L * 60L)
                .withScoreFormatter((target, score) -> (score == 0.0D ? "0" : String.valueOf(NetworkLeveling.ALGORITHM.experienceToLevel(score))) + "✯")
                .build();

        display.show();

        this.displays.add(display);
    }

    public LobbyJumpLeaderboard getJumpLeaderboard() {
        return this.jumpLeaderboard;
    }

}
