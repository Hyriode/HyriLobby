package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.selector.LobbySelectorGUI;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbySelectorItem extends LobbyItem {

    public LobbySelectorItem(HyriLobby plugin) {
        super(plugin, "lobby_selector", "item.lobby-selector.name", Material.NETHER_STAR);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new LobbySelectorGUI(this.plugin, e.getPlayer()).open();
    }

}
