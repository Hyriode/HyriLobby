package fr.hyriode.lobby.store;

import fr.hyriode.api.player.IHyriPlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 02/07/2022 at 17:22
 */
public class StoreDependentItem extends StoreItem {

    private final StoreItem depends;

    public StoreDependentItem(ItemStack itemStack, String category, String id, long price, StoreItem depends) {
        super(itemStack, category, id, price);
        this.depends = depends;
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return super.createItem(account);
    }

    public StoreItem getDepends() {
        return this.depends;
    }

}
