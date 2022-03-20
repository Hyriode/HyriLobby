package fr.hyriode.lobby.utils;

import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import org.bukkit.Location;

public class LocationConverter {

    public static Location toBukkitLocation(LobbyLocation lobbyLocation) {
        return new Location(IHyrame.WORLD.get(), lobbyLocation.getX() + 0.5, lobbyLocation.getY(), lobbyLocation.getZ() + 0.5);
    }

    public static LobbyLocation toLobbyLocation(Location location) {
        return new LobbyLocation((int) location.getX(), (int) location.getY(), (int) location.getZ());
    }
}
