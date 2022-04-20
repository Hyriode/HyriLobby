package fr.hyriode.lobby.utils;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.settings.HyriLanguage;
import fr.hyriode.hyrame.utils.DurationConverter;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

public class RandomTools {

    public static String getPrefix(boolean error) {
        return ChatColor.RESET + "[" + ChatColor.AQUA + "HyriJump" + ChatColor.RESET + "] " + (error ? ChatColor.RED : ChatColor.RESET);
    }

    public static String getDurationMessage(DurationConverter duration, UUID uuid) {
        final HyriLanguage lang = HyriAPI.get().getPlayerSettingsManager().getPlayerSettings(uuid).getLanguage();
        return(lang == HyriLanguage.EN ?
                ((duration.toHoursPart() != 0 ? duration.toHoursPart() + " hours " : "") + (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " minutes and " : "")
                        + duration.toSecondsPart() + " seconds")
                : (lang == HyriLanguage.FR ?
                ((duration.toHoursPart() != 0 ? duration.toHoursPart() + " heures " : "") + (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " minutes et " : "")
                        + duration.toSecondsPart() + " secondes")
                : ""));
    }
}
