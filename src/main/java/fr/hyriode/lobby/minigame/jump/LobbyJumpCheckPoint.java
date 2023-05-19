package fr.hyriode.lobby.minigame.jump;

import org.bukkit.Location;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 18/05/2022 at 15:52
 */
public class LobbyJumpCheckPoint {

    private final Location location;
    private final double yTeleport;

    public LobbyJumpCheckPoint(Location location, double yTeleport) {
        this.location = location;
        this.yTeleport = yTeleport;
    }

    public Location getLocation() {
        return location;
    }

    public double getYTeleport() {
        return yTeleport;
    }
}
