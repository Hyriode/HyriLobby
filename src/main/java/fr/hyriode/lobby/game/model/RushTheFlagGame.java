package fr.hyriode.lobby.game.model;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.reflection.entity.EnumItemSlot;
import fr.hyriode.lobby.game.LobbyGame;
import fr.hyriode.lobby.util.UsefulSkin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 26/06/2022 at 16:14
 */
public class RushTheFlagGame extends LobbyGame {

    private static final ItemStack ICON = new ItemBuilder(Material.BANNER, 1, 15).build();

    public RushTheFlagGame() {
        super("rushtheflag", ICON, State.OPENED);
        this.npcData = new NPCData(new Location(IHyrame.WORLD.get(), -45.5, 187, -1.5, -90, 0), UsefulSkin.RUSH_THE_FLAG).addEquipment(EnumItemSlot.MAIN_HAND, ICON);
    }

}
