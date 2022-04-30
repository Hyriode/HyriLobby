package fr.hyriode.hyrilobbyapi.packet;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 25/04/2022 at 00:46
 */
public class HyriLobbyPacket extends HyriPacket {

    private final String name;

    public HyriLobbyPacket(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return HyriAPI.GSON.toJson(this);
    }
}
