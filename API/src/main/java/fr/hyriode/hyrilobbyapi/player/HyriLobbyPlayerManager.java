package fr.hyriode.hyrilobbyapi.player;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.hyrilobbyapi.HyriLobbyAPI;

import java.util.UUID;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 00:21
 */
public class HyriLobbyPlayerManager {

    public HyriLobbyPlayerManager() {
    }

    public HyriLobbyPlayer getLobbyPlayer(UUID uuid) {
        final IHyriPlayer hyriPlayer = HyriAPI.get().getPlayerManager().getPlayer(uuid);
        final HyriLobbyPlayer lobbyPlayer = hyriPlayer.getData(HyriLobbyAPI.LOBBY_DATA_KEY, HyriLobbyPlayer.class);

        if(lobbyPlayer != null) {
            return lobbyPlayer;
        } else {
            hyriPlayer.addData(HyriLobbyAPI.LOBBY_DATA_KEY, new HyriLobbyPlayer());
            hyriPlayer.update();

            this.getLobbyPlayer(uuid);
        }

        return null;
    }

}
