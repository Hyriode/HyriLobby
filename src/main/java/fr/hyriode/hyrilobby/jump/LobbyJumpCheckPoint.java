package fr.hyriode.hyrilobby.jump;

import org.bukkit.Location;

import java.time.LocalDate;

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
