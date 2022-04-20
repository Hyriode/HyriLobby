package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PlayersVisibilityLevelGui extends LobbyInventory {

    private final IHyriPlayerSettings settings;
    private HyriPlayersVisibilityLevel level;

    private final ItemStack currentItem;

    public PlayersVisibilityLevelGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "visibility", 27);

        this.settings = this.account.getSettings();
        this.level = this.settings.getPlayersVisibilityLevel();

        this.currentItem = HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.setFill(FILL_ITEM);
        this.placeQuitButton(e -> this.owner.closeInventory());

        //Items with Consumer part
        this.setItem(10, new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1)).withName(this.getMessage("name")).build(), e -> this.onLevelClick(HyriPlayersVisibilityLevel.ALL));
        this.setItem(12, new ItemBuilder(new Wool(DyeColor.YELLOW).toItemStack(1)).withName(this.getMessage("name")).build(), e -> this.onLevelClick(HyriPlayersVisibilityLevel.FRIENDS));
        this.setItem(14, new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1)).withName(this.getMessage("name")).build(), e -> this.onLevelClick(HyriPlayersVisibilityLevel.PARTY));
        this.setItem(16, new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1)).withName(this.getMessage("name")).build(), e -> this.onLevelClick(HyriPlayersVisibilityLevel.NONE));

        this.updateCurrent();
    }

    private void onLevelClick(HyriPlayersVisibilityLevel level) {
        this.level = level;
        this.settings.setPlayersVisibilityLevel(this.level);

        this.updateCurrent();
        this.setFill(FILL_ITEM);
    }

    private void updateCurrent() {
        this.inventory.remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.getMessage("current")).build());
    }

    private int getSlot() {
        switch (this.level) {
            case ALL: return 1;
            case FRIENDS: return 3;
            case PARTY: return 5;
            case NONE: return 7;
            default: return 0;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.playerManager.sendPlayer(this.account);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> new SettingsGui(this.plugin, this.owner).open(), 1L);
    }
}
