package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.profile.ProfileGUI;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerProfileItem extends LobbyItem {

    public PlayerProfileItem(HyriLobby plugin) {
        super(plugin, "player_profile", "item.profile.name", ItemBuilder.asHead().build());
    }

    @Override
    public ItemStack onPreGive(IHyrame hyrame, Player player, int slot, ItemStack itemStack) {
        return new ItemBuilder(itemStack).withPlayerHead(player.getUniqueId()).build();
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new ProfileGUI(this.plugin, e.getPlayer()).open();
    }


}
