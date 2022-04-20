package fr.hyriode.lobby.hotbar.utils;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public abstract class LobbyItem extends HyriItem<HyriLobby> {

    public LobbyItem(HyriLobby plugin, String name, String displayName, String description, Material material, int data) {
        super(plugin, name, () -> plugin.getHyrame().getLanguageManager().getMessage(displayName), () -> Collections.singletonList(plugin.getHyrame().getLanguageManager().getMessage(description)), material, (byte) data);
    }

    @Override
    public void onLeftClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(hyrame, event);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(hyrame, event);
    }

    public abstract void onClick(IHyrame hyrame, PlayerInteractEvent event);
}
