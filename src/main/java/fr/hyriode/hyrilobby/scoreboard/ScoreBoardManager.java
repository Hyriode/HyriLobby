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
    private final ScheduledFuture glowingTask;
    private final ScheduledFuture reloadingTask;
    private int cooldown;

    public ScoreBoardManager(Main main) {
        this.main = main;
        this.boards = new HashMap<>();
        this.cooldown = 0;

        glowingTask = main.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            String displayName = this.titleAnimation();

            for (ScoreBoard scoreboard : this.boards.values())
                main.getExecutorMonoThread().execute(() -> scoreboard.setLines(displayName));
        }, 80, 80, TimeUnit.MILLISECONDS);

        reloadingTask = main.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            for (ScoreBoard scoreboard : this.boards.values())
                main.getExecutorMonoThread().execute(scoreboard::reloadBoard);
        }, 20, 20, TimeUnit.SECONDS);
    }

    public void onDisable() {
        this.glowingTask.cancel(true);
        this.reloadingTask.cancel(true);

        this.boards.values().forEach(ScoreBoard::onLogout);
    }

    public void onJoin(Player player) {
        if (this.boards.containsKey(player.getUniqueId())) {
            return;
        }
        this.boards.put(player.getUniqueId(), new ScoreBoard(main, player));
    }

    public void onLeave(Player player) {
        if (this.boards.containsKey(player.getUniqueId())) {
            this.boards.get(player.getUniqueId()).onLogout();
            this.boards.remove(player.getUniqueId());
        }
    }

    public void reload(Player player) {
        if (this.boards.containsKey(player.getUniqueId()))
            this.boards.get(player.getUniqueId()).reloadBoard();
    }

    private String titleAnimation() {
        String title = "Hyriode";

        if (cooldown > 0) {
            cooldown--;
            return "§3§l" + title;
        }
        if (this.cooldown < 50) {
            if (cooldown == 50) {
                return "§3§l" + title;
            }
            if (cooldown == 40) {
                return "§b§l" + title;
            }
            if (cooldown == 30) {
                return "§3§l" + title;
            }
            if (cooldown == 20) {
                return "§b§l" + title;
            }
            if (cooldown == 10) {
                return "§3§l" + title;
            }
            if(cooldown == 0) {
                return "§b§l" + title;
            }
        } else {
            cooldown = 50;
        }

        return ChatColor.DARK_AQUA + title;
    }
}
