package fr.hyriode.lobby.game.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.lobby.game.LobbyGame;
import org.bukkit.Material;

/**
 * Created by AstFaster
 * on 26/06/2022 at 11:44
 */
public class SheepWarsGame extends LobbyGame {

    public SheepWarsGame() {
        super("sheepwars", Material.WOOL, State.OPENED);
        this.usedInSelector = false;
    }

    @Override
    public boolean isBoostable() {
        return HyriAPI.get().getGameManager()
                .getRotatingGameManager()
                .getRotatingGame()
                .getInfo()
                .getName()
                .equals(this.name);
    }

}
