package fr.hyriode.lobby.lootbox;

import fr.hyriode.api.lootbox.HyriLootboxRarity;

/**
 * Created by AstFaster
 * on 19/10/2022 at 15:34
 */
public enum Lootbox {

    ONE_STAR(1500L),
    TWO_STARS(3500L),
    THREE_STARS(5500L),
    FOUR_STARS(8000L),
    FIVE_STARS(10000L);

    private final HyriLootboxRarity rarity;
    private final long price;

    Lootbox(long price) {
        this.rarity = HyriLootboxRarity.valueOf(this.name());
        this.price = price;
    }

    public HyriLootboxRarity getRarity() {
        return this.rarity;
    }

    public long getPrice() {
        return this.price;
    }

}
