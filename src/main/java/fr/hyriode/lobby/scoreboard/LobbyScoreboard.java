package fr.hyriode.lobby.scoreboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.game.scoreboard.HyriScoreboardIpConsumer;
import fr.hyriode.hyrame.scoreboard.HyriScoreboard;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.utils.Language;
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

    private static final String DASH = ChatColor.WHITE + " ⁃ ";

    private final IHyriPlayer account;

    public LobbyScoreboard(HyriLobby plugin, Player player) {
        super(plugin, player, "lobby", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Hyriode");

        this.account = HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId());

        this.addLines();
    }

    private void addLines() {
        this.setLine(0, this.getCurrentDate(), line -> line.setValue(this.getCurrentDate()), 20);
        this.setLine(1, " ");
        this.setLine(2, this.getPlayers(), line -> line.setValue(this.getPlayers()), 20);
        this.setLine(3, this.getServer(), line -> line.setValue(this.getServer()), 20);
        this.setLine(4, "  ");
        this.setLine(5, this.getProfile(), line -> line.setValue(this.getProfile()), 20);
        this.setLine(6, this.getRank(), line -> line.setValue(this.getRank()), 20);
        this.setLine(7, this.getHyris(), line -> line.setValue(this.getHyris()), 20);
        this.setLine(8, this.getLevel(), line -> line.setValue(this.getLevel()), 20);
        this.setLine(9, "   ");
        this.setLine(10, this.getServerIp(), new HyriScoreboardIpConsumer(HyriConstants.SERVER_IP), 2);
    }

    private String getCurrentDate() {
        return ChatColor.GRAY + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    private String getPlayers() {
        return Language.getMessage(this.player, "scoreboard.line.players");
    }

    private String getServer() {
        return Language.getMessage(this.player, "scoreboard.line.server") + HyriAPI.get().getServer().getName();
    }

    private String getProfile() {
        return Language.getMessage(this.player, "scoreboard.line.profile");
    }

    private String getRank() {
        return DASH + Language.getMessage(this.player, "scoreboard.line.rank") + this.account.getRank().getPrefix();
    }

    private String getHyris() {
        return DASH + "Hyris: " + ChatColor.LIGHT_PURPLE + this.account.getHyris().getAmount();
    }

    private String getLevel() {
        return DASH + Language.getMessage(this.player, "scoreboard.line.level") + this.account.getNetworkLeveling().getLevel();
    }

    private String getServerIp() {
        return ChatColor.DARK_AQUA + HyriConstants.SERVER_IP;
    }
}
