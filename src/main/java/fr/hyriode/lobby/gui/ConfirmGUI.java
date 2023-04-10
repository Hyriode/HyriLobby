package fr.hyriode.lobby.gui;

import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Created by AstFaster
 * on 23/10/2022 at 12:56
 */
public class ConfirmGUI extends LobbyGUI {

    protected Consumer<InventoryClickEvent> whenConfirm;
    protected Consumer<InventoryClickEvent> whenCancel;

    protected final ItemStack item;

    public ConfirmGUI(Player owner, HyriLobby plugin, ItemStack item) {
        super(owner, plugin, () -> "confirm", 54);
        this.item = item;
    }

    @Override
    protected void init() {
        this.applyDesign(Design.BORDER);

        // Representative item
        this.setItem(13, this.item);

        // Confirm
        this.setItem(30, new ItemBuilder(Material.STAINED_GLASS, 1, 5)
                .withName(LobbyMessage.CONFIRM_ITEM_CONFIRM_NAME.asString(this.account))
                .build(), this.whenConfirm);

        // Cancel
        this.setItem(32, new ItemBuilder(Material.STAINED_GLASS, 1, 14)
                .withName(LobbyMessage.CONFIRM_ITEM_CANCEL_NAME.asString(this.account))
                .build(), this.whenCancel);
    }

    public ConfirmGUI whenConfirm(Consumer<InventoryClickEvent> whenConfirm) {
        this.whenConfirm = whenConfirm;
        return this;
    }

    public ConfirmGUI whenCancel(Consumer<InventoryClickEvent> whenCancel) {
        this.whenCancel = whenCancel;
        return this;
    }

    @Override
    public void open() {
        this.init();
        super.open();
    }

}
