package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.selector.game.GameSelectorGUI;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameSelectorItem extends LobbyItem {

    private final HyriLobby plugin;

    public GameSelectorItem(HyriLobby plugin) {
        super(plugin, "main_menu", "item.main-menu.name", "item.main-menu.description", Material.COMPASS);

        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new GameSelectorGUI(this.plugin, e.getPlayer()).open();
    }
}
