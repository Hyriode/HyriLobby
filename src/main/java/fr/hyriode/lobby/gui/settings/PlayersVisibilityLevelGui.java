package fr.hyriode.lobby.gui.settings;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.util.References;
import fr.hyriode.lobby.util.UsefulHeads;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PlayersVisibilityLevelGui extends HyriInventory {

    private static final String BASE = "item.visibility.";

    private final SettingsGui oldGui;
    private final IHyriLanguageManager lang;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;
    private HyriPlayersVisibilityLevel level;

    private final ItemStack currentItem;

    public PlayersVisibilityLevelGui(HyriLobby plugin, Player owner, SettingsGui oldGui) {
        super(owner, plugin.getHyrame().getLanguageManager().getValue(owner, "title.visibility.gui"), 27);

        this.oldGui = oldGui;
        this.lang = plugin.getHyrame().getLanguageManager();

        this.pm = plugin.getAPI().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());

        this.settings = this.player.getSettings();
        this.level = this.settings.getPlayersVisibilityLevel();

        this.currentItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ARROW_DOWN.getTexture()).build();

        this.init();
    }

    private void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.setItem(10, new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1)).withName(this.lang.getValue(this.player, BASE + "allItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.ALL)
        );
        this.setItem(12, new ItemBuilder(new Wool(DyeColor.YELLOW).toItemStack(1)).withName(this.lang.getValue(this.player, BASE + "friendsItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.FRIENDS)
        );
        this.setItem(14, new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1)).withName(this.lang.getValue(this.player, BASE + "partyItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.PARTY)
        );
        this.setItem(16, new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1)).withName(this.lang.getValue(this.player, BASE + "noneItem")).build(),
                e -> this.onLevelClick(HyriPlayersVisibilityLevel.NONE)
        );
        this.setItem(22, new ItemBuilder(Material.BARRIER).withName(this.lang.getValue(this.player, BASE + "quit")).build(),
                e -> this.owner.closeInventory()
        );

        //Fill part
        this.setFill(References.FILL_ITEM);

        this.updateCurrent();
    }

    private void onLevelClick(HyriPlayersVisibilityLevel level) {
        this.level = level;
        this.settings.setPlayersVisibilityLevel(this.level);

        this.updateCurrent();
        this.setFill(References.FILL_ITEM);
    }

    private void updateCurrent() {
        this.owner.getInventory().remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.lang.getValue(this.player, BASE + "current") + this.getIndicatorName()).build());
    }

    private String getIndicatorName() {
        switch (this.level) {
            case ALL: return this.lang.getValue(this.player, BASE + "allLevel");
            case FRIENDS: return this.lang.getValue(this.player, BASE + "friendsLevel");
            case PARTY: return this.lang.getValue(this.player, BASE + "partyLevel");
            case NONE: return this.lang.getValue(this.player, BASE + "noneLevel");
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