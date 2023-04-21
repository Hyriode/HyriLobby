package fr.hyriode.lobby.gui.profile.lootbox;

import fr.hyriode.api.lootbox.HyriLootboxTransaction;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.ConfirmGUI;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.lootbox.LootboxAnimation;
import fr.hyriode.lobby.lootbox.LootboxReward;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by AstFaster
 * on 21/04/2023 at 14:21
 */
public class LootboxPreviewGUI extends LobbyGUI {

    private final String transaction;
    private final HyriLootboxTransaction lootbox;
    private final ItemStack lootboxIcon;
    private final LootboxReward rewards;

    public LootboxPreviewGUI(Player owner, HyriLobby plugin, String transaction, HyriLootboxTransaction lootbox, ItemStack lootboxIcon) {
        super(owner, plugin, () -> "lootbox-preview", 54);
        this.transaction = transaction;
        this.lootbox = lootbox;
        this.lootboxIcon = lootboxIcon;
        this.rewards = LootboxReward.valueOf(this.lootbox.getRarity().name());
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);
        this.addPagesItems(27, 35);

        this.setItem(13, new ItemBuilder(this.lootboxIcon)
                .appendLore("", LobbyMessage.CLICK_TO_OPEN.asString(this.account))
                .build(),
                event -> {
                    new ConfirmGUI(this.owner, this.plugin, this.lootboxIcon)
                            .whenConfirm(e -> this.triggerLootbox())
                            .whenCancel(e -> {
                                this.open();

                                this.owner.playSound(this.owner.getLocation(), Sound.FIZZ, 0.5F, 1.0F);
                            })
                            .open();
                });

        this.addLoots();
    }

    private void triggerLootbox() {
        this.owner.closeInventory();

        // Set booster as 'used'
        this.account.getTransactions().get(HyriLootboxTransaction.TRANSACTIONS_TYPE, this.transaction)
                .loadContent(new HyriLootboxTransaction())
                .setUsed(true);
        this.account.update();

        new LootboxAnimation(this.owner, this.plugin, this.rewards).start();
    }

    private void addLoots() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        for (LootboxReward.Item item : this.rewards.getItems()) {
            pagination.add(PaginatedItem.from(item.createItem(this.owner)));
        }

        this.paginationManager.updateGUI();
    }

}
