package fr.hyriode.lobby.gui.profile;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import org.bukkit.entity.Player;

public class LootboxesGUI extends LobbyGUI {
    public LootboxesGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-lootboxes", 54);
    }

    @Override
    protected void init() {

    }
}
