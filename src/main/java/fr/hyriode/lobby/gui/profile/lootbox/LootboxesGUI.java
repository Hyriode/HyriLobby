package fr.hyriode.lobby.gui.profile.lootbox;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.lootbox.HyriLootboxTransaction;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.HyrameMessage;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.lootbox.Lootbox;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class LootboxesGUI extends LobbyGUI {

    public LootboxesGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "lootboxes", 54);
        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.init();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        this.setupItems();
        this.addPagesItems(27, 35);

        this.paginationManager.updateGUI();
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();

        pagination.clear();

        final Map<String, HyriLootboxTransaction> lootboxes = HyriAPI.get().getLootboxManager().getLootboxes(this.account);

        for (Map.Entry<String, HyriLootboxTransaction> entry : lootboxes.entrySet()
                .stream()
                .sorted(Comparator.comparing(o -> o.getValue().getRarity()))
                .collect(Collectors.toList())) {
            final HyriLootboxTransaction lootbox = entry.getValue();

            if (lootbox.isUsed()) {
                continue;
            }

            final ItemStack itemStack = this.createLootboxItem(lootbox);

            pagination.add(PaginatedItem.from(itemStack, event -> this.openWithGoBack(49, new LootboxPreviewGUI(this.owner, this.plugin, entry.getKey(), lootbox, new ItemBuilder(itemStack).removeLoreLines(2).build()))));
        }
    }

    private ItemStack createLootboxItem(HyriLootboxTransaction lootbox) {
        final ItemStack itemStack = ItemBuilder.asHead(UsefulHead.ENDER_CHEST)
                .build();

        itemStack.setAmount(lootbox.getRarity().ordinal() + 1);

        return new ItemBuilder(itemStack)
                .withName(LobbyMessage.LOOTBOX_ITEM_NAME.asString(this.account).replace("%stars%", Lootbox.valueOf(lootbox.getRarity().name()).format()))
                .withLore(new ArrayList<>())
                .appendLore("", LobbyMessage.CLICK_TO_SEE.asString(this.account))
                .build();
    }

}
