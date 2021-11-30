package fr.hyriode.lobby.gui.settings;

import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriPlayersVisibilityLevel;
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

public class PlayersVisibilityLevelGui extends AbstractInventory {

    private final Player player;
    private final SettingsGui oldGui;
    private final IHyriPlayer hyriPlayer;
    private final IHyriLanguageManager lang;
    private final IHyriPlayerSettings hyriSettings;
    private final IHyriPlayerManager hyriPlayerManager;

    private HyriPlayersVisibilityLevel level;

    private final ItemStack fillItem;
    private final ItemStack closeItem;
    private final ItemStack allLevelItem;
    private final ItemStack noneLevelItem;
    private final ItemStack partyLevelItem;
    private final ItemStack friendsLevelItem;
    private final ItemStack currentLevelItem;

    public PlayersVisibilityLevelGui(HyriLobby plugin, Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, plugin.getHyrame().getLanguageManager().getMessageValueForPlayer(owner, "title.visibility.gui"), 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.lang = plugin.getHyrame().getLanguageManager();
        this.level = this.hyriSettings.getPlayersVisibilityLevel();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.quit")).build();
        this.allLevelItem = new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.allItem")).build();
        this.noneLevelItem = new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.noneItem")).build();
        this.partyLevelItem = new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.partyItem")).build();
        this.friendsLevelItem = new ItemBuilder(new Wool(DyeColor.YELLOW).toItemStack(1))
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.friendsItem")).build();
        this.currentLevelItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.current") + this.getIndicatorName(this.level)).build();

        setFill(this.fillItem);
        setItem(this.getSlot(this.level), this.currentLevelItem);
        setItem(10, this.allLevelItem, e -> {
            this.level = HyriPlayersVisibilityLevel.ALL;
            this.hyriSettings.setPlayersVisibilityLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(12, this.friendsLevelItem, e -> {
            this.level = HyriPlayersVisibilityLevel.FRIENDS;
            this.hyriSettings.setPlayersVisibilityLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(14, this.partyLevelItem, e -> {
            this.level = HyriPlayersVisibilityLevel.PARTY;
            this.hyriSettings.setPlayersVisibilityLevel(this.level);
            e.getInventory().remove(this.currentLevelItem);
            e.getInventory().setItem(this.getSlot(this.level), this.updateCurrent(this.currentLevelItem, this.level));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(16, this.noneLevelItem, e -> {
            this.level = HyriPlayersVisibilityLevel.NONE;
            this.hyriSettings.setPlayersVisibilityLevel(this.level);
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

    private ItemStack updateCurrent(ItemStack item, HyriPlayersVisibilityLevel level) {
        return new ItemBuilder(item).withName(this.lang.getMessageValueForPlayer(this.player, "item.visibility.current") + this.getIndicatorName(level)).build();
    }

    private String getIndicatorName(HyriPlayersVisibilityLevel level) {
        switch (level) {
            case ALL:
                return this.lang.getMessageValueForPlayer(this.player, "item.visibility.allLevel");
            case FRIENDS:
                return this.lang.getMessageValueForPlayer(this.player, "item.visibility.friendsLevel");
            case PARTY:
                return this.lang.getMessageValueForPlayer(this.player, "item.visibility.partyLevel");
            case NONE:
                return this.lang.getMessageValueForPlayer(this.player, "item.visibility.noneLevel");
            default:
                return "";
        }
    }

    private int getSlot(HyriPlayersVisibilityLevel level) {
        switch (level) {
            case ALL:
                return 1;
            case FRIENDS:
                return 3;
            case PARTY:
                return 5;
            case NONE:
                return 7;
            default:
                return 0;
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        this.hyriPlayerManager.sendPlayer(this.hyriPlayer);
    }
}
