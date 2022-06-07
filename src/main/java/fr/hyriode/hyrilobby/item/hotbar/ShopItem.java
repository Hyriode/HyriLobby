package fr.hyriode.hyrilobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopItem extends LobbyItem {


    public ShopItem(HyriLobby plugin) {
        super(plugin, "shop", "item.shop.name", Material.EMERALD, (byte) 0);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
       // new ShopGui(this.plugin, e.getPlayer()).open();
    }
}
