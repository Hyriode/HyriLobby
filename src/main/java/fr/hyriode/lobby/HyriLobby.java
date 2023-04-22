package fr.hyriode.lobby;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.ILobbyAPI;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.plugin.IPluginProvider;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.config.LobbyConfig;
import fr.hyriode.lobby.game.LobbyGameManager;
import fr.hyriode.lobby.hologram.LobbyHologramManager;
import fr.hyriode.lobby.host.HostHandler;
import fr.hyriode.lobby.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.listener.AccountListener;
import fr.hyriode.lobby.listener.InternalListener;
import fr.hyriode.lobby.npc.LobbyNPCManager;
import fr.hyriode.lobby.player.LobbyPlayerManager;
import fr.hyriode.lobby.queue.HostQueueHandler;
import fr.hyriode.lobby.queue.NormalQueueHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HyriLobby extends JavaPlugin {

    private IHyrame hyrame;
    private LobbyConfig config;

    private LobbyPlayerManager playerManager;
    private LobbyGameManager gameManager;
    private LobbyNPCManager npcManager;
    private LobbyLeaderboardManager leaderboardManager;
    private HostHandler hostHandler;
    private LobbyHologramManager hologramManager;

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
        
        this.config = new LobbyConfig(
                new LocationWrapper(0.5, 190.5, 0.5, 90, 0),
                new LocationWrapper(-277, 150, -39, 90, 0),
                new LocationWrapper(0.5, 188.0, -23.5, -180, 0),
                new LocationWrapper(-348.5, 163, -53.5, 135, 0),
                new LocationWrapper(0.5, 188, -26.5, -180, 0),
                Arrays.asList(
                        new LocationWrapper(-68.5, 197, -116.5, 90, 0),
                        new LocationWrapper(-165.5, 203, -129.5, 90, 0),
                        new LocationWrapper(-268.5, 220, -146.5, 90, 0),
                        new LocationWrapper(-301.5, 236, -67.5, 0, 0),
                        new LocationWrapper(-284.5, 244, 13.5, 0, 0),
                        new LocationWrapper(-195.5, 228, 84.5, 0, 0),
                        new LocationWrapper(-213.5, 232, 116.5, -90, 0)
                ),
                new LocationWrapper(-153.5, 237, 107.5),
                new LobbyConfig.Zone(
                        new LocationWrapper(-282, 150, 13),
                        new LocationWrapper(-386, 225, -94)),
                new LobbyConfig.Zone(
                        new LocationWrapper(-369, 156, -49),
                        new LocationWrapper(-343, 149, -78)));

        this.playerManager = new LobbyPlayerManager(this);
        this.gameManager = new LobbyGameManager();
        this.npcManager = new LobbyNPCManager(this);
        this.leaderboardManager = new LobbyLeaderboardManager(this);
        this.hologramManager = new LobbyHologramManager(this);

        if(!HyriAPI.get().getConfig().isDevEnvironment()) {
            this.hostHandler = new HostHandler(this);
        }


        HyriAPI.get().getEventBus().register(new AccountListener(this));
        HyriAPI.get().getNetworkManager().getEventBus().register(new InternalListener(this));
        HyriAPI.get().getQueueManager().addHandler(new NormalQueueHandler(this));
        HyriAPI.get().getQueueManager().addHandler(new HostQueueHandler(this));


        HyriAPI.get().getServer().setSlots(ILobbyAPI.MAX_PLAYERS);
        HyriAPI.get().getServer().setState(HyggServer.State.READY);
    }

    @Override
    public void onDisable() {
        this.playerManager.handleStop();
    }

    public LobbyConfig config() {
        return this.config;
    }

    public IHyrame getHyrame() {
        return this.hyrame;
    }

    public LobbyPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public LobbyGameManager getGameManager() {
        return this.gameManager;
    }

    public LobbyNPCManager getNPCManager() {
        return this.npcManager;
    }

    public LobbyLeaderboardManager getLeaderboardManager() {
        return this.leaderboardManager;
    }

    public LobbyHologramManager getHologramManager() {
        return hologramManager;
    }

    public HostHandler getHostHandler() {
        return this.hostHandler;
    }

    /**
     * The provider instance for {@linkplain IHyrame Hyrame}
     */
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
