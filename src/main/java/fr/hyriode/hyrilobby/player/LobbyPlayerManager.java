package fr.hyriode.hyrilobby.player;

import fr.hyriode.hyrilobby.HyriLobby;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 24/04/2022 at 21:35
 */
public class LobbyPlayerManager {

    private final HyriLobby plugin;

    private final Set<LobbyPlayer> players;

    public LobbyPlayerManager(HyriLobby plugin) {
        this.plugin = plugin;
        this.players = new HashSet<>();
    }

    public LobbyPlayer getLobbyPlayer(UUID uuid) {
        return this.players.stream().filter(lobbyPlayer -> lobbyPlayer.getUniqueId().compareTo(uuid) == 0).findFirst().orElse(null);
    }

    public void handleLogin(UUID uuid) {
        final LobbyPlayer lobbyPlayer = new LobbyPlayer(uuid, this.plugin);

        this.players.add(lobbyPlayer);

        lobbyPlayer.handleLogin(true, true);
    }

    public void handleLogout(UUID uuid) {
        final LobbyPlayer lobbyPlayer = this.getLobbyPlayer(uuid);

        lobbyPlayer.handleLogout();
        this.players.remove(lobbyPlayer);
    }

    public void handleStop() {
        this.players.forEach(LobbyPlayer::handleLogout);
    }
}
