package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.hotbar.utils.LobbyItem;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SettingsItem extends LobbyItem {

    private final HyriLobby plugin;

    public SettingsItem(HyriLobby plugin) {
        super(plugin, "settings", "item.settings.name", "item.settings.lore", Material.REDSTONE_COMPARATOR);
        this.plugin = plugin;
    }

    @Override
    public void onClick(IHyrame hyrame, PlayerInteractEvent e) {
        new SettingsGui(this.plugin, e.getPlayer()).open();
    }
}
