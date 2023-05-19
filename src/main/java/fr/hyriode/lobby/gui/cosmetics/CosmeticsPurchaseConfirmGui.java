package fr.hyriode.lobby.gui.cosmetics;

import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.CosmeticInfo;
import fr.hyriode.cosmetics.transaction.CosmeticPrice;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CosmeticsPurchaseConfirmGui extends LobbyGUI {

    private final CosmeticInfo cosmetic;
    private final ItemStack icon;

    private StorePrice price;
    protected List<StorePrice> prices = new ArrayList<>();

    public CosmeticsPurchaseConfirmGui(Player owner, HyriLobby plugin, CosmeticInfo cosmetic, ItemStack icon) {
        super(owner, plugin, name(owner, "gui.confirm.name"), 6 * 9);
        this.cosmetic = cosmetic;
        this.icon = icon;

        final int hyodesPrice = cosmetic.getPrice().getValue(CosmeticPrice.Currency.HYODES);
        final int hyrisPrice = cosmetic.getPrice().getValue(CosmeticPrice.Currency.HYRIS);

        if (hyodesPrice > 1) {
            prices.add(new StorePrice(StorePrice.Currency.HYODES, hyodesPrice));
        }
        if (hyrisPrice > 1) {
            prices.add(new StorePrice(StorePrice.Currency.HYRIS, hyrisPrice));
        }
        this.price = prices.get(0);
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        // Representative item
        this.setItem(13, this.icon);

        // Confirm
        this.setItem(this.prices.size() > 1 ? 29 : 30, new ItemBuilder(Material.STAINED_GLASS, 1, 5)
                .withName(LobbyMessage.CONFIRM_ITEM_CONFIRM_NAME.asString(this.account))
                .build(), event -> this.confirmEvent().accept(event, this.price));

        // Cancel
        this.setItem(this.prices.size() > 1 ? 33 : 32, new ItemBuilder(Material.STAINED_GLASS, 1, 14)
                .withName(LobbyMessage.CONFIRM_ITEM_CANCEL_NAME.asString(this.account))
                .build(), this.cancelEvent());


        if (this.prices.size() > 1) {
            this.addCurrencyItem();
        }
    }

    private void addCurrencyItem() {
        final List<String> lore = LobbyMessage.STORE_MONEY_EDIT_DESCRIPTION.asList(this.account);
        final List<String> values = new ArrayList<>();

        for (StorePrice price : this.prices) {
            final StorePrice.Currency currency = price.getCurrency();

            values.add(ChatColor.DARK_GRAY + Symbols.DOT_BOLD + " " + (this.price.getCurrency() == currency ? ChatColor.AQUA : ChatColor.GRAY) + ChatColor.stripColor(currency.getDisplay().getValue(this.account)));
        }

        lore.addAll(lore.indexOf("%currencies%"), values);
        lore.remove("%currencies%");

        // Money edit item
        this.setItem(31, new ItemBuilder(Material.GOLD_INGOT)
                        .withName(LobbyMessage.STORE_MONEY_EDIT_NAME.asString(this.account))
                        .withLore(lore)
                        .build(),
                event -> {
                    int nextIndex = this.prices.indexOf(this.price) + 1;

                    if (nextIndex >= this.prices.size()) {
                        nextIndex = 0;
                    }

                    this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                    this.price = this.prices.get(nextIndex);

                    this.addCurrencyItem();
                });
    }

    public BiConsumer<InventoryClickEvent, StorePrice> confirmEvent() {
        return (event, storePrice) -> {
            this.owner.closeInventory();

            if (!price.getCurrency().hasAmount(account, price.getAmount())) {
                this.owner.sendMessage(LobbyMessage.STORE_NOT_ENOUGH_MONEY_MESSAGE.asString(account).replace("%money%", price.getCurrency().getDisplay().getValue(account)));
                return;
            }

            price.getCurrency().removeAmount(account, price.getAmount());
            account.update();

            this.owner.sendMessage(LobbyMessage.STORE_PURCHASE_CONFIRMED_MESSAGE.asString(account));
            this.owner.playSound(this.owner.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);

            HyriCosmetics.get().getUserProvider().getUser(this.owner).addUnlockedCosmetic(cosmetic);
        };
    }

    public Consumer<InventoryClickEvent> cancelEvent() {
        return event -> {
            this.owner.closeInventory();
            this.owner.sendMessage(LobbyMessage.STORE_PURCHASE_CANCELLED_MESSAGE.asString(account));
        };
    }

    @Override
    public void open() {
        this.init();
        super.open();
    }
}
