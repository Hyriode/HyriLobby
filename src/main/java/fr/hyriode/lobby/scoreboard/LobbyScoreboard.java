package fr.hyriode.lobby.scoreboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.game.scoreboard.HyriScoreboardIpConsumer;
import fr.hyriode.hyrame.scoreboard.HyriScoreboard;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 25/08/2021 at 19:13
 */
public class LobbyScoreboard extends HyriScoreboard {

    private static final String ARROW = ChatColor.WHITE + "\u27A4";

    private static final String DASH = ChatColor.WHITE + " ⁃ ";

    public LobbyScoreboard(HyriLobby plugin, Player player) {
        super(plugin, player, "lobby", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Hyriode");

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
    }

    private IHyriPlayer getHyriPlayer() {
        return HyriAPI.get().getPlayerManager().getPlayer(this.player.getUniqueId());
    }

    private void addServerLines() {
        this.setLine(7, this.getNetworkLine());
        this.setLine(8, this.getServerId());
        this.setLine(9, this.getConnectedPlayers(), scoreboardLine -> scoreboardLine.setValue(this.getConnectedPlayers()), 80);
    }

    private void addServerIpLine() {
        this.setLine(11, ChatColor.DARK_AQUA + HyriConstants.SERVER_IP, new HyriScoreboardIpConsumer(HyriConstants.SERVER_IP), 2);
    }

    private String getCurrentDate() {
        return ChatColor.GRAY + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    private String getPlayerNameLine(IHyriPlayer player) {
        return ARROW + " " + (player.hasCustomName() ? ChatColor.RED + "" + ChatColor.BOLD + "Nick: " + ChatColor.RESET + ChatColor.GRAY + player.getCustomName() : ChatColor.DARK_AQUA + "" + ChatColor.BOLD + player.getName());
    }

    private String getRankLine(IHyriPlayer player) {
        return DASH + ChatColor.AQUA + "Grade: " + player.getRank().getName();
    }

    private String getHyrisLine(IHyriPlayer player) {
        return DASH + ChatColor.BLUE + "Hyris: " + ChatColor.WHITE + player.getHyris().getAmount();
    }

    private String getNetworkLine() {
        return ARROW + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + " Serveur";
    }

    private String getServerId() {
        return DASH + ChatColor.GRAY + "Actuel: " + ChatColor.WHITE + HyriAPI.get().getServer().getName();
    }

    private String getConnectedPlayers() {
        return DASH + ChatColor.GRAY + "Joueurs: " + ChatColor.WHITE + HyriAPI.get().getNetwork().getPlayers();
    }

}
