package fr.hyriode.lobby.gui.store;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.HyriPlus;
import fr.hyriode.api.rank.type.HyriPlayerRankType;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StoreCategory;
import fr.hyriode.lobby.store.StoreDependentItem;
import fr.hyriode.lobby.store.StoreItem;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        final Function<HyriPlayerRankType, Predicate<IHyriPlayer>> owningCheck = rankType -> account -> account.getRank().isSuperior(rankType);
        final String categoryId = CATEGORY.getId();
        final StoreItem vip = new StoreItem(itemCreator.apply(UsefulHead.GOLD_CUBE), categoryId, HyriPlayerRankType.VIP.getName(), 85000)
                .withOwningCheck(owningCheck.apply(HyriPlayerRankType.VIP))
                .whenBought(account -> {
                    account.addTransaction("ranks", HyriPlayerRankType.VIP.getName(), null);
                    account.setPlayerRank(HyriPlayerRankType.VIP);
                });

        final StoreItem vipPlus = new StoreDependentItem(itemCreator.apply(UsefulHead.LIME_GREEN_CUBE), categoryId, HyriPlayerRankType.VIP_PLUS.getName(), -1, vip).withOwningCheck(owningCheck.apply(HyriPlayerRankType.VIP_PLUS));
        final StoreItem epic = new StoreDependentItem(itemCreator.apply(UsefulHead.DEEP_SKY_BLUE), categoryId, HyriPlayerRankType.EPIC.getName(), -1, vipPlus).withOwningCheck(owningCheck.apply(HyriPlayerRankType.EPIC));
        final StoreItem hyriPlus = new StoreDependentItem(itemCreator.apply(UsefulHead.GOLD_BLOCK), categoryId, HyriPlus.TRANSACTION_TYPE, -1, epic).withOwningCheck(IHyriPlayer::hasHyriPlus);

        CATEGORY.addContent(vip, vipPlus, epic, hyriPlus);
    }

    public RanksGUI(Player owner, HyriLobby plugin) {
        super(owner, plugin, () -> "store-ranks", 54);

        this.init();
    }

    @Override
    protected void init() {
        this.border();

        final Consumer<InventoryClickEvent> onlyOnWebsite = event -> this.owner.sendMessage(LobbyMessage.STORE_ONLY_ON_WEBSITE_MESSAGE.asString(this.account));
        final StoreItem vip = CATEGORY.getItem(HyriPlayerRankType.VIP.getName());
        final StoreItem vipPlus = CATEGORY.getItem(HyriPlayerRankType.VIP_PLUS.getName());
        final StoreItem epic = CATEGORY.getItem(HyriPlayerRankType.EPIC.getName());
        final StoreItem hyriPlus = CATEGORY.getItem(HyriPlus.TRANSACTION_TYPE);

        this.setItem(20, vip.createItem(this.account), event -> vip.purchase(this.plugin, this.owner));
        this.setItem(21, vipPlus.createItem(this.account), onlyOnWebsite);
        this.setItem(22, epic.createItem(this.account), onlyOnWebsite);
        this.setItem(24, hyriPlus.createItem(this.account), onlyOnWebsite);
    }

}
