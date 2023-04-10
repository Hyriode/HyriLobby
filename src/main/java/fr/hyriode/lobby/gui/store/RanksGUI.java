package fr.hyriode.lobby.gui.store;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlus;
import fr.hyriode.api.player.transaction.HyriPlusTransaction;
import fr.hyriode.api.rank.PlayerRank;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.*;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by AstFaster
 * on 01/07/2022 at 19:24
 */
public class RanksGUI extends LobbyGUI {

    private static final StoreCategory CATEGORY = new StoreCategory(null, "ranks");

    static {
        final Function<UsefulHead, ItemStack> itemCreator = head -> ItemBuilder.asHead().withHeadTexture(head).build();
        final Function<PlayerRank, Predicate<IHyriPlayer>> owningCheck = rankType -> account -> account.getRank().isSuperior(rankType);
        final String categoryId = CATEGORY.getId();

        // Store items creation
        final StoreItem vip = new StoreItem(itemCreator.apply(UsefulHead.GOLD_CUBE), categoryId, PlayerRank.VIP.getName(),
                new StorePrice(StorePrice.Currency.HYRIS, 85000), new StorePrice(StorePrice.Currency.HYODES, 700))
                .withOwningCheck(owningCheck.apply(PlayerRank.VIP))
                .whenPurchased(account -> {
                    account.getTransactions().add("ranks", PlayerRank.VIP.getName(), new RankTransaction(PlayerRank.VIP));
                    account.getRank().setPlayerType(PlayerRank.VIP);
                });

        final StoreItem vipPlus = new StoreDependentItem(itemCreator.apply(UsefulHead.LIME_GREEN_CUBE), categoryId, PlayerRank.VIP_PLUS.getName(), vip,
                new StorePrice(StorePrice.Currency.HYODES, 1700))
                .withOwningCheck(owningCheck.apply(PlayerRank.VIP_PLUS))
                .whenPurchased(account -> {
                    account.getTransactions().add("ranks", PlayerRank.VIP_PLUS.getName(), new RankTransaction(PlayerRank.VIP_PLUS));
                    account.getRank().setPlayerType(PlayerRank.VIP_PLUS);
                });

        final StoreItem epic = new StoreDependentItem(itemCreator.apply(UsefulHead.DEEP_SKY_BLUE), categoryId, PlayerRank.EPIC.getName(), vipPlus,
                new StorePrice(StorePrice.Currency.HYODES, 4500))
                .withOwningCheck(owningCheck.apply(PlayerRank.EPIC))
                .whenPurchased(account -> {
                    account.getTransactions().add("ranks", PlayerRank.EPIC.getName(), new RankTransaction(PlayerRank.EPIC));
                    account.getRank().setPlayerType(PlayerRank.EPIC);
                });

        final StoreItem hyriPlus = new StoreDependentItem(itemCreator.apply(UsefulHead.GOLD_BLOCK), categoryId, HyriPlusTransaction.TRANSACTIONS_TYPE, epic,
                new StorePrice(StorePrice.Currency.HYODES, 800))
                .withOwningCheck(account -> account.getHyriPlus().has())
                .whenPurchased(account -> {
                    final IHyriPlus handle = account.getHyriPlus();
                    final boolean expired = handle.hasExpire();
                    final LocalDateTime start = LocalDateTime.now();
                    final long duration = start.until(start.plus(Period.parse("P1M")), ChronoUnit.SECONDS);

                    handle.setDuration(handle.getDuration() + duration);

                    if (expired) {
                        handle.enable();
                    }

                    account.getTransactions().add(HyriPlusTransaction.TRANSACTIONS_TYPE, new HyriPlusTransaction(duration));
                });


        // Add store items to their category
        CATEGORY.addContent(vip, vipPlus, epic, hyriPlus);
    }

    public RanksGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-ranks", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        final StoreItem vip = CATEGORY.getItem(PlayerRank.VIP.getName());
        final StoreItem vipPlus = CATEGORY.getItem(PlayerRank.VIP_PLUS.getName());
        final StoreItem epic = CATEGORY.getItem(PlayerRank.EPIC.getName());
        final StoreItem hyriPlus = CATEGORY.getItem(HyriPlusTransaction.TRANSACTIONS_TYPE);

        this.setItem(20, vip.createItem(this.account), event -> vip.purchase(this.plugin, this.owner));
        this.setItem(21, vipPlus.createItem(this.account), event -> vipPlus.purchase(this.plugin, this.owner));
        this.setItem(22, epic.createItem(this.account), event -> epic.purchase(this.plugin, this.owner));
        this.setItem(24, hyriPlus.createItem(this.account), event -> hyriPlus.purchase(this.plugin, this.owner));
    }

}
