package fr.hyriode.hyrilobby.scoreboard;

import fr.hyriode.hyrilobby.Main;
import fr.hyriode.tools.scoreboard.ObjectiveSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ScoreBoard {

    private BukkitTask reloadScoreboardTask;

    private String displayName;
    private final Main main;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    public ScoreBoard(Main main, Player player) {
        this.main = main;
        this.uuid = player.getUniqueId();
        displayName = "§3§lHyriode";
        objectiveSign = new ObjectiveSign("sidebar", this.displayName);
    }
    public void show() {
        this.objectiveSign.addPlayer(Bukkit.getPlayer(uuid));

        setLines();

        this.reloadScoreboardTask = this.reloadScoreboard();
    }

    public void hide() {
        if (this.reloadScoreboardTask != null) {
            this.reloadScoreboardTask.cancel();
        }

        this.objectiveSign.removePlayer(Bukkit.getPlayer(uuid));
    }
    private BukkitTask reloadScoreboard() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                setLines();
            }
        }.runTaskTimer(this.main, 20, 20);
    }

    public void setLines() {
        objectiveSign.setLine(0, "§1");
        objectiveSign.setLine(1, "§6§l" +Bukkit.getPlayer(uuid).getName());
        objectiveSign.setLine(2, "§9» §fGrade: §2Développeur");
        objectiveSign.setLine(3, "§9» §fHyris: §b" +main.getApi().getPlayerManager().getPlayer(uuid).getHyris().getAmount());
        objectiveSign.setLine(4, "§9» §fHyode: §b" +main.getApi().getPlayerManager().getPlayer(uuid).getHyode().getAmount());
        objectiveSign.setLine(5, "§3");
        objectiveSign.setLine(6, "§6§lServeur");
        objectiveSign.setLine(7, "§9» §fConnectés: §b" +Bukkit.getOnlinePlayers().size());
        objectiveSign.setLine(8, "§9» §fLobby: §b" +Bukkit.getMotd());
        objectiveSign.setLine(9, "§4");
        objectiveSign.setLine(10, "§ehyriode.fr");

        this.objectiveSign.updateLines();
    }
    public void onLogout() {
        objectiveSign.removePlayer(main.getServer().getPlayer(this.uuid));
    }
}
