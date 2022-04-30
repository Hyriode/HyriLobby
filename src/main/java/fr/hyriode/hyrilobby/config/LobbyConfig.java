package fr.hyriode.hyrilobby.config;

import fr.hyriode.hyrame.utils.Area;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hystia.api.config.IConfig;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 26/04/2022 at 17:49
 */
public class LobbyConfig implements IConfig {

    private final LocationWrapper spawnLocation;
    private final LocationWrapper vipLocation;
    private final LocationWrapper jumpLocation;

    private final Zone vipEntry;
    private final Zone pvpZone;

    public LobbyConfig(LocationWrapper spawnLocation, LocationWrapper vipLocation, LocationWrapper jumpLocation, Zone vipEntry, Zone pvpZone) {
        this.spawnLocation = spawnLocation;
        this.vipLocation = vipLocation;
        this.jumpLocation = jumpLocation;
        this.vipEntry = vipEntry;
        this.pvpZone = pvpZone;
    }

    public LocationWrapper getSpawnLocation() {
        return this.spawnLocation;
    }

    public LocationWrapper getVipLocation() {
        return this.vipLocation;
    }

    public LocationWrapper getJumpLocation() {
        return this.jumpLocation;
    }

    public Zone getPvpZone() {
        return this.pvpZone;
    }

    public Zone getVipEntry() {
        return this.vipEntry;
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
