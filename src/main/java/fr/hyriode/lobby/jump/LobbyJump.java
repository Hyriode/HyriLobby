package fr.hyriode.lobby.jump;

import fr.hyriode.hyrame.language.HyriLanguageMessage;
import fr.hyriode.hyrame.utils.Symbols;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

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
        this.start = new LobbyJumpCheckPoint(plugin.getConfiguration().getJumpStart().asBukkit(), 183);
        this.end = new LobbyJumpCheckPoint(plugin.getConfiguration().getJumpEnd().asBukkit(), 0);
        this.checkPoints = new ArrayList<>();

        this.setupCheckpoints(plugin);

        this.actualCheckPoint = null;
        this.timer = new LobbyJumpTimer();
        this.timer.runTaskTimerAsynchronously(plugin, 0, 20);

        this.startTime = System.currentTimeMillis();
    }

    private void setupCheckpoints(HyriLobby plugin) {
        this.checkPoints.add(this.start);
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(0).asBukkit(), 188));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(1).asBukkit(), 197));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(2).asBukkit(), 217));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(3).asBukkit(), 233));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(4).asBukkit(), 219));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(5).asBukkit(), 225));
        this.checkPoints.add(new LobbyJumpCheckPoint(plugin.getConfiguration().getCheckpoints().get(6).asBukkit(), 227));
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

    public String getPrefix(Player player) {
        return HyriLanguageMessage.get("prefix.jump").getForPlayer(player) + ChatColor.WHITE + " " + Symbols.LINE_VERTICAL_BOLD + " ";
    }
}
