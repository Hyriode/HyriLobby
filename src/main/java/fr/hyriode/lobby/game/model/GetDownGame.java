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
public class GetDownGame extends LobbyGame {

    public GetDownGame() {
        super("getdown", Material.SEA_LANTERN, State.OPENED);
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -48.5, 187, 9.5, -90, 0), UsefulSkin.GET_DOWN).addEquipment(EnumItemSlot.MAIN_HAND, Material.SEA_LANTERN);
    }



}
