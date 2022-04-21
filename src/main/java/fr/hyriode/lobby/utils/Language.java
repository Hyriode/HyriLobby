package fr.hyriode.lobby.utils;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyrame.placeholder.PlaceholderAPI;
import org.bukkit.entity.Player;

public class Language {

    public static String getMessage(Player player, String key) {
        return PlaceholderAPI.setPlaceholders(player, IHyriLanguageManager.Provider.get().getValue(player, key));
    }

    public static HyriLanguageMessage getMessage(String key) {
        return IHyriLanguageManager.Provider.get().getMessage(key);
    }
}
