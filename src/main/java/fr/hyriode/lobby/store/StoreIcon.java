package fr.hyriode.lobby.store;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 02/07/2022 at 16:45
 */
public abstract class StoreIcon {

    protected final String id;
    protected final ItemStack itemStack;

    public StoreIcon(String id, ItemStack itemStack) {
        this.id = id;
        this.itemStack = itemStack;
    }

    public abstract ItemStack createItem(IHyriPlayer account);

    public String getId() {
        return this.id;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public abstract HyriLanguageMessage getName();

    public abstract HyriLanguageMessage getDescription();

}
