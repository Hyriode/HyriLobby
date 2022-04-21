package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.utils.LobbyInventory;
import fr.hyriode.lobby.utils.UsefulHead;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends LobbyInventory {

    private HyriLanguage language;
    private final IHyriPlayerSettings settings;

    public LanguageGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings", "settings.language", 45);

        this.settings = this.account.getSettings();
        this.language = this.settings.getLanguage();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fillOutline(FILL_ITEM);

        this.setItem(20, this.getItem(UsefulHead.FRANCE, HyriLanguage.FR), e -> this.onLangClick(e, HyriLanguage.FR));
        this.setItem(24, this.getItem(UsefulHead.ENGLAND, HyriLanguage.EN), e -> this.onLangClick(e, HyriLanguage.EN));

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), this.getSlot(), slot -> this.getMessage("current") + this.getLangMessage(this.language), null);
        this.setupReturnButton(new SettingsGui(this.plugin, this.owner), null);
    }

    private void onLangClick(InventoryClickEvent event, HyriLanguage language) {
        this.language = language;
        this.settings.setLanguage(this.language);

        this.updateCurrentButton(this.getSlot(), event);
        this.fillOutline(FILL_ITEM);
    }

    private int getSlot() {
        switch (this.language) {
            case FR: return 20 - 9;
            case EN: return 24 - 9;
            default: return 0;
        }
    }

    private String getLangMessage(HyriLanguage language) {
        return this.getMessage("placeholder", "settings.lang." + language.getCode());
    }

    private ItemStack getItem(UsefulHead head, HyriLanguage language) {
        return HEAD_ITEM.apply(head).withName(this.getMessage("button") + this.getLangMessage(language)).build();
    }
}
