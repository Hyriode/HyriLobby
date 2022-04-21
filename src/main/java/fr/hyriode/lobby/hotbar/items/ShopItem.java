package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.shop.ShopGui;
import fr.hyriode.lobby.hotbar.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopItem extends LobbyItem {

    public ShopItem(HyriLobby plugin) {
        super(plugin, "shop", "item.shop.name", "item.shop.lore", Material.EMERALD, 0);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new ShopGui(this.plugin, e.getPlayer()).open();
    }
}
