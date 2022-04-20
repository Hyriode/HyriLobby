package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends LobbyInventory {

    private HyriLanguage language;
    private final ItemStack currentItem;
    private final IHyriPlayerSettings settings;

    public LanguageGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "language", 27);

        this.settings = this.account.getSettings();

        this.language = this.settings.getLanguage();
        this.currentItem = HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.setFill(FILL_ITEM);
        this.placeQuitButton(e -> this.owner.closeInventory());

        //Items with Consumer part
        this.setItem(11, HEAD_ITEM.apply(UsefulHead.FRANCE).withName(this.getMessage("name")).build(), e -> this.onLangClick(HyriLanguage.FR));
        this.setItem(15, HEAD_ITEM.apply(UsefulHead.ENGLAND).withName(this.getMessage("name")).build(), e -> this.onLangClick(HyriLanguage.EN));

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
        this.setItem(this.getSlot(), new ItemBuilder(this.currentItem).withName(this.getMessage("current")).build());
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
        this.playerManager.sendPlayer(this.account);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> new SettingsGui(this.plugin, this.owner).open(), 1L);
    }
}
