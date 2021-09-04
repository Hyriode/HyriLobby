package fr.hyriode.hyrilobby.gui.settings;

import fr.hyriode.common.inventory.AbstractInventory;
import fr.hyriode.common.item.ItemBuilder;
import fr.hyriode.hyrame.language.LanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.gui.SettingsGui;
import fr.hyriode.hyrilobby.util.UsefulHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends AbstractInventory {

    private Player player;
    private SettingsGui oldGui;
    private LanguageManager lang;
    private IHyriPlayer hyriPlayer;
    private IHyriPlayerSettings hyriSettings;
    private IHyriPlayerSettings.Language language;
    private IHyriPlayerManager hyriPlayerManager;

    private ItemStack enItem;
    private ItemStack frItem;
    private ItemStack fillItem;
    private ItemStack closeItem;
    private ItemStack currentLangItem;

    public LanguageGui(Player owner, IHyriPlayer player, IHyriPlayerManager manager, SettingsGui oldGui) {
        super(owner, HyriLobby.getInstance().getLanguageManager().getMessageForPlayer(owner, "title.language.gui"), 27);

        this.player = owner;
        this.oldGui = oldGui;
        this.hyriPlayer = player;
        this.hyriPlayerManager = manager;
        this.hyriSettings = this.hyriPlayer.getSettings();
        this.language = this.hyriSettings.getLanguage();
        this.lang = HyriLobby.getInstance().getLanguageManager();

        this.fillItem = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15)
                .withName(" ").build();
        this.closeItem = new ItemBuilder(Material.BARRIER)
                .withName(this.lang.getMessageForPlayer(this.player, "item.language.quit")).build();
        this.frItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.FRANCE.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.language.frItem")).build();
        this.enItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ENGLAND.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.language.enItem")).build();
        this.currentLangItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3)
                .withCustomHead(UsefulHeads.ARROW_DOWN.getTexture())
                .withName(this.lang.getMessageForPlayer(this.player, "item.language.current") + this.getIndicatorName(this.language)).build();

        setFill(this.fillItem);
        setItem(this.getSlot(this.language), this.currentLangItem);
        setItem(11, this.frItem, e -> {
            this.language = IHyriPlayerSettings.Language.FR;
            this.hyriSettings.setLanguage(this.language);
            e.getInventory().remove(this.currentLangItem);
            e.getInventory().setItem(this.getSlot(this.language), this.updateCurrent(this.currentLangItem, this.language));
            setFill(this.fillItem);
            this.player.updateInventory();
        });
        setItem(15, this.enItem, e -> {
            this.language = IHyriPlayerSettings.Language.EN;
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

    private ItemStack updateCurrent(ItemStack item, IHyriPlayerSettings.Language language) {
        return new ItemBuilder(item).withName(this.lang.getMessageForPlayer(this.player, "item.language.current") + this.getIndicatorName(language)).build();
    }

    private String getIndicatorName(IHyriPlayerSettings.Language language) {
        switch (language) {
            case FR:
                return this.lang.getMessageForPlayer(this.player, "item.language.frLang");
            case EN:
                return this.lang.getMessageForPlayer(this.player, "item.language.enLang");
            default:
                return "";
        }
    }

    private int getSlot(IHyriPlayerSettings.Language language) {
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
