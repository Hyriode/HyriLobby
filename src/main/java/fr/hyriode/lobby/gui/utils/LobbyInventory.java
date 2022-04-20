package fr.hyriode.lobby.gui.utils;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.utils.UsefulHead;
import fr.hyriode.lobby.utils.function.TriFunction;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class LobbyInventory extends HyriInventory {

    protected static final Supplier<IHyriLanguageManager> LANG = IHyriLanguageManager.Provider::get;

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 9).withName(" ").build();
    public static final Function<UsefulHead, ItemBuilder> HEAD_ITEM = texture -> new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(texture.getTexture());
    public static final Function<Player, ItemStack> QUIT_ITEM = player -> new ItemBuilder(Material.BARRIER).withName(LANG.get().getValue(player, "item.global.quit")).build();
    public static final TriFunction<Player, String, UsefulHead, ItemStack> PAGE_ITEM = (player, page, head) -> {
        final String name = LANG.get().getValue(player, "item.global.page." + (head == UsefulHead.RIGHT_ARROW ? "next" : "previous")) + " (" + page + ")";

        return HEAD_ITEM.apply(head).withName(name).build();
    };

    protected final String name;
    protected final IHyrame hyrame;
    protected final HyriLobby plugin;
    protected final IHyriPlayer account;
    protected final IHyriPlayerManager playerManager;

    protected int pageIndex;

    public LobbyInventory(Player owner, HyriLobby plugin, String name, int size) {
        super(owner, HyriInventory.name(plugin.getHyrame(), owner, "item." + name + ".name"), size);

        this.name = name;
        this.plugin = plugin;
        this.hyrame = plugin.getHyrame();
        this.playerManager = HyriAPI.get().getPlayerManager();
        this.account = this.playerManager.getPlayer(owner.getUniqueId());

        this.pageIndex = 0;
    }

    protected abstract void init();

    protected ItemStack createSwitch(boolean option) {
        Dye dye = new Dye(Material.INK_SACK);
        dye.setColor(option ? DyeColor.LIME : DyeColor.RED);
        return new ItemBuilder(dye.toItemStack(1)).withName(this.getSwitchName(option)).build();
    }

    protected ItemStack updateSwitch(boolean option, ItemStack item) {
        item.setDurability((short) (option ? 10 : 1));
        return new ItemBuilder(item).withName(this.getSwitchName(option)).build();
    }

    protected String getSwitchName(boolean option) {
        return this.getMessage("global", "switch") + (option ? this.getMessage("global", "switch.off") : this.getMessage("global", "switch.on"));
    }

    protected void fillOutline(ItemStack item) {
        this.setHorizontalLine(0, 8, item);
        this.setVerticalLine(0, this.size - 8, item);
        this.setVerticalLine(8, this.size - 1, item);
        this.setHorizontalLine(this.size - 8, this.size - 1, item);
    }

    protected void placeQuitButton(Consumer<InventoryClickEvent> onClick) {
        this.setItem(this.size - 5, QUIT_ITEM.apply(this.getOwner()), onClick);
    }

    protected void tryToFill(int startSlot, int startIndex, Map<ItemStack, Consumer<InventoryClickEvent>> items) {
        final List<Map.Entry<ItemStack, Consumer<InventoryClickEvent>>> entry = new ArrayList<>(items.entrySet());

        int index = startIndex;
        for (int i = startSlot; i < this.inventory.getSize(); i++) {
            if (index >= items.size()) {
                break;
            }

            if (this.inventory.getItem(i) == null) {
                final Map.Entry<ItemStack, Consumer<InventoryClickEvent>> itemEntry = entry.get(index);
                this.setItem(i, itemEntry.getKey(), event -> itemEntry.getValue().accept(event));
                index++;
            }
        }
    }

    protected void setupPagination(int size, int contentPerPage, Consumer<InventoryClickEvent> onClick) {
        final int pages = this.getTotalPages(size, contentPerPage);

        if (pages <= 1) {
            return;
        }

        this.setItem(this.size - 1, PAGE_ITEM.apply(this.owner, this.getPageMessage(this.pageIndex, pages), UsefulHead.RIGHT_ARROW), e -> {
            this.pageIndex++;
            onClick.accept(e);
        });

        if (this.pageIndex > 0) {
            this.setItem(this.size - 9, PAGE_ITEM.apply(this.owner, this.getPageMessage(this.pageIndex - 1, pages), UsefulHead.LEFT_ARROW), e -> {
                this.pageIndex--;
                onClick.accept(e);
            });
        }
    }

    protected String getMessage(String key) {
        return this.getMessage(this.name, key);
    };

    protected String getMessage(String name, String key) {
        return LANG.get().getValue(this.owner, "item." + name + "." + key);
    }

    protected String getCustomMessage(String key) {
        return LANG.get().getValue(this.owner, key);
    }

    protected int getIndexFromPage(int contentPerPage) {
        return this.pageIndex * contentPerPage;
    }

    protected int getPageFromIndex(int index, int contentPerPage) {
        return index / contentPerPage;
    }

    protected int getTotalPages(int size, int contentPerPage) {
        return (int) Math.ceil((double) size / (double) contentPerPage);
    }

    protected String getPageMessage(int page, int totalPages) {
        return (page + 1) + "/" + totalPages;
    }
}
