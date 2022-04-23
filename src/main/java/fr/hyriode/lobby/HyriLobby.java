package fr.hyriode.lobby;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.server.IHyriServer;
import fr.hyriode.hyggdrasil.api.lobby.HyggLobbyAPI;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.placeholder.PlaceholderAPI;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.jump.JumpHandler;
import fr.hyriode.lobby.leaderboard.LeaderboardHandler;
import fr.hyriode.lobby.placeholder.LobbyPlaceholder;
import fr.hyriode.lobby.rewards.RewardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HyriLobby extends JavaPlugin {

    private IHyrame hyrame;

    private LeaderboardHandler leaderboard;

    private RewardManager rewardManager;

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

        //TODO Save config
        //HyriAPI.get().getHystiaAPI().getConfigManager().

        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));

        LobbyAPI.get().start(str -> Arrays.asList(str).forEach(s -> sender.sendMessage(ChatColor.DARK_GREEN + s)));

        this.leaderboard = new LeaderboardHandler(this);
        this.rewardManager = new RewardManager();

        IHyrame.WORLD.get().setGameRuleValue("doFireTick", "false");

        PlaceholderAPI.registerHandler(new LobbyPlaceholder(this.hyrame));
        HyriAPI.get().getServer().setSlots(HyggLobbyAPI.MAX_PLAYERS);
        HyriAPI.get().getServer().setState(IHyriServer.State.READY);
    }

    @Override
    public void onDisable() {
        this.hyrame.getListenerManager().getListener(JumpHandler.class).stop();
    }

    public IHyrame getHyrame() {
        return this.hyrame;
    }

    public LeaderboardHandler getLeaderboardHandler() {
        return this.leaderboard;
    }

    public RewardManager getRewardManager() {
        return this.rewardManager;
    }
}
