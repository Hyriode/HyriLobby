package fr.hyriode.lobby.gui;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.inventory.pagination.PaginatedInventory;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.InventoryUtil;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class LobbyGUI extends PaginatedInventory {

    public static final Function<UsefulHead, ItemBuilder> HEAD_ITEM = texture -> ItemBuilder.asHead().withHeadTexture(texture);

    private boolean pagesItem;
    private int previousPageSlot;
    private int nextPageSlot;

    protected HyriLobby plugin;
    protected final IHyrame hyrame;
    protected final IHyriPlayer account;

    public LobbyGUI(Player owner, HyriLobby plugin, String name, int size) {
        super(owner, name, size);
        this.plugin = plugin;
        this.hyrame = plugin.getHyrame();
        this.account = IHyriPlayer.get(owner.getUniqueId());
    }

    public LobbyGUI(Player owner, HyriLobby plugin, Supplier<String> guiName, int size) {
        this(owner, plugin, name(owner, "gui." + guiName.get() + ".name"), size);
    }

    protected abstract void init();

    @Override
    public void updatePagination(int page, List<PaginatedItem> items) {
        if (this.pagesItem) {
            this.addPagesItems(this.previousPageSlot, this.nextPageSlot);
        }
    }

    protected void border() {
        final ItemStack item = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9).withName("§8").withAllItemFlags().build();

        for (int barrierSlot : InventoryUtil.getBorderSlots()) {
            this.setItem(barrierSlot, item);
        }
    }

    protected void openWithGoBack(int slot, HyriInventory inventory) {
        inventory.setItem(slot, new ItemBuilder(Material.ARROW)
                .withName(LobbyMessage.BACK_ITEM.asString(this.account))
                .build(), event -> this.open());

        inventory.open();
    }

    protected void addPagesItems(int previousSlot, int nextSlot) {
        this.pagesItem = true;
        this.previousPageSlot = previousSlot;
        this.nextPageSlot = nextSlot;

        final int currentPage = this.paginationManager.currentPage() + 1;
        final int totalPages = this.paginationManager.getPagination().totalPages();

        this.setItem(previousSlot, ItemBuilder.asHead()
                .withName(LobbyMessage.PREVIOUS_ITEM_NAME.asString(this.account).replace("%current_page%", String.valueOf(currentPage)).replace("%total_pages%", String.valueOf(totalPages)))
                .withLore(LobbyMessage.PREVIOUS_ITEM_LORE.asList(this.account))
                .withHeadTexture(UsefulHead.MONITOR_BACKWARD)
                .build(), event -> this.paginationManager.previousPage());

        this.setItem(nextSlot, ItemBuilder.asHead()
                .withName(LobbyMessage.NEXT_ITEM_NAME.asString(this.account).replace("%current_page%", String.valueOf(currentPage)).replace("%total_pages%", String.valueOf(totalPages)))
                .withLore(LobbyMessage.NEXT_ITEM_LORE.asList(this.account))
                .withHeadTexture(UsefulHead.MONITOR_FORWARD)
                .build(), event -> this.paginationManager.nextPage());
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.account.update();
    }

}