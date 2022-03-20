package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.settings.HyriPrivateMessagesLevel;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHeads;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PrivateMessagesLevelGui extends LobbyInventory {

    private final HyriLobby plugin;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;
    private HyriPrivateMessagesLevel level;

    private final ItemStack currentItem;

    public PrivateMessagesLevelGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame(), "item.mp.", "title.mp.gui", 27);

        this.plugin = plugin;

        this.pm = HyriAPI.get().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());

        this.settings = this.player.getSettings();
        this.level = this.settings.getPrivateMessagesLevel();

        this.currentItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ARROW_DOWN.getTexture()).build();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.setItem(11, new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1)).withName(this.getKey("allItem")).build(),
                e -> this.onLevelClick(HyriPrivateMessagesLevel.ALL)
        );
        this.setItem(13, new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1)).withName(this.getKey("friendsItem")).build(),
                e -> this.onLevelClick(HyriPrivateMessagesLevel.FRIENDS)
        );
        this.setItem(15, new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1)).withName(this.getKey("noneItem")).build(),
                e -> this.onLevelClick(HyriPrivateMessagesLevel.NONE)
        );
        this.setItem(22, new ItemBuilder(Material.BARRIER).withName(this.getKey("quit")).build(),
                e -> this.owner.closeInventory()
        );

        //Fill part
        this.setFill(FILL_ITEM);

        this.updateCurrent();
    }

    private void onLevelClick(HyriPrivateMessagesLevel level) {
        this.level = level;
        this.settings.setPrivateMessagesLevel(this.level);

        this.updateCurrent();
        this.setFill(FILL_ITEM);
    }

    private void updateCurrent() {
        this.inventory.remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.getKey("current") + this.getIndicatorName()).build());
    }

    private String getIndicatorName() {
        switch (this.level) {
            case ALL: return this.getKey("allLevel");
            case FRIENDS: return this.getKey("friendsLevel");
            case NONE: return this.getKey("noneLevel");
            default: return "";
        }
    }

    private int getSlot() {
        switch (this.level) {
            case ALL: return 2;
            case FRIENDS: return 4;
            case NONE: return 6;
            default: return 0;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.pm.sendPlayer(this.player);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> new SettingsGui(this.plugin, this.owner).open(), 1L);
    }
}
