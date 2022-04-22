package fr.hyriode.lobby.items.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.profile.ProfileGui;
import fr.hyriode.lobby.items.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerProfileItem extends LobbyItem {

    public PlayerProfileItem(HyriLobby plugin) {
        super(plugin, "player_profile", "item.profile.name", "item.profile.lore", Material.SKULL_ITEM, (byte) 3);
    }

    @Override
    public void onGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        final SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(player.getName());
        player.getInventory().getItem(slot).setItemMeta(meta);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new ProfileGui(this.plugin, e.getPlayer()).open();
    }
}
