package fr.hyriode.lobby.minigame.jump;

import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.HyriLobby;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 18/05/2022 at 15:49
 */
public class LobbyJump {

    private final LobbyJumpCheckPoint start;
    private final LobbyJumpCheckPoint end;
    private final List<LobbyJumpCheckPoint> checkPoints;

    private final long startTime;
    private LobbyJumpCheckPoint actualCheckPoint;
    private final LobbyJumpTimer timer;
    
    public LobbyJump(HyriLobby plugin) {
        this.start = new LobbyJumpCheckPoint(plugin.config().getJumpStart().asBukkit(), 183);
        this.end = new LobbyJumpCheckPoint(plugin.config().getJumpEnd().asBukkit(), 0);
        this.checkPoints = new ArrayList<>();

        this.setupCheckpoints(plugin);

        this.actualCheckPoint = null;
        this.timer = new LobbyJumpTimer();
        this.timer.runTaskTimerAsynchronously(plugin, 0, 20);

        this.startTime = System.currentTimeMillis();
    }

    private void setupCheckpoints(HyriLobby plugin) {
        final List<LocationWrapper> locations = plugin.config().getCheckpoints();

        this.checkPoints.add(this.start);
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(0).asBukkit(), 188));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(1).asBukkit(), 197));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(2).asBukkit(), 217));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(3).asBukkit(), 233));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(4).asBukkit(), 219));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(5).asBukkit(), 225));
        this.checkPoints.add(new LobbyJumpCheckPoint(locations.get(6).asBukkit(), 227));
    }

    public LobbyJumpCheckPoint getActualCheckPoint() {
        return actualCheckPoint;
    }

    public void setActualCheckPoint(LobbyJumpCheckPoint actualCheckPoint) {
        this.actualCheckPoint = actualCheckPoint;
    }

    public LobbyJumpCheckPoint getStart() {
        return start;
    }

    public LobbyJumpCheckPoint getEnd() {
        return end;
    }

    public List<LobbyJumpCheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public LobbyJumpTimer getTimer() {
        return timer;
    }

    public long getStartTime() {
        return startTime;
    }

}
