package fr.hyriode.lobby;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.plugin.IPluginProvider;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.config.LobbyConfig;
import fr.hyriode.lobby.player.LobbyPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:15
 */
public class HyriLobby extends JavaPlugin {

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

        this.hyrame = HyrameLoader.load(new Provider());

        final UUID worldId = IHyrame.WORLD.get().getUID();
        
        this.config = new LobbyConfig(
                new LocationWrapper(worldId, 0.5, 190.5, 0.5, 90, 0),
                new LocationWrapper(worldId, -277, 150, -39, 90, 0),
                new LocationWrapper(worldId, 0.5, 188.0, -23.5, -180, 0),
                new LocationWrapper(worldId,-348.5, 163, -53.5, 135, 0),
                new LocationWrapper(worldId, 0.5, 188, -26.5, -180, 0),
                Arrays.asList(
                        new LocationWrapper(worldId,-68.5, 197, -116.5, 90, 0),
                        new LocationWrapper(worldId, -165.5, 203, -129.5, 90, 0),
                        new LocationWrapper(worldId, -268.5, 220, -146.5, 90, 0),
                        new LocationWrapper(worldId, -301.5, 236, -67.5, 0, 0),
                        new LocationWrapper(worldId, -284.5, 244, 13.5, 0, 0),
                        new LocationWrapper(worldId, -195.5, 228, 84.5, 0, 0),
                        new LocationWrapper(worldId, -213.5, 232, 116.5, -90, 0)
                ),
                new LocationWrapper(worldId, -153.5, 237, 107.5),
                new LobbyConfig.Zone(
                        new LocationWrapper(worldId, -283, 150, -32),
                        new LocationWrapper(worldId, -289, 182, -48)),
                new LobbyConfig.Zone(
                        new LocationWrapper(worldId, -369, 156, -49),
                        new LocationWrapper(worldId, -343, 149, -78)));

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

    private class Provider implements IPluginProvider {

        private static final String PACKAGE = "fr.hyriode.lobby";

        @Override
        public JavaPlugin getPlugin() {
            return HyriLobby.this;
        }

        @Override
        public String getId() {
            return "lobby";
        }

        @Override
        public String[] getCommandsPackages() {
            return new String[] {PACKAGE};
        }

        @Override
        public String[] getListenersPackages() {
            return new String[] {PACKAGE};
        }

        @Override
        public String[] getItemsPackages() {
            return new String[] {PACKAGE};
        }

        @Override
        public String getLanguagesPath() {
            return "/lang/";
        }

    }

}
