package fr.hyriode.hyrilobby.scoreboard;

import fr.hyriode.hyrilobby.Main;
import fr.hyriode.tools.scoreboard.ObjectiveSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ScoreBoard {

    private final Main main;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    public ScoreBoard(Main main, Player player) {
        this.main = main;
        this.uuid = player.getUniqueId();

        this.objectiveSign = new ObjectiveSign(main.getApi().getServer().getId(), "Hyriode");
        this.reloadBoard();
        this.objectiveSign.addPlayer(player);
    }

    public void onLogout() {
        this.objectiveSign.removePlayer(this.main.getServer().getPlayer(this.uuid));
    }
    public void reloadBoard() {
        // Not used
    }
    public void setLines(String displayName) {
        this.objectiveSign.setDisplayName(displayName);

        this.objectiveSign.setLine(0, "§1");
        this.objectiveSign.setLine(1, "§7Serveur §9» §b" +main.getApi().getServer().getId());
        this.objectiveSign.setLine(2, "§7Connectés §9» §b"); //TODO
        this.objectiveSign.setLine(3, "§2");
        this.objectiveSign.setLine(4, "§7Pseudo §9» §b" + main.getApi().getPlayerManager().getPlayer(uuid).getName());
        this.objectiveSign.setLine(5, "§7Grade §9» §b"); //TODO
        this.objectiveSign.setLine(6, "§3");
        this.objectiveSign.setLine(7, "§7Hyris §9» §b" + main.getApi().getPlayerManager().getPlayer(uuid).getHyris());
        this.objectiveSign.setLine(8, "§7Hyrode§9» §b" + main.getApi().getPlayerManager().getPlayer(uuid).getHyode());
        this.objectiveSign.setLine(9, "§4");
        this.objectiveSign.setLine(10, "§3mc.hyriode.fr");

        this.objectiveSign.updateLines();
    }
}
