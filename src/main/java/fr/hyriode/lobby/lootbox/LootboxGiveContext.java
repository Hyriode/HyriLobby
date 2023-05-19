package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.player.IHyriPlayer;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 19/05/2023 at 15:51
 */
public class LootboxGiveContext {

    private final IHyriPlayer player;
    private ItemStack floatingItem;
    private String floatingText;

    private boolean message;

    public LootboxGiveContext(IHyriPlayer player, ItemStack floatingItem, String floatingText, boolean message) {
        this.player = player;
        this.floatingItem = floatingItem;
        this.floatingText = floatingText;
        this.message = message;
    }

    public IHyriPlayer getPlayer() {
        return this.player;
    }

    public ItemStack getFloatingItem() {
        return this.floatingItem;
    }

    public void setFloatingItem(ItemStack floatingItem) {
        this.floatingItem = floatingItem;
    }

    public String getFloatingText() {
        return this.floatingText;
    }

    public void setFloatingText(String floatingText) {
        this.floatingText = floatingText;
    }

    public boolean isMessage() {
        return this.message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

}
