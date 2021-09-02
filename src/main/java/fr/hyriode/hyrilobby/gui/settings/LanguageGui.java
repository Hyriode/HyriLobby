package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.common.inventory.AbstractInventory;
import fr.hyriode.common.item.ItemBuilder;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.hyrilobby.gui.SettingsGui;
import fr.hyriode.hyrilobby.util.UsefulHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends AbstractInventory {

    private Player player;
    private SettingsGui oldGui;
    private IHyriPlayer hyriPlayer;
    private IHyriPlayerSettings hyriSettings;
    private IHyriPlayerSettings.Language lang;
    private IHyriPlayerManager hyriPlayerManager;

    private ItemStack enItem;
    private ItemStack frItem;
    private ItemStack fillItem;
    private ItemStack closeItem;
    private ItemStack currentLangItem;

    public LanguageGui(Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, "Choix de la Langue", 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.lang = this.hyriSettings.getLanguage();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName("§fQuitter").build();
        this.frItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.FRANCE.getTexture())
                .withName("§fClique pour changer en \"Français\"").build();
        this.enItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ENGLAND.getTexture())
                .withName("§fClique pour changer en \"Anglais\"").build();
        this.currentLangItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName("§fActuellement sur \"" + this.getIndicatorName(this.lang) + "\"").build();

        setFill(this.fillItem);
        setItem(this.getSlot(this.lang), this.currentLangItem);
        setItem(11, this.frItem, e -> {
            this.lang = IHyriPlayerSettings.Language.FR;
            this.hyriSettings.setLanguage(this.lang);
            e.getInventory().remove(this.currentLangItem);
            e.getInventory().setItem(this.getSlot(this.lang), this.updateCurrent(this.currentLangItem, this.lang));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(15, this.enItem, e -> {
            this.lang = IHyriPlayerSettings.Language.EN;
            this.hyriSettings.setLanguage(this.lang);
            e.getInventory().remove(this.currentLangItem);
            e.getInventory().setItem(this.getSlot(this.lang), this.updateCurrent(this.currentLangItem, this.lang));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(22, this.closeItem, e -> {
            this.player.closeInventory();
            this.oldGui.open();
        });
    }

    private ItemStack updateCurrent(ItemStack item, IHyriPlayerSettings.Language lang) {
        return new ItemBuilder(item).withName("§fActuellement sur \"" + this.getIndicatorName(lang) + "\"").build();
    }

    private String getIndicatorName(IHyriPlayerSettings.Language lang) {
        switch (lang) {
            case FR:
                return "Français";
            case EN:
                return "English";
            default:
                return "";
        }
    }

    private int getSlot(IHyriPlayerSettings.Language lang) {
        switch (lang) {
            case FR:
                return 2;
            case EN:
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
