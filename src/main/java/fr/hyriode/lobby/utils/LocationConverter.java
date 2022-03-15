package fr.hyriode.lobby.utils;

import fr.hyriode.lobby.api.utils.LobbyLocation;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationConverter {

    public static Location toBukkitLocation(World world, LobbyLocation lobbyLocation) {
        return new Location(world, lobbyLocation.getX(), lobbyLocation.getY(), lobbyLocation.getZ());
    }

    public static LobbyLocation toLobbyLocation(Location location) {
        return new LobbyLocation((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }
}
