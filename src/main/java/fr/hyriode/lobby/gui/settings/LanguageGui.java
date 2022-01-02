package fr.hyriode.lobby.gui.settings;

import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriLanguage;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.util.UsefulHeads;
import fr.hyriode.tools.inventory.AbstractInventory;
import fr.hyriode.tools.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends AbstractInventory {

    private final Player player;
    private final SettingsGui oldGui;
    private final IHyriPlayer hyriPlayer;
    private final IHyriLanguageManager lang;
    private final IHyriPlayerSettings hyriSettings;
    private final IHyriPlayerManager hyriPlayerManager;

    private HyriLanguage language;

    private final ItemStack enItem;
    private final ItemStack frItem;
    private final ItemStack fillItem;
    private final ItemStack closeItem;
    private final ItemStack currentLangItem;

    public LanguageGui(HyriLobby plugin, Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, plugin.getHyrame().getLanguageManager().getValue(owner, "title.language.gui"), 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.language = this.hyriSettings.getLanguage();
        this.lang = plugin.getHyrame().getLanguageManager();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getValue(this.player, "item.language.quit")).build();
        this.frItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.FRANCE.getTexture())
                .withName(this.lang.getValue(this.player, "item.language.frItem")).build();
        this.enItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ENGLAND.getTexture())
                .withName(this.lang.getValue(this.player, "item.language.enItem")).build();
        this.currentLangItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName(this.lang.getValue(this.player, "item.language.current") + this.getIndicatorName(this.language)).build();

        setFill(this.fillItem);
        setItem(this.getSlot(this.language), this.currentLangItem);
        setItem(11, this.frItem, e -> {
            this.language = HyriLanguage.FR;
            this.hyriSettings.setLanguage(this.language);
            e.getInventory().remove(this.currentLangItem);
            e.getInventory().setItem(this.getSlot(this.language), this.updateCurrent(this.currentLangItem, this.language));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(15, this.enItem, e -> {
            this.language = HyriLanguage.EN;
            this.hyriSettings.setLanguage(this.language);
            e.getInventory().remove(this.currentLangItem);
            e.getInventory().setItem(this.getSlot(this.language), this.updateCurrent(this.currentLangItem, this.language));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(22, this.closeItem, e -> {
            this.player.closeInventory();
            this.oldGui.open();
        });
    }

    private ItemStack updateCurrent(ItemStack item, HyriLanguage language) {
        return new ItemBuilder(item).withName(this.lang.getValue(this.player, "item.language.current") + this.getIndicatorName(language)).build();
    }

    private String getIndicatorName(HyriLanguage language) {
        switch (language) {
            case FR:
                return this.lang.getValue(this.player, "item.language.frLang");
            case EN:
                return this.lang.getValue(this.player, "item.language.enLang");
            default:
                return "";
        }
    }

    private int getSlot(HyriLanguage language) {
        switch (language) {
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
