package fr.hyriode.lobby;

import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyrame.language.IHyriLanguageManager;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.lobby.listener.PlayerHandler;
import fr.hyriode.lobby.player.PlayerManager;
import fr.hyriode.lobby.scoreboard.ScoreboardManager;
import fr.hyriode.tools.inventory.InventoryHandler;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class HyriLobby extends JavaPlugin {

    /** Logger */
    private final Logger logger = getLogger();

    /** Scoreboard */
    private ScoreboardManager scoreboardManager;

    /** API */
    private HyriAPI api;

    /** Hyrame */
    private IHyrame hyrame;

    /** Language Manager*/
    private IHyriLanguageManager languageManager;

    @Override
    public void onEnable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={     Welcome to HyriLobby     }=====#");
        this.logger.info("#====={      The plugin is now       }=====#");
        this.logger.info("#====={           loading...         }=====#");
        this.logger.info("#====={  Authors: Akkashi, AstFaster }=====#");
        this.logger.info("#====={------------------------------}=====#");

        this.api = HyriAPI.get();
        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));
        this.languageManager = this.hyrame.getLanguageManager();

        this.registerManagers();
        this.registerCommands();
        this.registerListeners();

        new InventoryHandler(this);
        new PlayerHandler(this);

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

    public IHyrame getHyrame() {
        return this.hyrame;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

}
