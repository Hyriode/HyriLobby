package fr.hyriode.lobby.game.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Created by AstFaster
 * on 26/06/2022 at 16:27
 */
public class PearlControlGame extends LobbyGame {

    public PearlControlGame() {
        super("pearlcontrol", Material.ENDER_PEARL, State.OPENED);
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
