package fr.hyriode.lobby;

import com.google.gson.Gson;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.listener.PlayerHandler;
import fr.hyriode.lobby.player.PlayerManager;
import fr.hyriode.lobby.scoreboard.ScoreboardManager;
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

    /** Lobby API */
    private LobbyAPI lobbyAPI;

    @Override
    public void onEnable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={   HyriLobby is starting...   }=====#");
        this.logger.info("#====={  Authors: Akkashi, AstFaster,}=====#");
        this.logger.info("#====={------------------------------}=====#");

        this.api = HyriAPI.get();
        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));
        this.lobbyAPI = new LobbyAPI(new Gson(), this.api.getRedisResource());

        this.scoreboardManager = new ScoreboardManager(this);

        new PlayerHandler(this);
    }

    @Override
    public void onDisable() {
        this.logger.info("#====={------------------------------}=====#");
        this.logger.info("#====={   HyriLobby is now disabled  }=====#");
        this.logger.info("#====={------------------------------}=====#");

        PlayerManager.onDisable();
    }

    public HyriAPI getAPI() {
        return this.api;
    }

    public IHyrame getHyrame() {
        return this.hyrame;
    }

    public LobbyAPI getLobbyAPI() {
        return this.lobbyAPI;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

}
