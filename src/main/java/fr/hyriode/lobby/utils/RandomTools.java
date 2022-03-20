package fr.hyriode.lobby.utils;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.settings.HyriLanguage;
import net.md_5.bungee.api.ChatColor;

import java.time.Duration;
import java.util.UUID;

public class RandomTools {

    public static String getPrefix(boolean error) {
        return ChatColor.RESET + "[" + ChatColor.AQUA + "HyriJump" + ChatColor.RESET + "] " + (error ? ChatColor.RED : ChatColor.RESET);
    }

    public static String getDurationMessage(HyriDuration duration, UUID uuid) {
        final HyriLanguage lang = HyriAPI.get().getPlayerSettingsManager().getPlayerSettings(uuid).getLanguage();
        return(lang == HyriLanguage.EN ?
                ((duration.toHoursPart() != 0 ? duration.toHoursPart() + " hours " : "") + (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " minutes and " : "") + duration.toSecondsPart() + " seconds")
                : (lang == HyriLanguage.FR ?
                ((duration.toHoursPart() != 0 ? duration.toHoursPart() + " heures " : "") + (duration.toMinutesPart() != 0 ? duration.toMinutesPart() + " minutes et " : "") + duration.toSecondsPart() + " secondes")
                : ""));
    }

    public static class HyriDuration {

        private final long seconds;

        public HyriDuration(long seconds) {
            this.seconds = seconds;
        }

        public HyriDuration(Duration duration) {
            this.seconds = duration.getSeconds();
        }

        public long toDaysPart() {
            return this.seconds / 86400;
        }

        public int toHoursPart() {
            return (int) ((this.seconds / (60 * 60)) % 24);
        }

        public int toMinutesPart() {
            return (int) ((this.seconds / 60) % 60);
        }

        public int toSecondsPart() {
            return (int) (this.seconds % 60);
        }

        public int toSeconds() {
            return (int) this.seconds;
        }
    }
}
