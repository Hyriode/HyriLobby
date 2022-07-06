package fr.hyriode.lobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.settings.SettingsGUI;
import fr.hyriode.lobby.item.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SettingsItem extends LobbyItem {

    private final HyriLobby plugin;

    public SettingsItem(HyriLobby plugin) {
        super(plugin, "settings", "item.settings.name", Material.REDSTONE_COMPARATOR, (byte) 0);
        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new SettingsGUI(this.plugin, e.getPlayer(), true).open();
    }
}
