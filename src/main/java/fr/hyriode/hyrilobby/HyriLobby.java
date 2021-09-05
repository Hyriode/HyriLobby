package fr.hyriode.hyrilobby;

import fr.hyriode.common.inventory.InventoryHandler;
import fr.hyriode.common.item.ItemHandler;
import fr.hyriode.hyrame.Hyrame;
import fr.hyriode.hyrame.language.LanguageManager;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyrilobby.listener.PlayerHandler;
import fr.hyriode.hyrilobby.player.PlayerManager;
import fr.hyriode.hyrilobby.scoreboard.ScoreboardManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class HyriLobby extends JavaPlugin {

    private static HyriLobby instance;

    /** Logger */
    private final Logger logger = getLogger();

    /** Scoreboard */
    private ScoreboardManager scoreboardManager;

    /** API */
    private HyriAPI api;

    /** Hyrame */
    private Hyrame hyrame;

    /** Language Manager*/
    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={     Welcome to HyriLobby     }=====#");
        this.logger.info("#====={      The plugin is now       }=====#");
        this.logger.info("#====={           loading...         }=====#");
        this.logger.info("#====={  Authors: Akkashi, AstFaster }=====#");
        this.logger.info("#====={------------------------------}=====#");

        instance = this;
        this.api = HyriAPI.get();
        this.hyrame = new Hyrame(new HyriLobbyProvider(this));
        this.languageManager = new LanguageManager(this.hyrame);

        this.registerManagers();
        this.registerCommands();
        this.registerListeners();

        new ItemHandler(this);
        new InventoryHandler(this);
        new PlayerHandler(this, this);

    }

    @Override
    public void onDisable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={   HyriLobby is now disabled  }=====#");
        this.logger.info("#====={    Thanks using HyriLobby !  }=====#");
        this.logger.info("#====={------------------------------}=====#");
        PlayerManager.onDisable();
    }

    private void registerManagers() {
       this.scoreboardManager = new ScoreboardManager(this);
    }

    private void registerListeners() {

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

    public Hyrame getHyrame() {
        return this.hyrame;
    }

    public static HyriLobby getInstance() {
        return instance;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

}
