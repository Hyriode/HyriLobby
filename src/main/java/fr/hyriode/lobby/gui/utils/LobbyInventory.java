package fr.hyriode.lobby.gui.utils;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class LobbyInventory extends HyriInventory {

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build();

    protected final String baseKey;
    protected final IHyriLanguageManager langManager;

    public LobbyInventory(Player owner, IHyrame hyrame, String baseKey, String title, int size) {
        super(owner, hyrame.getLanguageManager().getValue(owner, title), size);

        this.baseKey = baseKey;
        this.langManager = hyrame.getLanguageManager();
    }

    protected abstract void init();

    protected String getKey(String key) {
        return this.langManager.getValue(this.owner, this.baseKey + key);
    };
}
