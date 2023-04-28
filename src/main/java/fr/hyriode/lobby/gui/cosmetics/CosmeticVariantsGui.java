package fr.hyriode.lobby.gui.cosmetics;

import fr.hyriode.cosmetics.common.AbstractCosmetic;
import fr.hyriode.cosmetics.user.PlayerCosmetic;
import fr.hyriode.hyrame.inventory.pagination.PaginatedInventory;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.inventory.pagination.PaginationArea;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Pagination;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.cosmetic.CosmeticItem;
import fr.hyriode.lobby.gui.LobbyGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CosmeticVariantsGui extends LobbyGUI {

    private final PlayerCosmetic<?> playerCosmetic;

    public CosmeticVariantsGui(final Player owner, HyriLobby plugin, final PlayerCosmetic<?> playerCosmetic) {
        super(owner, plugin, name(owner, "gui.cosmetic.variants"), 9 * 6);
        this.playerCosmetic = playerCosmetic;

        this.paginationManager.setArea(new PaginationArea(20, 33));

        this.setItem(4, new CosmeticItem(playerCosmetic.getAbstractCosmetic().getType()).toItemStack(owner, false));
        this.setItem(49,
                new ItemBuilder(Material.ARROW).withName(name(owner, "go-back.display")).build(), event -> {
                    this.owner.closeInventory();
                    new CosmeticsGui(this.owner, this.plugin, playerCosmetic.getAbstractCosmetic().getCategory()).open();
                }
        );

        this.init();
        this.setupItems();
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);
        this.addPagesItems(27, 35);
    }

    private void setupItems() {
        final Pagination<PaginatedItem> pagination = this.paginationManager.getPagination();
        pagination.clear();

        AbstractCosmetic<?> abstractCosmetic = this.playerCosmetic.getAbstractCosmetic();
        if (abstractCosmetic.hasVariants()) {
            for (Map.Entry<String, ItemStack> entry : abstractCosmetic.getVariantsItem().entrySet()) {
                pagination.add(PaginatedItem.from(entry.getValue(), clickEvent(entry.getKey(), abstractCosmetic)));
            }
        }

        this.paginationManager.updateGUI();
    }

    private Consumer<InventoryClickEvent> clickEvent(String variant, AbstractCosmetic<?> cosmetic) {
        return event -> {
            cosmetic.setVariant(variant);
            playerCosmetic.getUser().updateData();
            this.owner.playSound(this.owner.getLocation(), Sound.VILLAGER_IDLE, 0.5F, 1.0F);
            this.owner.getOpenInventory().close();
            new CosmeticsGui(owner, plugin, cosmetic.getCategory()).open();
        };
    }
}
