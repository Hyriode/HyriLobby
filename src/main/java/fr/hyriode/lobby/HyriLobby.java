package fr.hyriode.lobby;

import com.google.gson.Gson;
import fr.hyriode.hyrame.HyrameLoader;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.listener.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class HyriLobby extends JavaPlugin {

    /** API */
    private HyriAPI api;

    /** Hyrame */
    private IHyrame hyrame;

    /** Lobby API */
    private LobbyAPI lobbyAPI;

    /** Player Handler */
    private PlayerHandler playerHandler;

    @Override
    public void onEnable() {
        this.getLogger().info("#====={------------------------------}=====#");
        this.getLogger().info("#====={   HyriLobby is starting...   }=====#");
        this.getLogger().info("#====={------------------------------}=====#");

        this.api = HyriAPI.get();
        this.hyrame = HyrameLoader.load(new HyriLobbyProvider(this));
        this.lobbyAPI = new LobbyAPI(new Gson(), this.api.getRedisConnection().getPool());

        this.playerHandler = this.hyrame.getListenerManager().getListener(PlayerHandler.class);
    }

    @Override
    public void onDisable() {
        this.getLogger().info("#====={------------------------------}=====#");
        this.getLogger().info("#====={  HyriLobby is now disabled ! }=====#");
        this.getLogger().info("#====={------------------------------}=====#");
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

    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }
}
