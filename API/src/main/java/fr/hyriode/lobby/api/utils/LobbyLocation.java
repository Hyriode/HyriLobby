package fr.hyriode.lobby.api.utils;

public class LobbyLocation {

    private int x;
    private int y;
    private int z;

    public LobbyLocation() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public LobbyLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void setZ(int z) {
        this.z = z;
    }
    
    public void setLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static boolean isEquals(LobbyLocation first, LobbyLocation second) {
        return first.getX() == second.getX() && first.getY() == second.getY() && first.getZ() == second.getZ();
    }

    public static String toStringFormat(LobbyLocation lobbyLocation) {
        return lobbyLocation.getX() + ":" + lobbyLocation.getY() + ":" + lobbyLocation.getZ();
    }
}
