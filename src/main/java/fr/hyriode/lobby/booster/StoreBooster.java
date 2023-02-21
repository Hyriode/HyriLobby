package fr.hyriode.lobby.booster;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.booster.IHyriBoosterManager;
import fr.hyriode.api.player.IHyriPlayer;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.hyrame.utils.list.ListReplacer;
import fr.hyriode.lobby.store.StoreItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AstFaster
 * on 20/10/2022 at 07:16
 */
public class StoreBooster extends StoreItem {

    public enum Type {

        ONE_FIVE(1.5D, 15000, 8000), // +50%%
        TWO(2.0D, 20000, 12000), // +100%
        TWO_FIVE(2.5D, 25000, 17000), // +150%
        THREE(3.0D, 30000, 22000) // +200%
        ;

        private final double multiplier;
        private final long globalPrice;
        private final long selectablePrice;

        Type(double multiplier, long globalPrice, long selectablePrice) {
            this.multiplier = multiplier;
            this.globalPrice = globalPrice;
            this.selectablePrice = selectablePrice;
        }

        public String format() {
            return "+" + ((int) (this.multiplier * 100 - 100)) + "%";
        }

        public double getMultiplier() {
            return this.multiplier;
        }

        public long getGlobalPrice() {
            return this.globalPrice;
        }

        public long getSelectablePrice() {
            return this.selectablePrice;
        }

    }

    private Type type;
    private boolean global = false;

    public StoreBooster(String category) {
        super(new ItemStack(Material.POTION), category, "booster", -1);
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
                .replace("%global%", this.global ? ChatColor.GREEN + Symbols.TICK_BOLD : ChatColor.RED + Symbols.CROSS_STYLIZED_BOLD)
                .list());

        return builder.build();
    }

    private void updatePrice() {
        this.price = this.global ? this.type.getGlobalPrice() : this.type.getSelectablePrice();
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;

        this.updatePrice();
    }

    public boolean isGlobal() {
        return this.global;
    }

    public void setGlobal(boolean global) {
        this.global = global;

        this.updatePrice();
    }

}
