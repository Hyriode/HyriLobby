package fr.hyriode.lobby.game.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.lobby.game.LobbyGame;
import org.bukkit.Material;

/**
 * Created by AstFaster
 * on 26/06/2022 at 11:44
 */
public class GetDownGame extends LobbyGame {

    public GetDownGame() {
        super("getdown", Material.SEA_LANTERN, State.OPENED);
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
