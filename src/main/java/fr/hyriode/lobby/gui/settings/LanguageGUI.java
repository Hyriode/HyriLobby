package fr.hyriode.lobby.gui.settings;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.model.IHyriPlayerSettings;
import fr.hyriode.hyrame.item.ItemBuilder;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.LobbyGUI;
import fr.hyriode.lobby.language.Language;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class LanguageGUI extends LobbyGUI {

    private final IHyriPlayerSettings settings;

    public LanguageGUI(HyriLobby plugin, Player owner) {
        super(owner, plugin, () -> "settings-language", 54);
        this.settings = this.account.getSettings();

        this.init();
    }

    @Override
    protected void init() {
        this.inventory.clear();

        this.applyDesign(Design.BORDER);

        this.setLanguageItem(21, Language.FRENCH);
        this.setLanguageItem(23, Language.ENGLISH);
    }

    private void setLanguageItem(int slot, Language language) {
        this.setItem(slot, this.createItem(language), event -> {
            final HyriLanguage newLang = language.getInitial();

            if (this.settings.getLanguage() == newLang) {
                return;
            }

            this.settings.setLanguage(newLang);
            this.account.update();
            this.owner.closeInventory();

            final IHyriPlayer newAccount = IHyriPlayer.get(this.owner.getUniqueId());

            this.owner.sendMessage(LobbyMessage.LANG_UPDATED_MESSAGE.asString(newAccount).replace("%lang%", language.getDisplay(newAccount)));
        });
    }

    private ItemStack createItem(Language language) {
        final HyriLanguage initial = language.getInitial();
        final boolean usingLang = this.settings.getLanguage() == initial;

        return ItemBuilder.asHead()
                .withName(ChatColor.AQUA + language.getDisplay().getValue(initial) + (usingLang ? ChatColor.GREEN + " " + Symbols.TICK_BOLD : ""))
                .withLore(usingLang ? LobbyMessage.LANG_ITEM_USING_LORE.asLang().getValue(initial) : LobbyMessage.LANG_ITEM_LORE.asLang().getValue(initial))
                .withHeadTexture(language)
                .build();
    }

}
