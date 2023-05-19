package fr.hyriode.lobby.ui.npc;

import fr.hyriode.lobby.HyriLobby;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 27/06/2022 at 14:59
 */
public abstract class LobbyNPCHandler {

    protected final HyriLobby plugin;

    public LobbyNPCHandler(HyriLobby plugin) {
        this.plugin = plugin;
    }

    public abstract void disable();

    public void onLogin(Player player) {}

    public void onLogout(Player player) {}

}
