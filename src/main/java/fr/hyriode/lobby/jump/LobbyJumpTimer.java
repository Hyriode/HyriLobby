package fr.hyriode.lobby.jump;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Project: Hyriode-Development
 * Created by Akkashi
 * on 07/06/2022 at 17:23
 */
public class LobbyJumpTimer extends BukkitRunnable {

    private long currentTime = 0;
    private final List<Consumer<Long>> timeChangedActions = new ArrayList<>();

    @Override
    public void run() {
        this.currentTime++;

        this.timeChangedActions.forEach(consumer -> consumer.accept(this.currentTime));
    }

    public void setOnTimeChanged(Consumer<Long> timeChanged) {
        this.timeChangedActions.add(timeChanged);
    }

    public List<Consumer<Long>> getTimeChangedActions() {
        return timeChangedActions;
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
