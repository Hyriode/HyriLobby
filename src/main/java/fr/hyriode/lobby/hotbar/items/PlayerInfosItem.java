package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerInfosItem extends LobbyItem {

    public PlayerInfosItem(HyriLobby plugin) {
        super(plugin, "player_info", "item.infos.name", "item.infos.lore", Material.SKULL_ITEM, (byte) 3);
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(player.getName());
        player.getInventory().getItem(slot).setItemMeta(meta);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        e.getPlayer().sendMessage("Triggered " + this.name);
    }
}
