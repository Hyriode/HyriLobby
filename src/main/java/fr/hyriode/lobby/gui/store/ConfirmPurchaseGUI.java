package fr.hyriode.lobby.gui.store;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.store.StoreItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 04/07/2022 at 14:37
 */
public class ConfirmPurchaseGUI extends LobbyGUI {

    private Consumer<InventoryClickEvent> whenConfirm;
    private Consumer<InventoryClickEvent> whenCancel;

    private final StoreItem item;

    public ConfirmPurchaseGUI(Player owner, HyriLobby plugin, StoreItem item) {
        super(owner, plugin, () -> "store-confirm", 54);
        this.item = item;
    }

    @Override
    protected void init() {
        this.border();

        // Representative item
        this.setItem(13, this.item.createItem(this.account, false));

        // Confirm
        this.setItem(30, new ItemBuilder(Material.STAINED_GLASS, 1, 5)
                .withName(LobbyMessage.STORE_CONFIRM_ITEM_CONFIRM_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_CONFIRM_ITEM_CONFIRM_LORE.asList(this.account))
                .build(), this.whenConfirm);

        // Cancel
        this.setItem(32, new ItemBuilder(Material.STAINED_GLASS, 1, 14)
                .withName(LobbyMessage.STORE_CONFIRM_ITEM_CANCEL_NAME.asString(this.account))
                .withLore(LobbyMessage.STORE_CONFIRM_ITEM_CANCEL_LORE.asList(this.account))
                .build(), this.whenCancel);
    }

    public ConfirmPurchaseGUI whenConfirm(Consumer<InventoryClickEvent> whenConfirm) {
        this.whenConfirm = whenConfirm;
        return this;
    }

    public ConfirmPurchaseGUI whenCancel(Consumer<InventoryClickEvent> whenCancel) {
        this.whenCancel = whenCancel;
        return this;
    }

    @Override
    public void open() {
        this.init();
        super.open();
    }

}
