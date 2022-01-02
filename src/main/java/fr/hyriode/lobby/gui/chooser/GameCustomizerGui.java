package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.api.chooser.GameItem;
import fr.hyriode.lobby.gui.GameChooserGui;
import fr.hyriode.lobby.util.References;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GameCustomizerGui extends HyriInventory {

    private final GameChooserGui oldGui;

    private final int slot;
    private final GameItem item;

    public GameCustomizerGui(GameItem item, int clickedSlot, GameChooserGui oldGui) {
        //TODO Upscale depends on minigames number
        super(oldGui.player, oldGui.lang.getValue(oldGui.player, "item.customize.replacer"), 27);

        this.oldGui = oldGui;

        this.item = item;
        this.slot = clickedSlot;

        this.init();
    }

    private void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.oldGui.menu.getGames().forEach((itemSlot, gameItem) -> {
            final int s = itemSlot + 10;
            this.setItem(s, new ItemBuilder(gameItem != this.item ? Material.getMaterial(gameItem.getMaterial()) : Material.BARRIER).withName("§f" + (gameItem != this.item ? gameItem.getName() : this.oldGui.lang.getValue(this.owner, "item.customize.same"))).build(), e -> {
                this.oldGui.menu.setGame(this.slot - 10, gameItem);
                this.oldGui.menu.setGame(itemSlot, this.item);
                this.owner.closeInventory();

                this.oldGui.enchantedGameClickCallback(this.item, this.slot - 10, gameItem, itemSlot);
            });
        });

        //Fill part
        this.setFill(References.FILL_ITEM);
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.oldGui.open();
        this.oldGui.pm.sendPlayer(this.oldGui.lp);
    }
}