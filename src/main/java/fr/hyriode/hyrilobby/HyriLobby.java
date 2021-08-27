package fr.hyriode.hyrilobby;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyrilobby.listener.PlayerHandler;
import fr.hyriode.hyrilobby.scoreboard.ScoreboardManager;
import fr.hyriode.hyrilobby.tab.TabManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class HyriLobby extends JavaPlugin {

    /** Logger */
    private final Logger logger = getLogger();

    /** Tab */
    private TabManager tabManager;

    /** Scoreboard */
    private ScoreboardManager scoreboardManager;

    /** API */
    private HyriAPI api;

    @Override
    public void onEnable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={     Welcome to HyriLobby     }=====#");
        this.logger.info("#====={      The plugin is now       }=====#");
        this.logger.info("#====={  loading... Author: Akkashi  }=====#");
        this.logger.info("#====={------------------------------}=====#");

        this.api = HyriAPI.get();

        this.registerManagers();
        this.registerCommands();
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={   HyriLobby is now disabled  }=====#");
        this.logger.info("#====={    Thanks using HyriLobby !  }=====#");
        this.logger.info("#====={------------------------------}=====#");
    }

    private void registerManagers() {
       this.scoreboardManager = new ScoreboardManager(this);
       this.tabManager = new TabManager();
    }

    private void registerListeners() {
        final PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(new PlayerHandler(this), this);
    }

    private void registerCommands() {

    }

    private void fastCmd(String command, CommandExecutor executor) {
        this.getCommand(command).setExecutor(executor);

        this.logger.info("Registered command: " +command);
    }

    public HyriAPI getAPI() {
        return this.api;
    }

    public TabManager getTabManager() {
        return this.tabManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

}
