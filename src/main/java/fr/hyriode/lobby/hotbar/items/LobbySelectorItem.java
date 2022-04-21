package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.hotbar.LobbySelectorGui;
import fr.hyriode.lobby.hotbar.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbySelectorItem extends LobbyItem {

    public LobbySelectorItem(HyriLobby plugin) {
        super(plugin, "lobby_selector", "item.lobby_selector.name", "item.lobby_selector.lore", Material.NETHER_STAR, 0);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new LobbySelectorGui(this.plugin, e.getPlayer()).open();
    }
}
