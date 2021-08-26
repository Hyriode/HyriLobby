package fr.hyriode.hyrilobby.scoreboard;

import fr.hyriode.common.board.Scoreboard;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.util.References;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 25/08/2021 at 19:13
 */
public class LobbyScoreboard extends Scoreboard {

    private static final String DASH = ChatColor.WHITE + " ⁃ ";

    private final HyriLobby plugin;

    public LobbyScoreboard(HyriLobby plugin, Player player) {
        super(plugin, player, "lobby", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Hyriode");
        this.plugin = plugin;

        this.addLines();
    }

    private void addLines() {
        this.addDateLine();
        this.setLine(1, " ");
        this.addProfileLines();
        this.setLine(6, "  ");
        this.addServerLines();
        this.setLine(9, "   ");
        this.addServerIpLine();
    }

    private void addDateLine() {
        this.setLine(0, this.getCurrentDate(), scoreboardLine -> scoreboardLine.setValue(this.getCurrentDate()), 20);
    }

    private void addProfileLines() {
        final IHyriPlayer player = this.plugin.getAPI().getPlayerManager().getPlayer(this.player.getUniqueId());

        this.setLine(2, this.getPlayerNameLine(player), scoreboardLine -> scoreboardLine.setValue(this.getPlayerNameLine(player)), 60);
        this.setLine(3, this.getRankLine(player), scoreboardLine -> scoreboardLine.setValue(this.getRankLine(player)), 60);
        this.setLine(4, this.getHyrisLine(player), scoreboardLine -> scoreboardLine.setValue(this.getHyrisLine(player)), 60);
        this.setLine(5, this.getHyodeLine(player), scoreboardLine -> scoreboardLine.setValue(this.getHyodeLine(player)), 60);
    }

    private void addServerLines() {
        this.setLine(7, this.getServerId());
        this.setLine(8, this.getConnectedPlayers(), scoreboardLine -> scoreboardLine.setValue(this.getConnectedPlayers()), 80);
    }

    private void addServerIpLine() {
        this.setLine(10, ChatColor.DARK_AQUA + References.SERVER_IP, new LobbyScoreboardIpConsumer(References.SERVER_IP), 2);
    }

    private String getCurrentDate() {
        return ChatColor.GRAY + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    private String getPlayerNameLine(IHyriPlayer player) {
        return ChatColor.WHITE + "\u27A2 " + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + player.getName() + ChatColor.RESET + (player.hasNickname() ? ChatColor.GRAY + " (" + player.getCustomName() + ")" : "");
    }

    private String getRankLine(IHyriPlayer player) {
        return DASH + "Grade: " + player.getRank().getDisplayName();
    }

    private String getHyrisLine(IHyriPlayer player) {
        return DASH + ChatColor.BLUE + "Hyris: " + ChatColor.WHITE + player.getHyris().getAmount();
    }

    private String getHyodeLine(IHyriPlayer player) {
        return DASH + ChatColor.GOLD + "Hyode: " + ChatColor.WHITE + player.getHyode().getAmount();
    }

    private String getServerId() {
        // return ChatColor.DARK_GRAY + "Serveur: " + ChatColor.WHITE + this.plugin.getAPI().getServer().getId();
        return ChatColor.GRAY + "Serveur: " + ChatColor.WHITE + "lobby-sq68zv";
    }

    private String getConnectedPlayers() {
        // TODO Get global players
        return ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size();
    }

}
