package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.lootbox.HyriLootboxRarity;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:34
 */
public enum Lootbox {

    ONE_STAR(new StorePrice(StorePrice.Currency.HYRIS, 1500L), new StorePrice(StorePrice.Currency.HYODES, 450)),
    TWO_STARS(new StorePrice(StorePrice.Currency.HYRIS, 3500L), new StorePrice(StorePrice.Currency.HYODES, 550)),
    THREE_STARS(new StorePrice(StorePrice.Currency.HYRIS, 5500L), new StorePrice(StorePrice.Currency.HYODES, 700)),
    FOUR_STARS(new StorePrice(StorePrice.Currency.HYRIS, 8000L), new StorePrice(StorePrice.Currency.HYODES, 850)),
    FIVE_STARS(new StorePrice(StorePrice.Currency.HYRIS, 10000L), new StorePrice(StorePrice.Currency.HYODES, 1000));

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
