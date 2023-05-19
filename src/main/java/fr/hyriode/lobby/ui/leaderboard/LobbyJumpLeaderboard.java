package fr.hyriode.lobby.ui.leaderboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.leaderboard.HyriLeaderboardScope;
import fr.hyriode.api.leaderboard.IHyriLeaderboard;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.leaderboard.HyriLeaderboardDisplay;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/07/2022 at 20:34
 */
public class LobbyJumpLeaderboard {

    public static final String TYPE = "lobby-jump";
    public static final String NAME = "global";

    private HyriLeaderboardDisplay display;

    private final HyriLobby plugin;

    public LobbyJumpLeaderboard(HyriLobby plugin) {
        this.plugin = plugin;
    }

    public void updatePlayerScore(UUID playerId, long time) {
        final IHyriLeaderboard leaderboard = HyriAPI.get().getLeaderboardProvider().getLeaderboard(TYPE, NAME);
        final long oldTime = (long) -leaderboard.getScore(HyriLeaderboardScope.TOTAL, playerId);

        if (time < oldTime || oldTime <= 0) {
            leaderboard.setScore(playerId, -time);

            this.display.update();
        }
    }

    public HyriLeaderboardDisplay createDisplay() {
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        final DecimalFormat decimal = new DecimalFormat("000");
        this.display = new HyriLeaderboardDisplay.Builder(this.plugin, TYPE, NAME, new Location(IHyrame.WORLD.get(), 4.3, 187, -31))
                .withHeader(LobbyMessage.LEADERBOARD_JUMP_HEADER::asString)
                .withUpdateTime(20L * 60L)
                .withScoreFormatter((target, score) -> {
                    final long time = (long) -score;

                    return format.format(time) + "." + decimal.format(time % 1000);
                })
                .build();

        this.display.show();

        return this.display;
    }

}
