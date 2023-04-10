package fr.hyriode.lobby.gui.store;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.booster.StoreBooster;
import fr.hyriode.lobby.gui.ConfirmGUI;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StoreItem;
import fr.hyriode.lobby.store.StorePrice;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 23/10/2022 at 12:56
 */
public class StoreConfirmGUI extends LobbyGUI {

    private StorePrice price;

    private final StoreItem storeItem;
    private final ItemStack icon;

    private BiConsumer<InventoryClickEvent, StorePrice> whenConfirm;
    private Consumer<InventoryClickEvent> whenCancel;

    public StoreConfirmGUI(Player owner, HyriLobby plugin, ItemStack icon, StoreItem storeItem) {
        super(owner, plugin, () -> "confirm", 6 * 9);
        this.storeItem = storeItem;
        this.icon = icon;
        this.price = storeItem.getPrices()[0];
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        // Representative item
        this.setItem(13, this.icon);

        // Confirm
        this.setItem(this.storeItem.getPrices().length > 1 ? 29 : 30, new ItemBuilder(Material.STAINED_GLASS, 1, 5)
                .withName(LobbyMessage.CONFIRM_ITEM_CONFIRM_NAME.asString(this.account))
                .build(), event -> this.whenConfirm.accept(event, this.price));

        // Cancel
        this.setItem(this.storeItem.getPrices().length > 1 ? 33 : 32, new ItemBuilder(Material.STAINED_GLASS, 1, 14)
                .withName(LobbyMessage.CONFIRM_ITEM_CANCEL_NAME.asString(this.account))
                .build(), this.whenCancel);


        if (this.storeItem.getPrices().length > 1) {
            this.addCurrencyItem();
        }
    }

    private void addCurrencyItem() {
        final List<String> lore = LobbyMessage.STORE_MONEY_EDIT_DESCRIPTION.asList(this.account);
        final List<String> values = new ArrayList<>();

        for (StorePrice price : this.storeItem.getPrices()) {
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
                    final List<StorePrice> prices = Arrays.asList(this.storeItem.getPrices());

                    int nextIndex = prices.indexOf(this.price) + 1;

                    if (nextIndex >= prices.size()) {
                        nextIndex = 0;
                    }

                    this.owner.playSound(this.owner.getLocation(), Sound.CLICK, 0.5F, 2.0F);
                    this.price = prices.get(nextIndex);

                    this.addCurrencyItem();
                });
    }

    public StoreConfirmGUI whenConfirm(BiConsumer<InventoryClickEvent, StorePrice> whenConfirm) {
        this.whenConfirm = whenConfirm;
        return this;
    }

    public StoreConfirmGUI whenCancel(Consumer<InventoryClickEvent> whenCancel) {
        this.whenCancel = whenCancel;
        return this;
    }

    @Override
    public void open() {
        this.init();
        super.open();
    }

}
