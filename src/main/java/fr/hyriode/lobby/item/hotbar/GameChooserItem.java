package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.chooser.GamesChooserGui;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameChooserItem extends LobbyItem {

    private final HyriLobby plugin;

    public GameChooserItem(HyriLobby plugin) {
        super(plugin, "main_menu", "item.main-menu.name", Material.COMPASS, (byte) 0);

        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new GamesChooserGui(this.plugin, e.getPlayer()).open();
    }
}
