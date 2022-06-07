package fr.hyriode.hyrilobby;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hyrilobby.config.LobbyConfig;
import fr.hyriode.hyrilobby.player.LobbyPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:15
 */
public class HyriLobby extends JavaPlugin {

    private static IHyriLanguageManager languageManager;

    private IHyrame hyrame;
    private LobbyConfig config;

    private LobbyPlayerManager playerManager;

    @Override
    public void onEnable() {
        final ConsoleCommandSender sender = Bukkit.getConsoleSender();
        sender.sendMessage(ChatColor.GREEN + "  _    _            _ _           _     _           ");
        sender.sendMessage(ChatColor.GREEN + " | |  | |          (_) |         | |   | |          ");
        sender.sendMessage(ChatColor.GREEN + " | |__| |_   _ _ __ _| |     ___ | |__ | |__  _   _ ");
        sender.sendMessage(ChatColor.GREEN + " |  __  | | | | '__| | |    / _ \\| '_ \\| '_ \\| | | |");
        sender.sendMessage(ChatColor.GREEN + " | |  | | |_| | |  | | |___| (_) | |_) | |_) | |_| |");
        sender.sendMessage(ChatColor.GREEN + " |_|  |_|\\__, |_|  |_|______\\___/|_.__/|_.__/ \\__, |");
        sender.sendMessage(ChatColor.GREEN + "          __/ |                                __/ |");
        sender.sendMessage(ChatColor.GREEN + "         |___/                                |___/ ");

        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));

        this.config = new LobbyConfig(
                new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 190.5, 0.5, 90, 0),
                new LocationWrapper(new Location(IHyrame.WORLD.get(), -277, 150, -39, 90, 0)),
                new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 188.0, -23.5, -180, 0),
                new LocationWrapper(IHyrame.WORLD.get().getUID(),-348.5, 163, -53.5, 135, 0),
                new LocationWrapper(IHyrame.WORLD.get().getUID(), 0.5, 188, -26.5, -180, 0),
                Arrays.asList(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(),-68.5, 197, -116.5, 90, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -165.5, 203, -129.5, 90, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -268.5, 220, -146.5, 90, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -301.5, 236, -67.5, 0, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -284.5, 244, 13.5, 0, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -195.5, 228, 84.5, 0, 0),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -213.5, 232, 116.5, -90, 0)
                ),
                new LocationWrapper(IHyrame.WORLD.get().getUID(), -153.5, 237, 107.5),
                new LobbyConfig.Zone(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -283, 150, -32),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -289, 182, -48)),
                new LobbyConfig.Zone(
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -369, 156, -49),
                        new LocationWrapper(IHyrame.WORLD.get().getUID(), -343, 149, -78)));

        languageManager = this.hyrame.getLanguageManager();

        this.playerManager = new LobbyPlayerManager(this);

        HyriAPI.get().getServer().setState(IHyriServer.State.READY);
        HyriAPI.get().getServer().setSlots(HyggLobbyAPI.MAX_PLAYERS);
    }

    @Override
    public void onDisable() {
        this.playerManager.handleStop();
    }

    public IHyrame getHyrame() {
        return hyrame;
    }

    public LobbyConfig getConfiguration() {
        return this.config;
    }

    public LobbyPlayerManager getPlayerManager() {
        return playerManager;
    }

    public static IHyriLanguageManager getLanguageManager() {
        return languageManager;
    }
}
