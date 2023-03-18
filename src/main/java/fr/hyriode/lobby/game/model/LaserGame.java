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
public class LaserGame extends LobbyGame {

    public LaserGame() {
        super("lasergame", Material.IRON_HOE, State.OPENED);
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -48.5, 187, -8.5, -90, 0), UsefulSkin.LASER_GAME).addEquipment(EnumItemSlot.MAIN_HAND, Material.IRON_HOE);
    }

}
