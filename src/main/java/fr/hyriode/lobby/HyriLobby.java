package fr.hyriode.lobby;

import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.jump.JumpHandler;
import fr.hyriode.lobby.leaderboard.LeaderboardHandler;
import fr.hyriode.lobby.utils.LobbyPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HyriLobby extends JavaPlugin {

    private IHyrame hyrame;

    private LeaderboardHandler leaderboard;

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

        LobbyAPI.get().start(str -> Arrays.asList(str).forEach(s -> sender.sendMessage(ChatColor.DARK_GREEN + s)));

        this.leaderboard = new LeaderboardHandler(this);

        LobbyPermission.register();
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
}
