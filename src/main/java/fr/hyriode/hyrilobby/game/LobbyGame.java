package fr.hyriode.hyrilobby.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 26/04/2022 at 17:10
 */
public enum LobbyGame {

    PEARL_CONTROL("pearlcontrol", Material.ENDER_PEARL),
    RUSH_THE_FLAG("rushtheflag", Material.BANNER),
    THE_RUNNER("therunner", Material.DIAMOND_BOOTS),
    BRIDGER("bridger", Material.SANDSTONE),
    BEDWARS("bedwars", Material.BED),
    LASER_GAME("lasergame", Material.IRON_HOE),
    ;

    private final IHyriGameInfo game;
    private final Material icon;
    private final String description;
    private final String gameType;

    LobbyGame(String name, Material icon) {
        this.game = HyriAPI.get().getGameManager().getGameInfo(name);
        this.icon = icon;
        this.description = "game." + name + ".description";
        this.gameType = "game." + name + ".type";
    }

    public IHyriGameInfo getGame() {
        return this.game;
    }

    public Material getIcon() {
        return this.icon;
    }

    public String getGameTypeLine(Player player) {
        return LobbyMessage.TYPE_LINE.get().getForPlayer(player) + HyriLanguageMessage.get(this.gameType).getForPlayer(player);
    }

    public List<String> getDescription(Player player) {
        List<String> outputLore = new ArrayList<>();
        String[] splitLore = HyriLanguageMessage.get(this.description).getForPlayer(player).split("\n");
        for(String desc : splitLore){
            outputLore.add(ChatColor.GRAY + desc);
        }
        return outputLore;
    }
}
