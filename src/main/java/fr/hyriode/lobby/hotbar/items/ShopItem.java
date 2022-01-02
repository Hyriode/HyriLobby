package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class ShopItem extends HyriItem<HyriLobby> {

    public ShopItem(HyriLobby plugin) {
        super(plugin, "shop", () -> plugin.getHyrame().getLanguageManager().getMessage("item.shop.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.shop.lore")), Material.EMERALD);
    }

    @Override
    public void onLeftClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(event);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(event);
    }

    public void onClick(PlayerInteractEvent e) {
        e.getPlayer().sendMessage("Triggered " + this.name);
    }
}
