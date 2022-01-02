package fr.hyriode.lobby.gui.settings;

import fr.hyriode.hyrame.inventory.HyriInventory;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.player.IHyriPlayer;
import fr.hyriode.hyriapi.player.IHyriPlayerManager;
import fr.hyriode.hyriapi.settings.HyriLanguage;
import fr.hyriode.hyriapi.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.util.References;
import fr.hyriode.lobby.util.UsefulHeads;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends HyriInventory {

    private static final String BASE = "item.language.";

    private final SettingsGui oldGui;
    private final IHyriLanguageManager lang;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;
    private HyriLanguage language;

    private final ItemStack currentItem;


    public LanguageGui(HyriLobby plugin, Player owner, SettingsGui oldGui) {
        super(owner, plugin.getHyrame().getLanguageManager().getValue(owner, "title.language.gui"), 27);

        this.oldGui = oldGui;
        this.lang = plugin.getHyrame().getLanguageManager();

        this.pm = plugin.getAPI().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());

        this.settings = this.player.getSettings();
        this.language = this.settings.getLanguage();

        this.currentItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ARROW_DOWN.getTexture()).build();

        this.init();
    }

    private void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.setItem(11, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.FRANCE.getTexture()).withName(this.lang.getValue(this.player, BASE + "frItem")).build(),
                e -> this.onLangClick(HyriLanguage.FR)
        );
        this.setItem(15, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ENGLAND.getTexture()).withName(this.lang.getValue(this.player, BASE + "enItem")).build(),
                e -> this.onLangClick(HyriLanguage.EN)
        );
        this.setItem(22, new ItemBuilder(Material.BARRIER).withName(this.lang.getValue(this.player, BASE + "quit")).build(),
                e -> this.owner.closeInventory()
        );

        //Fill part
        this.setFill(References.FILL_ITEM);

        this.updateCurrent();
    }

    private void onLangClick(HyriLanguage language) {
        this.language = language;
        this.settings.setLanguage(this.language);

        this.updateCurrent();
        this.setFill(References.FILL_ITEM);
    }

    private void updateCurrent() {
        this.owner.getInventory().remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.lang.getValue(this.player, BASE + "current") + this.getIndicatorName()).build());
    }

    private String getIndicatorName() {
        switch (this.language) {
            case FR: return this.lang.getValue(this.player, BASE + "frLang");
            case EN: return this.lang.getValue(this.player, BASE + "enLang");
            default: return "";
        }
    }

    private int getSlot() {
        switch (this.language) {
            case FR: return 2;
            case EN: return 6;
            default: return 0;
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        this.oldGui.open();
        this.pm.sendPlayer(this.player);
    }
}
