package fr.hyriode.lobby.hotbar;

import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.items.*;
import org.bukkit.entity.Player;

public class HotbarManager {

    private final IHyriItemManager item;

    public HotbarManager(HyriLobby lobby) {
        this.item = lobby.getHyrame().getItemManager();
    }

    public void onLogin(Player player) {
        player.getInventory().clear();

        this.item.giveItem(player, 0, GameChooserItem.class);
        this.item.giveItem(player, 1, PlayerInfosItem.class);
        this.item.giveItem(player, 4, ShopItem.class);
        this.item.giveItem(player, 7, SettingsItem.class);
        this.item.giveItem(player, 8, LobbySelectorItem.class);
    }
}
