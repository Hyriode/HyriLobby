package fr.hyriode.hyrilobby.util;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.utils.DurationConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RandomTools {


    public static String getDurationMessage(DurationConverter duration, UUID uuid) {
        return RandomTools.getDurationMessage(duration, Bukkit.getPlayer(uuid));
    }

    public static String getDurationMessage(DurationConverter duration, Player player) {
        final String base = "item.units.";

        return (duration.toMonthsPart() != 0 ? duration.toMonthsPart() + " " + getMessage(player, base + "months"): "") + " " +
                (duration.toDaysPart() != 0 ? duration.toDaysPart() + " " + getMessage(player, base + "days"): "") + " " +
                (duration.toHoursPart() != 0 ? duration.toHoursPart() + " " + getMessage(player, base + "hours") : "") + " " +
                (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " " + getMessage(player, base + "minutes"): "") + " " +
                (duration.toSecondsPart() != 0 ? duration.toSecondsPart() + " " + getMessage(player, base + "seconds") : "");
    }

    private static String getMessage(Player player, String key) {
        return HyriLanguageMessage.get(key).getForPlayer(player);
    }
}