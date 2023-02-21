package fr.hyriode.lobby.item;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.item.HyriItem;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public abstract class LobbyItem extends HyriItem<HyriLobby> {

    public LobbyItem(HyriLobby plugin, String name, String displayKey, String descriptionKey, Material material) {
        super(plugin, name, () -> HyriLanguageMessage.get(displayKey), () -> HyriLanguageMessage.get(descriptionKey), material);
    }

    public LobbyItem(HyriLobby plugin, String name, String displayKey, String descriptionKey, ItemStack item) {
        super(plugin, name, () -> HyriLanguageMessage.get(displayKey), () -> HyriLanguageMessage.get(descriptionKey), item);
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