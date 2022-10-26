package fr.hyriode.lobby.gui.store;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 01/07/2022 at 19:24
 */
public class CosmeticsGUI extends LobbyGUI {

    public CosmeticsGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-cosmetics", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);
    }

}
