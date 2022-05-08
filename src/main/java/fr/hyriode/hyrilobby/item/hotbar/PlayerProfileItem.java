package fr.hyriode.hyrilobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.profile.ProfileGui;
import fr.hyriode.hyrilobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerProfileItem extends LobbyItem {

    public PlayerProfileItem(HyriLobby plugin) {
        super(plugin, "player_profile", "item.profile.name", Material.SKULL_ITEM, (byte) 3);
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        return new ItemBuilder(itemStack).withPlayerHead(player.getUniqueId()).build();
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
     //   new ProfileGui(this.plugin, e.getPlayer()).open();
    }


}
