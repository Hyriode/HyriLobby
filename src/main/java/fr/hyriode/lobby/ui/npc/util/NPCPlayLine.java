package fr.hyriode.lobby.ui.npc.util;

import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * Created by AstFaster
 * on 27/06/2022 at 15:42
 */
public class NPCPlayLine implements Function<Player, String> {

    private int count = 0;
    private int charIndex = 0;

    @Override
    public String apply(Player player) {
        final String play = LobbyMessage.GAME_NPC_HEADER_PLAY.asString(player);
        String result = ChatColor.DARK_AQUA + play;

        if (this.count >= 20) {
            if (this.charIndex > play.length()) {
                this.count = 0;
                this.charIndex = 0;
            } else {
                if (this.charIndex == 0) {
                    result = ChatColor.AQUA + play.substring(0, 1) + ChatColor.DARK_AQUA + play.substring(1);
                } else if (this.charIndex == play.length()) {
                    result = ChatColor.DARK_AQUA + play;
                } else {
                    final String start = play.substring(0, this.charIndex);
                    final String character = play.substring(this.charIndex, this.charIndex + 1);
                    final String end = play.substring(this.charIndex + 1);

                    result = ChatColor.DARK_AQUA + start + ChatColor.AQUA + character + ChatColor.DARK_AQUA + end;
                }
                this.charIndex++;
            }
        }
        this.count++;

        return ChatColor.DARK_GRAY + "» " + result + ChatColor.DARK_GRAY + " «";
    }

}
