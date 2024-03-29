package fr.hyriode.lobby.config;

import fr.hyriode.api.config.IHyriConfig;
import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.LocationWrapper;

import java.util.List;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 26/04/2022 at 17:49
 */
public class LobbyConfig implements IHyriConfig {

    private final LocationWrapper spawnLocation;
    private final LocationWrapper vipLocation;
    private final LocationWrapper jumpLocation;
    private final LocationWrapper pvpLocation;

    private final LocationWrapper jumpStart;
    private final List<LocationWrapper> checkpoints;
    private final LocationWrapper jumpEnd;

    private final Zone vipZone;
    private final Zone pvpZone;

    public LobbyConfig(LocationWrapper spawnLocation, LocationWrapper vipLocation, LocationWrapper jumpLocation, LocationWrapper pvpLocation, LocationWrapper jumpStart, List<LocationWrapper> checkpoints, LocationWrapper jumpEnd, Zone vipZone, Zone pvpZone) {
        this.spawnLocation = spawnLocation;
        this.vipLocation = vipLocation;
        this.jumpLocation = jumpLocation;
        this.pvpLocation = pvpLocation;
        this.jumpStart = jumpStart;
        this.checkpoints = checkpoints;
        this.jumpEnd = jumpEnd;
        this.vipZone = vipZone;
        this.pvpZone = pvpZone;
    }

    public LocationWrapper getSpawnLocation() {
        return this.spawnLocation;
    }

    public LocationWrapper getVIPLocation() {
        return this.vipLocation;
    }

    public LocationWrapper getJumpLocation() {
        return this.jumpLocation;
    }

    public LocationWrapper getJumpStart() {
        return jumpStart;
    }

    public List<LocationWrapper> getCheckpoints() {
        return checkpoints;
    }

    public LocationWrapper getJumpEnd() {
        return jumpEnd;
    }

    public Zone getPvpZone() {
        return this.pvpZone;
    }

    public Zone getVIPZone() {
        return this.vipZone;
    }

    public LocationWrapper getPvpLocation() {
        return pvpLocation;
    }

    public static class Zone {

        private final LocationWrapper first;
        private final LocationWrapper second;

        public Zone(LocationWrapper first, LocationWrapper second) {
            this.first = first;
            this.second = second;
        }

        public LocationWrapper getFirst() {
            return this.first;
        }

        public LocationWrapper getSecond() {
            return this.second;
        }

        public Area asArea() {
            return new Area(first.asBukkit(), second.asBukkit());
        }

    }
}
