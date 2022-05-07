package fr.hyriode.hyrilobby.jump;

import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 04/05/2022 at 15:30
 */
public class JumpManager {

    private final HyriLobby plugin;

    public JumpManager(HyriLobby plugin) {
        this.plugin = plugin;
    }

    public void handleStart(LobbyPlayer lobbyPlayer) {
        Jump jump = new Jump(this.plugin);

        lobbyPlayer.setLastCheckpoint(jump.getCheckPoints().get(0));
        lobbyPlayer.setInJump(true);
        lobbyPlayer.setJump(jump);
    }

    public void endJump(LobbyPlayer lobbyPlayer) {
        lobbyPlayer.handleLogin(false);
    }
}
