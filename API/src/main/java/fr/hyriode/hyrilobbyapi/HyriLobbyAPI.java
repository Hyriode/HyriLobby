package fr.hyriode.hyrilobbyapi;

import fr.hyriode.hyrilobbyapi.packet.HyriLobbyPacketManager;
import fr.hyriode.hyrilobbyapi.player.HyriLobbyPlayerManager;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 00:16
 */
public class HyriLobbyAPI {

    public static final String LOBBY_DATA_KEY = "hyrilobby";

    private static HyriLobbyAPI INSTANCE;

    private final HyriLobbyPlayerManager playerManager;

    private final HyriLobbyPacketManager packetManager;

    public HyriLobbyAPI() {
        INSTANCE = this;

        this.playerManager = new HyriLobbyPlayerManager();
        this.packetManager = new HyriLobbyPacketManager();
    }

    public HyriLobbyPlayerManager getPlayerManager() {
        return playerManager;
    }

    public HyriLobbyPacketManager getPacketManager() {
        return packetManager;
    }

    public static HyriLobbyAPI get() {
        return INSTANCE;
    }
}
