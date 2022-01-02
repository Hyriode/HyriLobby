package fr.hyriode.lobby.gui.chooser;

import fr.hyriode.lobby.api.chooser.GameItem;
import fr.hyriode.lobby.gui.GameChooserGui;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.Material;

public class GameCustomizerGui extends AbstractInventory {

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
        this.oldGui.menu.getGames().forEach((itemSlot, gameItem) -> {
            final int s = itemSlot + 10;
            this.setItem(s, new ItemBuilder(gameItem != this.item ? Material.getMaterial(gameItem.getMaterial()) : Material.BARRIER).withName("§f" + (gameItem != this.item ? gameItem.getName() : this.oldGui.lang.getValue(this.owner, "item.customize.same"))).build(), e -> {
                this.owner.closeInventory();
                this.oldGui.open();

                this.oldGui.enchantedGameClickCallback(this.item, this.slot - 10, gameItem, itemSlot);
            });
        });

        this.setFill(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).withName(" ").build());
    }
}