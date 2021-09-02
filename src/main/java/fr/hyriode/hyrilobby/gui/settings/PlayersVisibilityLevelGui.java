package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.common.inventory.AbstractInventory;
import fr.hyriode.common.item.ItemBuilder;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriPlayersVisibilityLevel;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.hyrilobby.gui.SettingsGui;
import fr.hyriode.hyrilobby.util.UsefulHeads;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

public class PlayersVisibilityLevelGui extends AbstractInventory {

    private Player player;
    private SettingsGui oldGui;
    private IHyriPlayer hyriPlayer;
    private IHyriPlayerSettings hyriSettings;
    private HyriPlayersVisibilityLevel level;
    private IHyriPlayerManager hyriPlayerManager;

    private ItemStack fillItem;
    private ItemStack closeItem;
    private ItemStack allLevelItem;
    private ItemStack noneLevelItem;
    private ItemStack partyLevelItem;
    private ItemStack friendsLevelItem;
    private ItemStack currentLevelItem;

    public PlayersVisibilityLevelGui(Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, "Filtre des Joueurs Affichés", 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.level = this.hyriSettings.getPlayersVisibilityLevel();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName("§fQuitter").build();
        this.allLevelItem = new ItemBuilder(new Wool(DyeColor.LIME).toItemStack(1))
                .withName("§fClique pour passer sur \"Tout le Monde\"").build();
        this.noneLevelItem = new ItemBuilder(new Wool(DyeColor.RED).toItemStack(1))
                .withName("§fClique pour passer sur \"Aucun\"").build();
        this.partyLevelItem = new ItemBuilder(new Wool(DyeColor.ORANGE).toItemStack(1))
                .withName("§fClique pour passer sur \"Partie Uniquement\"").build();
        this.friendsLevelItem = new ItemBuilder(new Wool(DyeColor.YELLOW).toItemStack(1))
                .withName("§fClique pour passer sur \"Amis Uniquement\"").build();
        this.currentLevelItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName("§fActuellement sur \"" + this.getIndicatorName(this.level) + "\"").build();

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
        return new ItemBuilder(item).withName("§fActuellement sur \"" + this.getIndicatorName(level) + "\"").build();
    }

    private String getIndicatorName(HyriPlayersVisibilityLevel level) {
        switch (level) {
            case ALL:
                return "Tout le Monde";
            case FRIENDS:
                return "Amis Uniquement";
            case PARTY:
                return "Partie Uniquement";
            case NONE:
                return "Aucun";
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
