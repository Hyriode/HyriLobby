package fr.hyriode.lobby.store;

import fr.hyriode.api.money.IHyriMoney;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.store.ConfirmPurchaseGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
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

    private HyriLanguageMessage name;
    private HyriLanguageMessage description;

    protected Predicate<IHyriPlayer> owningCheck = account -> account.getTransaction(this.getCategory(), this.id) != null;
    protected Consumer<IHyriPlayer> whenBought;

    protected final String category;
    protected final long price;

    public StoreItem(ItemStack itemStack, String category, String id, long price) {
        super(id, itemStack);
        this.category = category;
        this.price = price;
    }

    @Override
    public ItemStack createItem(IHyriPlayer account) {
        return this.createItem(account, true);
    }

    public ItemStack createItem(IHyriPlayer account, boolean purchaseLine) {
        final String description = this.getDescription().getForPlayer(account);
        final List<String> lore = new ArrayList<>(Arrays.asList(description.split("\n")));

        if (this.price >= 0) {
            lore.add("");
            lore.add(LobbyMessage.STORE_PRICE_LINE.asString(account).replace("%price%", NumberFormat.getInstance().format(this.price).replace(",", ".")));
        }

        if (purchaseLine) {
            lore.add("");
            lore.add(this.owningCheck.test(account) ? LobbyMessage.STORE_OWN_LINE.asString(account) : LobbyMessage.CLICK_TO_BUY.asString(account));
        }

        return new ItemBuilder(this.itemStack.clone())
                .withName(this.getName().getForPlayer(account))
                .withLore(lore)
                .withAllItemFlags()
                .build();
    }

    public void purchase(HyriLobby plugin, Player player) {
        if (this.price < 0) {
            return;
        }

        final IHyriPlayer account = IHyriPlayer.get(player.getUniqueId());

        if (this.owningCheck.test(account)) {
            player.sendMessage(LobbyMessage.STORE_ALREADY_OWN_MESSAGE.asString(account));
            return;
        }

        final IHyriMoney hyris = account.getHyris();

        if (hyris.getAmount() < this.price) {
            player.sendMessage(LobbyMessage.STORE_NOT_ENOUGH_MONEY_MESSAGE.asString(account));
            return;
        }

        new ConfirmPurchaseGUI(player, plugin, this)
                .whenConfirm(event -> {
                    player.closeInventory();
                    hyris.remove(this.price).exec();
                    player.sendMessage(LobbyMessage.STORE_PURCHASE_CONFIRMED_MESSAGE.asString(account));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 2.0F);

                    this.whenBought.accept(account);

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

    public long getPrice() {
        return this.price;
    }

    public StoreItem withOwningCheck(Predicate<IHyriPlayer> owningCheck) {
        this.owningCheck = owningCheck;
        return this;
    }

    public StoreItem whenBought(Consumer<IHyriPlayer> whenBought) {
        this.whenBought = whenBought;
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

}
