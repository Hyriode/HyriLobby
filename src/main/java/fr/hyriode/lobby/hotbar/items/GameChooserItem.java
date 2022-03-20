package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.chooser.DefaultChooserGui;
import fr.hyriode.lobby.hotbar.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameChooserItem extends LobbyItem {

    private final HyriLobby plugin;

    public GameChooserItem(HyriLobby plugin) {
        super(plugin, "game_chooser", "item.chooser.name", "item.chooser.lore", Material.COMPASS);

        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new DefaultChooserGui(this.plugin, e.getPlayer()).open();
    }
}
