package fr.hyriode.lobby.placeholder;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.settings.IHyriPlayerSettings;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyrame.placeholder.PlaceholderPrefixHandler;
import org.bukkit.entity.Player;

public class LobbyPlaceholder extends PlaceholderPrefixHandler {

    final IHyriLanguageManager lang;

    public LobbyPlaceholder(IHyrame hyrame) {
        super("lobby");

        this.lang = hyrame.getLanguageManager();;
    }

    @Override
    public String handle(Player player, String placeholder) {
        final IHyriPlayerSettings settings = HyriAPI.get().getPlayerSettingsManager().getPlayerSettings(player.getUniqueId());

        switch (placeholder) {
            case "hotbar_click": return this.lang.getValue(player, "item.global.hotbar.click");
            case "settings_click": return this.lang.getValue(player, "item.global.settings.click");
            case "settings_current": return this.lang.getValue(player, "item.global.settings.current");
            case "settings_lang": return this.lang.getValue(player, this.lang.getValue(player, "item.global.settings.lang." + settings.getLanguage().getCode()));
            case "settings_visibility": return this.lang.getValue(player, "item.global.settings.level." + settings.getPlayersVisibilityLevel().name().toLowerCase());
            case "settings_mp": return this.lang.getValue(player, "item.global.settings.level." + settings.getPrivateMessagesLevel().name().toLowerCase());
        }
        return null;
    }
}
