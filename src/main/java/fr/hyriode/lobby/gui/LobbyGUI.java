package fr.hyriode.lobby.gui;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.inventory.pagination.PaginatedInventory;
import fr.hyriode.hyrame.inventory.pagination.PaginatedItem;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class LobbyGUI extends PaginatedInventory {

    public static final IDesign<Byte> PURIFIED_BORDER = new IDesign<Byte>() {

        private final int[] slots = new int[] {0, 1, 2, 6, 7, 8, 45, 46, 47, 51, 52, 53};

        @Override
        public void apply(HyriInventory inventory, Byte data) {
            final ItemStack border = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 9)
                    .withName(" ")
                    .withAllItemFlags()
                    .build();

            for (int slot : slots) {
                inventory.setItem(slot, border);
            }
        }
    };
    public static final Function<UsefulHead, ItemBuilder> HEAD_ITEM = texture -> ItemBuilder.asHead().withHeadTexture(texture);

    private boolean pagesItem;
    private int previousPageSlot;
    private int nextPageSlot;

    protected HyriLobby plugin;
    protected final IHyrame hyrame;
    protected IHyriPlayer account;
    protected final IHyriPlayerSession session;

    public LobbyGUI(Player owner, HyriLobby plugin, String name, int size) {
        super(owner, name, size);
        this.plugin = plugin;
        this.hyrame = plugin.getHyrame();
        this.account = IHyriPlayer.get(owner.getUniqueId());
        this.session = IHyriPlayerSession.get(owner.getUniqueId());
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

        if (this.paginationManager.getPagination().totalPages() > 1) {
            this.addDefaultPagesItems(previousSlot, nextSlot);
        }
    }

}
