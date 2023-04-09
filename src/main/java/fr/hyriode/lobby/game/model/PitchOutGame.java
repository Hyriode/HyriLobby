package fr.hyriode.lobby.game.model;

import fr.hyriode.lobby.game.LobbyGame;
import org.bukkit.Material;

public class PitchOutGame extends LobbyGame {

    public PitchOutGame() {
        super("pitchout", Material.SNOW_BALL, State.OPENED);
        this.usedInSelector = false;
    }

}
