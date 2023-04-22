package fr.hyriode.lobby.hologram;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LobbyHologramManager {

    private final List<Hologram> frenchHolograms;
    private final List<Hologram> englishHolograms;
    public LobbyHologramManager(JavaPlugin plugin) {
        this.frenchHolograms = new ArrayList<>();
        this.englishHolograms = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(new Handler(), plugin);

        this.registerHologram(new LobbyHologram(plugin, new LocationWrapper(-342D, 166D, -24D), LobbyMessage.HOLOGRAM_CASINO_ROLLER_MACHINES_DESCRIPTION));
    }

    private void registerHologram(LobbyHologram lobbyHologram) {
        this.frenchHolograms.add(lobbyHologram.asHologram(HyriLanguage.FR));
        this.englishHolograms.add(lobbyHologram.asHologram(HyriLanguage.EN));
    }

    public List<Hologram> getFrenchHolograms() {
        return frenchHolograms;
    }

    public List<Hologram> getEnglishHolograms() {
        return englishHolograms;
    }

    private class Handler implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            final Player player = event.getPlayer();

            if(IHyriPlayer.get(player.getUniqueId()).getSettings().getLanguage() == HyriLanguage.FR) {
                LobbyHologramManager.this.frenchHolograms.forEach(hologram -> hologram.addReceiver(player));
            } else {
                LobbyHologramManager.this.englishHolograms.forEach(hologram -> hologram.addReceiver(player));
            }
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            final Player player = event.getPlayer();

            if(IHyriPlayer.get(player.getUniqueId()).getSettings().getLanguage() == HyriLanguage.FR) {
                LobbyHologramManager.this.frenchHolograms.forEach(hologram -> hologram.removeReceiver(player));
            } else {
                LobbyHologramManager.this.englishHolograms.forEach(hologram -> hologram.removeReceiver(player));
            }
        }
    }
}
