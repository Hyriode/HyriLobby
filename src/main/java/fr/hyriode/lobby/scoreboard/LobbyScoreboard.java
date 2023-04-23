package fr.hyriode.lobby.scoreboard;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.game.scoreboard.IPLine;
import fr.hyriode.hyrame.scoreboard.HyriScoreboard;
import fr.hyriode.hyrame.utils.TimeUtil;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.NumberFormat;

public class LobbyScoreboard extends HyriScoreboard {

    private static final String DASH = ChatColor.WHITE + " ⁃ ";

    private IHyriPlayer account;

    public LobbyScoreboard(HyriLobby plugin, Player player) {
        super(plugin, player, "lobby", ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Hyriode " + ChatColor.AQUA + ChatColor.BOLD + "2");
        this.account = IHyriPlayer.get(this.player.getUniqueId());

        this.addBlankLine(1);
        this.addBlankLine(4);
        this.addBlankLine(10);

        this.addLines();
        this.setLine(11, this.getServerIp(), new IPLine(HyriConstants.SERVER_IP), 2);
    }

    public void update(boolean all) {
        this.account = IHyriPlayer.get(this.player.getUniqueId());

        if (all) {
            this.addLines();
        } else {
            this.addUpdatableLines();
        }

        this.updateLines();
    }

    private void addLines() {
        this.setLine(3, this.getServer());
        this.setLine(5, this.getProfile());

        this.addUpdatableLines();
    }

    private void addUpdatableLines() {
        this.setLine(0, this.getCurrentDate());
        this.setLine(2, this.getPlayers());
        this.setLine(6, this.getRank());
        this.setLine(7, this.getHyris());
        this.setLine(8, this.getHyodes());
        this.setLine(9, this.getLevel());
    }

    private String getCurrentDate() {
        return ChatColor.GRAY + TimeUtil.getCurrentFormattedDate();
    }

    private String getPlayers() {
        return this.getLinePrefix("players").replace("%value%", String.valueOf(HyriAPI.get().getNetworkManager().getPlayerCounter().getPlayers()));
    }

    private String getServer() {
        return this.getLinePrefix("server").replace("%value%", HyriAPI.get().getServer().getName());
    }

    private String getProfile() {
        return this.getLinePrefix("profile");
    }

    private String getRank() {
        final String line = DASH + this.getLinePrefix("rank");

        if (this.account.getRank().isDefault()) {
            return line.replace("%value%", HyriLanguageMessage.get("scoreboard.no-rank.value").getValue(this.account));
        }
        return line.replace("%value%", this.account.getPrefix());
    }

    private String getHyris() {
        return DASH + this.getLinePrefix("hyris").replace("%value%", NumberFormat.getInstance().format(this.account.getHyris().getAmount()).replace(",", "."));
    }

    private String getHyodes() {
        return DASH + this.getLinePrefix("hyodes").replace("%value%", NumberFormat.getInstance().format(this.account.getHyodes().getAmount()).replace(",", "."));
    }

    private String getLevel() {
        return DASH + this.getLinePrefix("level").replace("%value%", String.valueOf(this.account.getNetworkLeveling().getLevel()));
    }

    private String getServerIp() {
        return ChatColor.DARK_AQUA + HyriConstants.SERVER_IP;
    }

    private String getLinePrefix(String prefix) {
        return HyriLanguageMessage.get("scoreboard." + prefix + ".display").getValue(this.account);
    }

}
