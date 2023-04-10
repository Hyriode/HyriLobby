package fr.hyriode.lobby.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.store.StoreItem;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 20/10/2022 at 07:16
 */
public class StoreBooster extends StoreItem {

    public enum Type {

        ONE_FIVE(1.5D, new StorePrice(StorePrice.Currency.HYRIS, 15000), new StorePrice(StorePrice.Currency.HYODES, 300)), // +50%%
        TWO(2.0D, new StorePrice(StorePrice.Currency.HYRIS, 20000), new StorePrice(StorePrice.Currency.HYODES, 450)), // +100%
        TWO_FIVE(2.5D, new StorePrice(StorePrice.Currency.HYODES, 600)), // +150%
        THREE(3.0D, new StorePrice(StorePrice.Currency.HYODES, 750)) // +200%

        ;

        private final double multiplier;
        private final StorePrice[] prices;

        Type(double multiplier, StorePrice... prices) {
            this.multiplier = multiplier;
            this.prices = prices;
        }

        public String format() {
            return "+" + ((int) (this.multiplier * 100 - 100)) + "%";
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        public StorePrice[] getPrices() {
            return this.prices;
        }

    }

    private Type type;

    public StoreBooster(String category) {
        super(new ItemStack(Material.POTION), category, "booster");
        this.type = Type.ONE_FIVE;
        this.whenPurchased = player -> HyriAPI.get().getBoosterManager().giveBooster(player, this.type.getMultiplier(), 3600);

        this.updatePrice();
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return this.createItem(account, true);
    }

    @Override
    public ItemStack createItem(IHyriPlayer account, boolean purchaseLine) {
        final ItemBuilder builder = new ItemBuilder(super.createItem(account, purchaseLine));

        builder.withLore(ListReplacer.replace(builder.getLore(), "%boost%", this.type.format())
                .replace("%multiplier%", "x" + this.type.getMultiplier())
                .list());

        return builder.build();
    }

    private void updatePrice() {
        this.prices = this.type.getPrices();
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;

        this.updatePrice();
    }

}
