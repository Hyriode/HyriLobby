package fr.hyriode.hyrilobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.selector.LobbySelectorGui;
import fr.hyriode.hyrilobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class LobbySelectorItem extends LobbyItem {

    public LobbySelectorItem(HyriLobby plugin) {
        super(plugin, "lobby_selector", "item.lobby-selector.name", Material.NETHER_STAR, (byte) 0);
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new LobbySelectorGui(this.plugin, e.getPlayer()).open();
    }

}
