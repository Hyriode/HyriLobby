package fr.hyriode.lobby.item;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class LobbyItem extends HyriItem<HyriLobby> {

    public LobbyItem(HyriLobby plugin, String name, String key, Material material, byte data) {
        super(plugin, name, () -> HyriLanguageMessage.get(key), material, data);
    }

    @Override
    public void onLeftClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(hyrame, event);
    }

    @Override
    public void onRightClick(IHyrame hyrame, PlayerInteractEvent event) {
        this.onClick(hyrame, event);
    }

    public void onClick(IHyrame hyrame, PlayerInteractEvent event) {}

}