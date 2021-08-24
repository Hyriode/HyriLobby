package fr.hyriode.hyrilobby;

import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyrilobby.managers.LobbyManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main INSTANCE;
    private HyriAPI api;
    private LobbyManager lobbyManager;
    private Logger logger;

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
        logger.info("Connecting to the database");
        // Here is the method to connect the database
        logger.info("Database connected !");
        logger.info("#============================#");
        logger.info("Registering managers...");
        // Here is the #registerManagers method
        logger.info("Managers registered !");
        logger.info("#============================#");
        logger.info("Registering commands...");
        // Here is the #registerCommands method
        logger.info("Commands registered !");
        logger.info("#============================#");
        logger.info("Registering listeners...");
        // Here is the #registerListeners method
        logger.info("Listeners registered !");
        logger.info("#============================#");
        logger.info("Registering runnables !");
        // Here is the #registerRunnables method
        logger.info("Runnables registered !");
        logger.info("#====={------------------------------}=====#");
        logger.info("#====={    HyriLobby is now loaded   }=====#");
        logger.info("#====={    Thanks using HyriLobby !  }=====#");
        logger.info("#====={------------------------------}=====#");
    }

    @Override
    public void onDisable() {
        // Not needed for now
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public HyriAPI getApi() {
        return api;
    }
}
