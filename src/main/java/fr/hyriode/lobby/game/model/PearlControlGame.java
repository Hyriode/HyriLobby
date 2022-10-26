package fr.hyriode.lobby.game.model;

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
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -48.5, 187, 9.5, -90, 0), UsefulSkin.PEARL_CONTROL).addEquipment(EnumItemSlot.MAIN_HAND, Material.ENDER_PEARL);
        this.hostCompatible = true;
    }

}
