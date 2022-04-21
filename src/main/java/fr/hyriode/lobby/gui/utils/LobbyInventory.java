package fr.hyriode.lobby.gui.utils;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.utils.Language;
import fr.hyriode.lobby.utils.UsefulHead;
import fr.hyriode.lobby.utils.function.TriFunction;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class LobbyInventory extends HyriInventory {

    public static final ItemStack FILL_ITEM = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 9).withName(" ").build();
    public static final Function<UsefulHead, ItemBuilder> HEAD_ITEM = texture -> new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(texture.getTexture());
    public static final Function<UUID, ItemBuilder> PLAYER_HEAD_ITEM = player -> new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withSkullOwner(player);
    public static final Function<Player, ItemStack> RETURN_ITEM = player -> HEAD_ITEM.apply(UsefulHead.LEFT_ARROW).withName(ChatColor.RED + Language.getMessage(player, "item.global.quit")).build();
    public static final TriFunction<Player, String, UsefulHead, ItemStack> PAGE_ITEM = (player, page, head) -> {
        final String name = ChatColor.WHITE + Language.getMessage(player, "item.global.page." + (head == UsefulHead.RIGHT_ARROW ? "next" : "previous")) + ChatColor.AQUA + " (" + page + ")";

        return HEAD_ITEM.apply(head).withName(name).build();
    };

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    protected final String name;
    protected final IHyrame hyrame;
    protected final HyriLobby plugin;
    protected final IHyriPlayer account;
    protected final IHyriPlayerManager playerManager;

    protected LobbyInventory previousGui;
    protected Consumer<InventoryClickEvent> onReturn;

    protected int pageIndex;
    protected final int pageSize;

    protected ItemStack currentButton;
    protected Function<Integer, String> currentButtonName;
    protected Consumer<InventoryClickEvent> onCurrentUpdate;

    public LobbyInventory(Player owner, HyriLobby plugin, String name, int size) {
        this(owner, plugin, name, name + ".name", size);
    }

    public LobbyInventory(Player owner, HyriLobby plugin, String name, String title, int size) {
        super(owner, ChatColor.stripColor(Language.getMessage(owner, "item." + title)), size);

        this.name = name;
        this.plugin = plugin;
        this.hyrame = plugin.getHyrame();
        this.playerManager = HyriAPI.get().getPlayerManager();
        this.account = this.playerManager.getPlayer(owner.getUniqueId());

        this.pageIndex = 0;
        this.pageSize = (this.size - 9 - 9) / 9 * 2;
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

        if (this.previousGui != null) {
            this.placeReturnButton();
        }
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

    protected void setupPagination(int elementsSize, Consumer<InventoryClickEvent> onClick) {
        final int pages = this.getTotalPages(elementsSize);

        if (pages <= 1) {
            return;
        }

        if (this.pageIndex + 1 != pages) {
            this.setItem(this.size - 1, PAGE_ITEM.apply(this.owner, this.getPageMessage(this.pageIndex, pages), UsefulHead.RIGHT_ARROW), e -> {
                this.pageIndex++;
                onClick.accept(e);
            });
        }

        if (this.pageIndex > 0) {
            this.setItem(this.size - 9, PAGE_ITEM.apply(this.owner, this.getPageMessage(this.pageIndex - 1, pages), UsefulHead.LEFT_ARROW), e -> {
                this.pageIndex--;
                onClick.accept(e);
            });
        }
    }

    protected void setupCurrentButton(ItemStack item, int slot, Function<Integer, String> name, Consumer<InventoryClickEvent> onUpdate) {
        this.currentButton = item;
        this.currentButtonName = name;
        this.onCurrentUpdate = onUpdate;

        this.updateCurrentButton(slot, null);
    }

    protected void updateCurrentButton(int slot, InventoryClickEvent event) {
        if (this.currentButton != null) {
            this.inventory.remove(this.currentButton);
            this.setItem(slot, new ItemBuilder(this.currentButton).withName(this.currentButtonName.apply(slot)).build());
        }

        if (this.onCurrentUpdate != null) {
            this.onCurrentUpdate.accept(event);
        }
    }

    protected void setupReturnButton(LobbyInventory previous, Consumer<InventoryClickEvent> onClick) {
        this.previousGui = previous;
        this.onReturn = onClick;

        this.placeReturnButton();
    }

    protected void placeReturnButton() {
        this.setItem(this.size - 5, RETURN_ITEM.apply(this.owner), e -> {
            this.owner.closeInventory();

            if (this.onReturn != null) {
                this.onReturn.accept(e);
            }
        });
    }

    protected String getMessage(String key) {
        return this.getMessage(this.name, key);
    };

    protected String getMessage(String name, String key) {
        return Language.getMessage(this.owner, "item." + name + "." + key);
    }

    protected String getCustomMessage(String key) {
        return Language.getMessage(this.owner, key);
    }

    protected int getIndexFromPage() {
        return this.pageIndex * this.pageSize;
    }

    protected int getPageFromIndex(int index) {
        return index / this.pageSize;
    }

    protected int getTotalPages(int size) {
        return (int) Math.ceil((double) size / (double) this.pageSize);
    }

    protected String getPageMessage(int page, int totalPages) {
        return (page + 1) + "/" + totalPages;
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.playerManager.sendPlayer(this.account);

        if (this.previousGui != null) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.previousGui.open(), 1L);
        }
    }
}
