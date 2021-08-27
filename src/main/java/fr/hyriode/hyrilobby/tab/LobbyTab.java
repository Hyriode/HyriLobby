package fr.hyriode.hyrilobby.tab;

import fr.hyriode.common.tab.Tab;
import fr.hyriode.hyrilobby.util.References;
import org.bukkit.ChatColor;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 27/08/2021 at 08:23
 */
public class LobbyTab extends Tab {

    private static final String RIGHT_QUOTE_MARK = ChatColor.AQUA + "\u00BB";
    private static final String LEFT_QUOTE_MARK = ChatColor.AQUA + "\u00AB";

    public LobbyTab() {
        this.addLines();
    }

    private void addLines() {
        this.addHeaderLines();
        this.addFooterLines();
    }

    private void addHeaderLines() {
        this.setBlankHeaderLine(0);
        this.setHeaderLine(1, RIGHT_QUOTE_MARK + ChatColor.DARK_AQUA + ChatColor.BOLD + " Hyriode " + ChatColor.RESET + LEFT_QUOTE_MARK);
        this.setBlankHeaderLine(2);
    }

    private void addFooterLines() {
        this.setBlankFooterLine(0);
        this.setFooterLine(1, ChatColor.GRAY + "Site et Forum: " + ChatColor.AQUA + References.WEBSITE_URL);
        this.setFooterLine(2, ChatColor.GRAY + "Boutique: " + ChatColor.GOLD + References.STORE_WEBSITE_URL);
        this.setFooterLine(3, ChatColor.GRAY + "Discord: " + ChatColor.BLUE + References.DISCORD_URL);
        this.setBlankFooterLine(4);
    }

}
