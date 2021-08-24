package fr.hyriode.hyrilobby;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyrilobby.events.PlayerJoinListener;
import fr.hyriode.hyrilobby.events.PlayerQuitListener;
import fr.hyriode.hyrilobby.scoreboard.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private HyriAPI api;
    private Logger logger = getLogger();
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;
    private ScoreBoardManager scoreBoardManager;

    @Override
    public void onEnable() {
        logger.info("#====={------------------------------}=====#");
        logger.info("#====={     Welcome to HyriLobby     }=====#");
        logger.info("#====={      The plugin is now       }=====#");
        logger.info("#====={  loading... Author: Akkahsi  }=====#");
        logger.info("#====={------------------------------}=====#");
        logger.info("Registering instance...");
        INSTANCE = this;
        logger.info("Instance registered !");
        logger.info("#============================#");
        logger.info("Registering API...");
        api = HyriAPI.get();
        logger.info("API registered !");
        logger.info("#============================#");
        logger.info("Registering managers...");
        registerManagers();
        logger.info("Managers registered !");
        logger.info("#============================#");
        logger.info("Registering commands...");
        registerCommands();
        logger.info("Commands registered !");
        logger.info("#============================#");
        logger.info("Registering listeners...");
        registerListeners();
        logger.info("Listeners registered !");
        logger.info("#============================#");
        logger.info("Registering other stuff...");
        this.scheduledExecutorService = Executors.newScheduledThreadPool(16);
        this.executorMonoThread = Executors.newScheduledThreadPool(1);
        logger.info("Other stuff registered !");
        logger.info("#====={------------------------------}=====#");
        logger.info("#====={    HyriLobby is now loaded   }=====#");
        logger.info("#====={    Thanks using HyriLobby !  }=====#");
        logger.info("#====={------------------------------}=====#");
    }

    @Override
    public void onDisable() {
     getScoreBoardManager().onDisable();
        logger.info("#====={------------------------------}=====#");
        logger.info("#====={   HyriLobby is now disabled  }=====#");
        logger.info("#====={    Thanks using HyriLobby !  }=====#");
        logger.info("#====={------------------------------}=====#");
    }
    private void registerManagers() {
        scoreBoardManager = new ScoreBoardManager(this);
    }
    private void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);
    }
    private void registerCommands() {

    }
    private void fastCmd(String command, CommandExecutor executor) {
        getCommand(command).setExecutor(executor);
        logger.info("Registered command: " +command);
    }
    public HyriAPI getApi() {
        return api;
    }

    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public ScoreBoardManager getScoreBoardManager() {
        return scoreBoardManager;
    }
}
