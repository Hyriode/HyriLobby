package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

public class PlayerInfosItem extends HyriItem<HyriLobby> {

    public PlayerInfosItem(HyriLobby plugin) {
        super(plugin, "player_info", () -> plugin.getHyrame().getLanguageManager().getMessage("item.infos.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.infos.lore")), Material.SKULL_ITEM, (byte) 3);
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(player.getName());
        player.getInventory().getItem(slot).setItemMeta(meta);
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
