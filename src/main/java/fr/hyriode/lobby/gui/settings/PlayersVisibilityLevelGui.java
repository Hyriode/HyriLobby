package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHeads;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PlayersVisibilityLevelGui extends LobbyInventory {

    private final SettingsGui oldGui;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;
    private HyriPlayersVisibilityLevel level;

    private final ItemStack currentItem;

    public PlayersVisibilityLevelGui(HyriLobby plugin, Player owner, SettingsGui oldGui) {
        super(owner, plugin.getHyrame(), "item.visibility.", "title.visibility.gui", 27);

        this.oldGui = oldGui;

        this.pm = HyriAPI.get().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());

        this.settings = this.player.getSettings();
        this.level = this.settings.getPlayersVisibilityLevel();

        this.currentItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ARROW_DOWN.getTexture()).build();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.setItem(10, new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1)).withName(this.getKey("allItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.ALL)
        );
        this.setItem(12, new ItemBuilder(new Wool(DyeColor.YELLOW).toItemStack(1)).withName(this.getKey("friendsItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.FRIENDS)
        );
        this.setItem(14, new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1)).withName(this.getKey("partyItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.PARTY)
        );
        this.setItem(16, new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1)).withName(this.getKey("noneItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.NONE)
        );
        this.setItem(22, new ItemBuilder(Material.BARRIER).withName(this.getKey("quit")).build(),
                e -> this.owner.closeInventory()
        );

        //Fill part
        this.setFill(HyriLobby.FILL_ITEM);

        this.updateCurrent();
    }

    private void onLevelClick(HyriPlayersVisibilityLevel level) {
        this.level = level;
        this.settings.setPlayersVisibilityLevel(this.level);

        this.updateCurrent();
        this.setFill(HyriLobby.FILL_ITEM);
    }

    private void updateCurrent() {
        this.inventory.remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.getKey("current") + this.getIndicatorName()).build());
    }

    private String getIndicatorName() {
        switch (this.level) {
            case ALL: return this.getKey("allLevel");
            case FRIENDS: return this.getKey("friendsLevel");
            case PARTY: return this.getKey("partyLevel");
            case NONE: return this.getKey("noneLevel");
            default: return "";
        }
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
        this.oldGui.open();
        this.pm.sendPlayer(this.player);
    }
}
