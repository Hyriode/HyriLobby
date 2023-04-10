package fr.hyriode.lobby.store;

import fr.hyriode.api.language.HyriLanguageMessage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.store.StoreConfirmGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by AstFaster
 * on 02/07/2022 at 16:40
 */
public class StoreItem extends StoreIcon {

    protected HyriLanguageMessage name;
    protected HyriLanguageMessage description;

    protected Predicate<IHyriPlayer> owningCheck = account -> account.getTransactions().get(this.getCategory(), this.id) != null;
    protected Consumer<IHyriPlayer> whenPurchased;

    protected final String category;
    protected StorePrice[] prices;

    public StoreItem(ItemStack itemStack, String category, String id, StorePrice... prices) {
        super(id, itemStack);
        this.category = category;
        this.prices = prices;
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return this.createItem(account, true);
    }

    public ItemStack createItem(IHyriPlayer account, boolean purchaseLine) {
        final String description = this.getDescription().getValue(account);
        final List<String> lore = new ArrayList<>(Arrays.asList(description.split("\n")));

        if (this.prices.length > 0) {
            lore.add("");

            final StringBuilder prices = new StringBuilder();

            for (int i = 0; i < this.prices.length; i++) {
                final StorePrice price = this.prices[i];

                prices.append(price.getCurrency().formatAmount(price.getAmount())).append(i + 1 == this.prices.length ? "" : ChatColor.GRAY + " / ");
            }

            lore.add(LobbyMessage.STORE_PRICE_LINE.asString(account).replace("%price%", prices));
        }

        if (purchaseLine) {
            lore.add("");
            lore.add(this.owningCheck.test(account) ? LobbyMessage.STORE_OWN_LINE.asString(account) : LobbyMessage.CLICK_TO_BUY.asString(account));
        }

        return new ItemBuilder(this.itemStack.clone())
                .withName(this.getName().getValue(account))
                .withLore(lore)
                .withAllItemFlags()
                .build();
    }

    public void purchase(HyriLobby plugin, Player player) {
        final IHyriPlayer account = IHyriPlayer.get(player.getUniqueId());

        if (this.owningCheck.test(account)) {
            player.sendMessage(LobbyMessage.STORE_ALREADY_OWN_MESSAGE.asString(account));
            return;
        }

        new StoreConfirmGUI(player, plugin, this.createItem(account, false), this)
                .whenConfirm((event, price) -> {
                    player.closeInventory();

                    if (!price.getCurrency().hasAmount(account, price.getAmount())) {
                        player.sendMessage(LobbyMessage.STORE_NOT_ENOUGH_MONEY_MESSAGE.asString(account).replace("%money%", price.getCurrency().getDisplay().getValue(account)));
                        return;
                    }

                    price.getCurrency().removeAmount(account, price.getAmount());

                    player.sendMessage(LobbyMessage.STORE_PURCHASE_CONFIRMED_MESSAGE.asString(account));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);

                    if (this.whenPurchased != null) {
                        this.whenPurchased.accept(account);
                    }

                    account.update();
                })
                .whenCancel(event -> {
                    player.closeInventory();
                    player.sendMessage(LobbyMessage.STORE_PURCHASE_CANCELLED_MESSAGE.asString(account));
                })
                .open();
    }

    public String getCategory() {
        return this.category;
    }

    public StoreItem withOwningCheck(Predicate<IHyriPlayer> owningCheck) {
        this.owningCheck = owningCheck;
        return this;
    }

    public StoreItem whenPurchased(Consumer<IHyriPlayer> whenPurchased) {
        this.whenPurchased = whenPurchased;
        return this;
    }

    @Override
    public HyriLanguageMessage getName() {
        return this.name == null ? this.name = HyriLanguageMessage.get("store.item." + this.category + "." + this.id + ".name") : this.name;
    }

    @Override
    public HyriLanguageMessage getDescription() {
        return this.description == null ? this.description = HyriLanguageMessage.get("store.item." + this.category + "." + this.id + ".description") : this.description;
    }

    public StorePrice[] getPrices() {
        return this.prices;
    }

}
