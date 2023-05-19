package fr.hyriode.lobby.ui.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.ui.gui.settings.SettingsGUI;
import fr.hyriode.lobby.ui.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SettingsItem extends LobbyItem {

    private final HyriLobby plugin;

    public SettingsItem(HyriLobby plugin) {
        super(plugin, "settings", "item.settings.name", Material.REDSTONE_COMPARATOR);
        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new SettingsGUI(this.plugin, e.getPlayer(), true).open();
    }
}
