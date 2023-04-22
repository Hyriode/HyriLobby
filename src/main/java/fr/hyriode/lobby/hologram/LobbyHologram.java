package fr.hyriode.lobby.hologram;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyHologram {
    private final JavaPlugin plugin;
    private final LocationWrapper location;
    private final LobbyMessage message;

    public LobbyHologram(JavaPlugin plugin, LocationWrapper location, LobbyMessage message) {
        this.plugin = plugin;
        this.location = location;
        this.message = message;

    }

    public Hologram asHologram(HyriLanguage language) {
         final Hologram.Builder builder = new Hologram.Builder(this.plugin)
                 .withLocation(this.location.asBukkit());

         for (String message : this.message.asList(language)) {
             builder.withLine(message);
         }

         return builder.build();
    }
}
