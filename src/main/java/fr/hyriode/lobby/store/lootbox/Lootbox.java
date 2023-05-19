package fr.hyriode.lobby.store.lootbox;

import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:34
 */
public enum Lootbox {

    ONE_STAR(new StorePrice(StorePrice.Currency.HYRIS, 3500L), new StorePrice(StorePrice.Currency.HYODES, 150L)),
    TWO_STARS(new StorePrice(StorePrice.Currency.HYRIS, 6500L), new StorePrice(StorePrice.Currency.HYODES, 200L)),
    THREE_STARS(new StorePrice(StorePrice.Currency.HYRIS, 9500L), new StorePrice(StorePrice.Currency.HYODES, 250L)),
    FOUR_STARS(new StorePrice(StorePrice.Currency.HYRIS, 13500L), new StorePrice(StorePrice.Currency.HYODES, 350L)),
    FIVE_STARS(new StorePrice(StorePrice.Currency.HYRIS, 17000L), new StorePrice(StorePrice.Currency.HYODES, 500L));

    private final HyriLootboxRarity rarity;
    private final StorePrice[] prices;

    Lootbox(StorePrice... prices) {
        this.rarity = HyriLootboxRarity.valueOf(this.name());
        this.prices = prices;
    }

    public HyriLootboxRarity getRarity() {
        return this.rarity;
    }

    public StorePrice[] getPrices() {
        return this.prices;
    }

    public String format() {
        final StringBuilder stars = new StringBuilder();

        for (int i = 1; i <= 5; i++) {
            stars.append(this.rarity.getId() < i ? ChatColor.GRAY : ChatColor.YELLOW).append(Symbols.STAR);
        }
        return stars.toString();
    }

}
