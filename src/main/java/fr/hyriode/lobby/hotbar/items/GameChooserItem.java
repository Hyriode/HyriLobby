package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.GameChooserGui;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class GameChooserItem extends HyriItem<HyriLobby> {

    private final HyriLobby plugin;

    public GameChooserItem(HyriLobby plugin) {
        super(plugin, "game_chooser", () -> plugin.getHyrame().getLanguageManager().getMessage("item.chooser.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.chooser.lore")), Material.COMPASS);

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

    public void onClick(PlayerInteractEvent e) {
        new GameChooserGui(this.plugin, e.getPlayer()).open();
    }
}
