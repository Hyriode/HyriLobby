package fr.hyriode.lobby.api.utils;

import fr.hyriode.lobby.api.LobbyAPI;

/**
 * Represents a location in the lobby.
 */
public class LobbyLocation {

    /**
     * The x coordinate.
     */
    private final int x;
    /**
     * The y coordinate.
     */
    private final int y;
    /**
     * The z coordinate.
     */
    private final int z;

    /**
     * The constructor of the location.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public LobbyLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the x coordinate.
     * @return The x coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Get the y coordinate.
     * @return The y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Get the z coordinate.
     * @return The z coordinate.
     */
    public int getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return LobbyAPI.GSON.toJson(this);
    }

    /**
     * Check if the given location is equals to the other location.
     * @param first The first location.
     * @param second The second location to compare with.
     * @return <code>true</code> if the two locations are equals, <code>false</code> otherwise.
     */
    public static boolean isEquals(LobbyLocation first, LobbyLocation second) {
        return first.getX() == second.getX() && first.getY() == second.getY() && first.getZ() == second.getZ();
    }

    /**
     * Get the location formatted as a string.
     * @param lobbyLocation The location to format.
     * @return The formatted location.
     */
    public static String toStringFormat(LobbyLocation lobbyLocation) {
        return lobbyLocation.getX() + " " + lobbyLocation.getY() + " " + lobbyLocation.getZ();
    }
}
