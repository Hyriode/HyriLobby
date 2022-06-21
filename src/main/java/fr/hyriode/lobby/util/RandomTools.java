package fr.hyriode.lobby.util;

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
        final String base = "unit.";
        final StringBuilder builder = new StringBuilder();

        if(duration.toMonthsPart() != 0) {
            builder.append(duration.toMonthsPart())
                    .append(getMessage(player, base + "months"));
        }
        if(duration.toDaysPart() != 0) {
            builder.append(duration.toDaysPart())
                    .append(getMessage(player, base + "days"));
        }
        if(duration.toHoursPart() != 0) {
            builder.append(duration.toHoursPart())
                    .append(getMessage(player, base + "hours"));
        }
        if(duration.toMinutesPart() != 0) {
            builder.append(duration.toMinutesPart())
                    .append(getMessage(player, base + "minutes"));
        }
        if(duration.toSecondsPart() != 0) {
            builder.append(duration.toSecondsPart())
                    .append(getMessage(player, base + "seconds"));
        }

        return builder.toString();
    }

    private static String getMessage(Player player, String key) {
        return HyriLanguageMessage.get(key).getForPlayer(player);
    }
}