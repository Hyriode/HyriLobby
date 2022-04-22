package fr.hyriode.lobby.items.utils;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.utils.Language;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public abstract class LobbyItem extends HyriItem<HyriLobby> {

    public LobbyItem(HyriLobby plugin, String name, String displayName, String description, Material material, int data) {
        super(plugin, name, () -> Language.getMessage(displayName), () -> Collections.singletonList(Language.getMessage(description)), material, (byte) data);
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
