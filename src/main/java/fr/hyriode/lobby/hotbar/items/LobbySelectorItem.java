package fr.hyriode.lobby.hotbar.items;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class LobbySelectorItem extends HyriItem<HyriLobby> {

    public LobbySelectorItem(HyriLobby plugin) {
        super(plugin, "lobby_selector", () -> plugin.getHyrame().getLanguageManager().getMessage("item.lobbySelector.name"),
                () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage("item.lobbySelector.lore")), Material.NETHER_STAR);
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
