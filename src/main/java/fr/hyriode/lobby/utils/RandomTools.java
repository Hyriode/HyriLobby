package fr.hyriode.lobby.utils;

import fr.hyriode.hyrame.utils.DurationConverter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RandomTools {

    public static String getPrefix(String name, boolean error) {
        return ChatColor.RESET + "[" + ChatColor.AQUA + name + ChatColor.RESET + "] " + (error ? ChatColor.RED : ChatColor.RESET);
    }

    public static String getDurationMessage(DurationConverter duration, UUID uuid) {
        return RandomTools.getDurationMessage(duration, Bukkit.getPlayer(uuid));
    }

    public static String getDurationMessage(DurationConverter duration, Player player) {
        final String base = "item.units.";

        return (duration.toDaysPart() != 0 ? duration.toDaysPart() + " " + Language.getMessage(player, base + "days"): "") + " " +
                (duration.toHoursPart() != 0 ? duration.toHoursPart() + " " + Language.getMessage(player, base + "hours") : "") + " " +
                (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " " + Language.getMessage(player, base + "minutes"): "") + " " +
                (duration.toSecondsPart() != 0 ? duration.toSecondsPart() + " " + Language.getMessage(player, base + "seconds") : "");
    }
}
