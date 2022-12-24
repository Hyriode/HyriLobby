package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.store.StoreGUI;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class StoreItem extends LobbyItem {

    public StoreItem(HyriLobby plugin) {
        super(plugin, "store", "item.store.name", "item.store.description", Material.EMERALD);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent event) {
        new StoreGUI(event.getPlayer(), this.plugin).open();
    }

}
