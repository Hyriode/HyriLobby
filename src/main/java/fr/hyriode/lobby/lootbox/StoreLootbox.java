package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.store.StoreItem;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 22/10/2022 at 15:08
 */
public class StoreLootbox extends StoreItem {

    private final Lootbox handle;

    public StoreLootbox(String category, Lootbox handle) {
        super(ItemBuilder.asHead().withHeadTexture(UsefulHead.ENDER_CHEST).build(), category, handle.name().toLowerCase().replace("_", "-"), handle.getPrices());
        this.handle = handle;
        this.name = HyriLanguageMessage.get("store.item.lootbox.name");
        this.description = HyriLanguageMessage.get("store.item.lootbox.description");
    }

    @Override
    public ItemStack createItem(IHyriPlayer account, boolean purchaseLine) {
        final ItemBuilder builder = new ItemBuilder(super.createItem(account, purchaseLine));

        return builder.withName(builder.getName().replace("%stars%", this.formatStars())).build();
    }

    private String formatStars() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 5; i++) {
            builder.append(this.handle.getRarity().getId() < i ? ChatColor.GRAY : ChatColor.YELLOW).append(Symbols.STAR);
        }
        return builder.toString();
    }

}
