package fr.hyriode.lobby.hotbar;

import fr.hyriode.hyrame.item.IHyriItemManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.hotbar.items.*;
import org.bukkit.entity.Player;

public class HotbarManager {

    private final Player p;
    private final HyriLobby lobby;
    private final IHyriItemManager item;

    public HotbarManager(HyriLobby lobby, Player p) {
        this.p = p;
        this.lobby = lobby;
        this.item = this.lobby.getHyrame().getItemManager();
    }

    public void onLogin() {
        this.p.getInventory().clear();

        this.item.giveItem(this.p, 0, GameChooserItem.class);
        this.item.giveItem(this.p, 1, PlayerInfosItem.class);
        this.item.giveItem(this.p, 4, ShopItem.class);
        this.item.giveItem(this.p, 7, SettingsItem.class);
        this.item.giveItem(this.p, 8, LobbySelectorItem.class);
    }
}
