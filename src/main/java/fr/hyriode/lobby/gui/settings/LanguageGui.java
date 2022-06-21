package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyInventory;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.util.UsefulHead;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LanguageGui extends LobbyInventory {

    private HyriLanguage language;
    private final IHyriPlayerSettings settings;

    public LanguageGui(HyriLobby plugin, Player owner) {
        super(owner, plugin, "settings_language", "settings-language", 54);

        this.settings = this.account.getSettings();
        this.language = this.settings.getLanguage();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.fill();

        this.setItem(29, this.getItem(UsefulHead.FRANCE, HyriLanguage.FR), e -> this.onLangClick(e, HyriLanguage.FR));
        this.setItem(33, this.getItem(UsefulHead.ENGLAND, HyriLanguage.EN), e -> this.onLangClick(e, HyriLanguage.EN));

        this.setupCurrentButton(HEAD_ITEM.apply(UsefulHead.ARROW_DOWN).build(), this.getSlot(), slot -> LobbyMessage.CURRENT_ITEM.asLang().getForPlayer(this.owner) + this.getLangMessage(this.language), null);
    }

    private void onLangClick(InventoryClickEvent event, HyriLanguage language) {
        this.language = language;
        this.settings.setLanguage(this.language);
        this.account.update();

        this.updateCurrentButton(this.getSlot(), event);
        this.fill();
    }

    private int getSlot() {
        switch (this.language) {
            case FR: return 29 - 9;
            case EN: return 33 - 9;
            default: return 0;
        }
    }

    private String getLangMessage(HyriLanguage language) {
        return HyriLanguageMessage.get("settings.lang." + language.getCode()).getForPlayer(this.owner);
    }

    private ItemStack getItem(UsefulHead head, HyriLanguage language) {
        return HEAD_ITEM.apply(head).withName(this.getLangMessage(language)).build();
    }
}
