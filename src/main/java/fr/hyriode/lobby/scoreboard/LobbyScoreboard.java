package fr.hyriode.lobby.scoreboard;

import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.util.References;
import fr.hyriode.tools.scoreboard.Scoreboard;
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

    private static final String ARROW = ChatColor.WHITE + "\u27A4";

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
        this.setLine(10, "   ");
        this.addServerIpLine();
    }

    private void addDateLine() {
        this.setLine(0, this.getCurrentDate(), scoreboardLine -> scoreboardLine.setValue(this.getCurrentDate()), 20);
    }

    private void addProfileLines() {
        final IHyriPlayer player = this.getHyriPlayer();

        this.setLine(2, this.getPlayerNameLine(player), scoreboardLine -> scoreboardLine.setValue(this.getPlayerNameLine(this.getHyriPlayer())), 60);
        this.setLine(3, this.getRankLine(player), scoreboardLine -> scoreboardLine.setValue(this.getRankLine(this.getHyriPlayer())), 60);
        this.setLine(4, this.getHyrisLine(player), scoreboardLine -> scoreboardLine.setValue(this.getHyrisLine(this.getHyriPlayer())), 60);
        this.setLine(5, this.getHyodeLine(player), scoreboardLine -> scoreboardLine.setValue(this.getHyodeLine(this.getHyriPlayer())), 60);
    }

    private IHyriPlayer getHyriPlayer() {
        return this.plugin.getAPI().getPlayerManager().getPlayer(this.player.getUniqueId());
    }

    private void addServerLines() {
        this.setLine(7, this.getNetworkLine());
        this.setLine(8, this.getServerId());
        this.setLine(9, this.getConnectedPlayers(), scoreboardLine -> scoreboardLine.setValue(this.getConnectedPlayers()), 80);
    }

    private void addServerIpLine() {
        this.setLine(11, ChatColor.DARK_AQUA + References.SERVER_IP, new LobbyScoreboardIpConsumer(References.SERVER_IP), 2);
    }

    private String getCurrentDate() {
        return ChatColor.GRAY + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    private String getPlayerNameLine(IHyriPlayer player) {
        return ARROW + " " + (player.hasCustomName() ? ChatColor.RED + "" + ChatColor.BOLD + "Nick: " + ChatColor.RESET + ChatColor.GRAY + player.getCustomName() : ChatColor.DARK_AQUA + "" + ChatColor.BOLD + player.getName());
    }

    private String getRankLine(IHyriPlayer player) {
        return DASH + ChatColor.AQUA + "Grade: " + player.getRank().getDisplayName();
    }

    private String getHyrisLine(IHyriPlayer player) {
        return DASH + ChatColor.BLUE + "Hyris: " + ChatColor.WHITE + player.getHyris().getAmount();
    }

    private String getHyodeLine(IHyriPlayer player) {
        return DASH + ChatColor.GOLD + "Hyode: " + ChatColor.WHITE + player.getHyode().getAmount();
    }

    private String getNetworkLine() {
        return ARROW + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Serveur";
    }

    private String getServerId() {
        // return ChatColor.DARK_GRAY + "Serveur: " + ChatColor.WHITE + this.plugin.getAPI().getServer().getId();
        return DASH + ChatColor.GRAY + "Actuel: " + ChatColor.WHITE + "lobby-sq68zv";
    }

    private String getConnectedPlayers() {
        // TODO Get global players
        return DASH + ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size();
    }

}
