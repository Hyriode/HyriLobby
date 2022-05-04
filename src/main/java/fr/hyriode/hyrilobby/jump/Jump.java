package fr.hyriode.hyrilobby.jump;

import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hyrilobby.HyriLobby;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 04/05/2022 at 13:31
 */
public class Jump {
    
    private final HyriLobby plugin;
    
    private final LocationWrapper start;
    private final List<CheckPoint> checkPoints;
    private final LocationWrapper end;

    public Jump(HyriLobby plugin) {
        this.plugin = plugin;
        
        this.start = this.plugin.getConfiguration().getJumpStart();
        this.checkPoints = new ArrayList<>();
        
        this.end = this.plugin.getConfiguration().getJumpEnd();
        
        this.setupCheckPoints();
    }

    private void setupCheckPoints() {
        this.checkPoints.add(new CheckPoint(0, this.plugin.getConfiguration().getJumpStart(), 183));
        this.checkPoints.add(new CheckPoint(1, this.plugin.getConfiguration().getCheckpoints().get(0), 183));
        this.checkPoints.add(new CheckPoint(2, this.plugin.getConfiguration().getCheckpoints().get(1), 197));
        this.checkPoints.add(new CheckPoint(3, this.plugin.getConfiguration().getCheckpoints().get(2), 217));
        this.checkPoints.add(new CheckPoint(4, this.plugin.getConfiguration().getCheckpoints().get(3), 233));
        this.checkPoints.add(new CheckPoint(5, this.plugin.getConfiguration().getCheckpoints().get(4), 219));
        this.checkPoints.add(new CheckPoint(6, this.plugin.getConfiguration().getCheckpoints().get(5), 225));
        this.checkPoints.add(new CheckPoint(7, this.plugin.getConfiguration().getCheckpoints().get(6), 227));
    }

    public LocationWrapper getStart() {
        return start;
    }

    public List<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public LocationWrapper getEnd() {
        return end;
    }

    public static class CheckPoint {

        private final int number;
        private final LocationWrapper location;
        private final double yPos;

        public CheckPoint(int number, LocationWrapper location, double yPos) {
            this.number = number;
            this.location = location;
            this.yPos = yPos;
        }

        public int getNumber() {
            return number;
        }

        public LocationWrapper getLocation() {
            return location;
        }

        public double getYPos() {
            return yPos;
        }
    }
}
