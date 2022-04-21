package fr.hyriode.lobby.hotbar;

import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.items.*;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class HotbarManager {

    private final Supplier<IHyriItemManager> item;

    public HotbarManager(HyriLobby plugin) {
        this.item = () -> plugin.getHyrame().getItemManager();
    }

    public void onLogin(Player player) {
        player.getInventory().clear();

        this.item.get().giveItem(player, 0, GameChooserItem.class);
        this.item.get().giveItem(player, 1, PlayerProfileItem.class);
        this.item.get().giveItem(player, 4, ShopItem.class);
        this.item.get().giveItem(player, 7, SettingsItem.class);
        this.item.get().giveItem(player, 8, LobbySelectorItem.class);

        player.getInventory().setHeldItemSlot(0);
    }
}
