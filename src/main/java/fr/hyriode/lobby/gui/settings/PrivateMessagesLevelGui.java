package fr.hyriode.lobby.gui.settings;

import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriPrivateMessagesLevel;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PrivateMessagesLevelGui extends AbstractInventory {

    private final Player player;
    private final SettingsGui oldGui;
    private final IHyriPlayer hyriPlayer;
    private final IHyriLanguageManager lang;
    private final IHyriPlayerSettings hyriSettings;
    private final IHyriPlayerManager hyriPlayerManager;

    private HyriPrivateMessagesLevel level;

    private final ItemStack fillItem;
    private final ItemStack closeItem;
    private final ItemStack allLevelItem;
    private final ItemStack noneLevelItem;
    private final ItemStack friendsLevelItem;
    private final ItemStack currentLevelItem;

    public PrivateMessagesLevelGui(HyriLobby plugin, Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.mp.gui"), 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.lang = plugin.getHyrame().getLanguageManager();
        this.level = this.hyriSettings.getPrivateMessagesLevel();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.quit")).build();
        this.allLevelItem = new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.allItem")).build();
        this.noneLevelItem = new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.noneItem")).build();
        this.friendsLevelItem = new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.friendsItem")).build();
        this.currentLevelItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.current") + this.getIndicatorName(this.level)).build();

        setFill(this.fillItem);
        setItem(this.getSlot(this.level), this.currentLevelItem);
        setItem(11, this.allLevelItem, e -> {
            this.level = HyriPrivateMessagesLevel.ALL;
            this.hyriSettings.setPrivateMessagesLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(13, this.friendsLevelItem, e -> {
            this.level = HyriPrivateMessagesLevel.FRIENDS;
            this.hyriSettings.setPrivateMessagesLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(15, this.noneLevelItem, e -> {
            this.level = HyriPrivateMessagesLevel.NONE;
            this.hyriSettings.setPrivateMessagesLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(22, this.closeItem, e -> {
            this.player.closeInventory();
            this.oldGui.open();
        });
    }

    private ItemStack updateCurrent(ItemStack item, HyriPrivateMessagesLevel level) {
        return new ItemBuilder(item).withName(this.lang.getMessageValueForPlayer(this.player, "item.mp.current") + this.getIndicatorName(level)).build();
    }

    private String getIndicatorName(HyriPrivateMessagesLevel level) {
        switch (level) {
            case ALL:
                return this.lang.getMessageValueForPlayer(this.player, "item.mp.allLevel");
            case FRIENDS:
                return this.lang.getMessageValueForPlayer(this.player, "item.mp.friendsLevel");
            case NONE:
                return this.lang.getMessageValueForPlayer(this.player, "item.mp.noneLevel");
            default:
                return "";
        }
    }

    private int getSlot(HyriPrivateMessagesLevel level) {
        switch (level) {
            case ALL:
                return 2;
            case FRIENDS:
                return 4;
            case NONE:
                return 6;
            default:
                return 0;
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.hyriPlayerManager.sendPlayer(this.hyriPlayer);
    }
}
