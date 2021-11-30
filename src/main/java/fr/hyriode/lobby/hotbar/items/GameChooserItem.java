package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class GameChooserItem extends HyriItem<HyriLobby> {

    public GameChooserItem(HyriLobby plugin) {
        super(plugin, "game_chooser", () -> plugin.getHyrame().getLanguageManager().getMessage("item.chooser.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.chooser.lore")), Material.COMPASS);
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
        e.getPlayer().sendMessage("Triggered " + this.name);
    }
}
