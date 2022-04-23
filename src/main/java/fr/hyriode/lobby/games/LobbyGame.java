package fr.hyriode.lobby.games;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import org.bukkit.Material;

/**
 * Project: HyriLobby
 * Created by AstFaster
 * on 23/04/2022 at 15:57
 */
public enum LobbyGame {

    PEARL_CONTROL("pearlcontrol", Material.ENDER_PEARL),
    RUSH_THE_FLAG("rushtheflag", Material.WOOL),
    THE_RUNNER("therunner", Material.IRON_BOOTS),
    BRIDGER("bridger", Material.SANDSTONE),
    BEDWARS("bedwars", Material.BED),
    LASER_GAME("lasergame", Material.IRON_HOE)
    ;

    private final IHyriGameInfo game;
    private final Material icon;


    LobbyGame(String name, Material icon) {
        this.game = HyriAPI.get().getGameManager().getGameInfo(name);
        this.icon = icon;
    }

    public IHyriGameInfo getGame() {
        return this.game;
    }

    public Material getIcon() {
        return this.icon;
    }

}
