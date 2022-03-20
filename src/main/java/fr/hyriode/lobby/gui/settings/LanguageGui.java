package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.SettingsGui;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHeads;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends LobbyInventory {

    private final HyriLobby plugin;

    private final IHyriPlayer player;
    private final IHyriPlayerManager pm;
    private final IHyriPlayerSettings settings;

    private HyriLanguage language;
    private final ItemStack currentItem;

    public LanguageGui(HyriLobby plugin, Player owner) {
        super(owner, plugin.getHyrame(), "item.language.", "title.language.gui", 27);

        this.plugin = plugin;

        this.pm = HyriAPI.get().getPlayerManager();
        this.player = this.pm.getPlayer(this.owner.getUniqueId());
        this.settings = this.player.getSettings();

        this.language = this.settings.getLanguage();
        this.currentItem = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ARROW_DOWN.getTexture()).build();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        //Items with Consumer part
        this.setItem(11, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.FRANCE.getTexture()).withName(this.getKey("frItem")).build(),
                e -> this.onLangClick(HyriLanguage.FR)
        );
        this.setItem(15, new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).withCustomHead(UsefulHeads.ENGLAND.getTexture()).withName(this.getKey("enItem")).build(),
                e -> this.onLangClick(HyriLanguage.EN)
        );
        this.setItem(22, new ItemBuilder(Material.BARRIER).withName(this.getKey("quit")).build(),
                e -> this.owner.closeInventory()
        );

        //Fill part
        this.setFill(FILL_ITEM);

        this.updateCurrent();
    }

    private void onLangClick(HyriLanguage language) {
        this.language = language;
        this.settings.setLanguage(this.language);

        this.updateCurrent();
        this.setFill(FILL_ITEM);
    }

    private void updateCurrent() {
        this.inventory.remove(this.currentItem);
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.getKey("current") + this.getIndicatorName()).build());
    }

    private String getIndicatorName() {
        switch (this.language) {
            case FR: return this.getKey("frLang");
            case EN: return this.getKey("enLang");
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
        this.pm.sendPlayer(this.player);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> new SettingsGui(this.plugin, this.owner).open(), 1L);
    }
}
