package fr.hyriode.lobby.game.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Created by AstFaster
 * on 26/06/2022 at 11:44
 */
public class BedWarsGame extends LobbyGame {

    public BedWarsGame() {
        super("bedwars", Material.BED, State.OPENED);
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -45.5, 187, 2.5, -90, 0), UsefulSkin.BED_WARS).addEquipment(EnumItemSlot.MAIN_HAND, Material.BED);
        this.hostCompatible = true;
    }

}
