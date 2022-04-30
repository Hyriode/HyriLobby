package fr.hyriode.hyrilobby.item.hotbar;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.settings.SettingsGui;
import fr.hyriode.hyrilobby.item.LobbyItem;
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
        new SettingsGui(this.plugin, e.getPlayer()).open();
    }
}
