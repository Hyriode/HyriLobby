package fr.hyriode.lobby.gui.store;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 01/07/2022 at 19:24
 */
public class GamesGUI extends LobbyGUI {

    public GamesGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-games", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.border();
    }

}
