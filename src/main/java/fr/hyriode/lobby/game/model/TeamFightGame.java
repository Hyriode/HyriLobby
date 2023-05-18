package fr.hyriode.lobby.game.model;

import fr.hyriode.lobby.game.LobbyGame;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 17/05/2022 at 16:36
 */
public class TeamFightGame extends LobbyGame {

    public TeamFightGame() {
        super("teamfight", new ItemStack(Material.DIAMOND_SWORD), State.RANKED);
        this.hostCompatible = true;
    }

}
