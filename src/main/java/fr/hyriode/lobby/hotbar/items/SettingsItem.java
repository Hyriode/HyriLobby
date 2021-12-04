package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class SettingsItem extends HyriItem<HyriLobby> {

    private final HyriLobby plugin;

    public SettingsItem(HyriLobby plugin) {
        super(plugin, "settings", () -> plugin.getHyrame().getLanguageManager().getMessage("item.settings.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.settings.lore")), Material.REDSTONE_COMPARATOR);
        this.plugin = plugin;
    }

    @Override
    public void onLeftClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(event);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(event);
    }

    public void onClick(PlayerInteractEvent event) {
        new SettingsGui(this.plugin, event.getPlayer()).open();
    }
}
