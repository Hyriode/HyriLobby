package fr.hyriode.lobby.game.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Created by AstFaster
 * on 26/06/2022 at 16:19
 */
public class TheRunnerGame extends LobbyGame {

    public TheRunnerGame() {
        super("therunner", Material.DIAMOND_BOOTS, State.OPENED);
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -47.5, 187, 5.5, -90, 0), UsefulSkin.THE_RUNNER).addEquipment(EnumItemSlot.MAIN_HAND, Material.DIAMOND_BOOTS);
    }

}
