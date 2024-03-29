package fr.hyriode.lobby.game.model;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.Material;

public class MoutronGame extends LobbyGame {

    public MoutronGame() {
        super("moutron", ItemBuilder.asHead(UsefulHead.BLUE_SHEEP).build(), State.OPENED);
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
