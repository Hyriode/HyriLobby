package fr.hyriode.lobby.items.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.GamesChooserGui;
import fr.hyriode.lobby.items.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameChooserItem extends LobbyItem {

    private final HyriLobby plugin;

    public GameChooserItem(HyriLobby plugin) {
        super(plugin, "games_selector", "item.games_selector.name", "item.games_selector.lore", Material.COMPASS, 0);

        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new GamesChooserGui(this.plugin, e.getPlayer()).open();
    }
}
