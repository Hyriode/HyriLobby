package fr.hyriode.lobby.hologram;

import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrame.hologram.Hologram;
import fr.hyriode.hyrame.utils.LocationWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LobbyHologramManager {

    private final List<Hologram> holograms;
    public LobbyHologramManager(JavaPlugin plugin) {
        this.holograms = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(new Handler(), plugin);

        this.registerHologram(new Hologram.Builder(plugin, new LocationWrapper(0D, 200D, 0D).asBukkit()));
    }

    private void registerHologram(Hologram.Builder hologramBuilder) {

        this.holograms.add(hologramBuilder.build());
    }

    private class Handler implements Listener {
        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            final Player player = event.getPlayer();

            if(IHyriPlayer.get(player.getUniqueId()).getSettings().getLanguage() == HyriLanguage.FR) {
                LobbyHologramManager.this.holograms.forEach(hologram -> hologram.addReceiver(player));
            }

        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            final Player player = event.getPlayer();

            for (Hologram hologram : LobbyHologramManager.this.holograms) {
                hologram.removeReceiver(player);
            }
        }
    }
}
