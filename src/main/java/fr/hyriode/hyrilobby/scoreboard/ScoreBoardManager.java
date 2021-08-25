package fr.hyriode.hyrilobby.scoreboard;

import fr.hyriode.hyrilobby.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * All the code after the #ipAnimation method come from the github
 * of SamaGames Hub -> https://github.com/SamaGames/Hub/
 */

public class ScoreBoardManager {

    private Main main;
    private final Map<UUID, ScoreBoard> boards;

    public ScoreBoardManager(Main main) {
        this.main = main;
        this.boards = new HashMap<>();
    }
    public void onDisable() {
        boards.values().forEach(ScoreBoard::onLogout);
    }

    public void onLogin(Player player) {
        if (boards.containsKey(player.getUniqueId())) {
            return;
        }
        ScoreBoard scoreBoard = new ScoreBoard(this.main, player);
        boards.put(player.getUniqueId(), scoreBoard);
        scoreBoard.show();
    }

    public void onLogout(Player player) {
        ScoreBoard scoreBoard = this.boards.get(player.getUniqueId());

        if (scoreBoard != null) {
            this.boards.remove(player.getUniqueId()).hide();
        }
    }
}
