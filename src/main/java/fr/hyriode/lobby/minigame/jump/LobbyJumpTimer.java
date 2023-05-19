package fr.hyriode.lobby.minigame.jump;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Project: Hyriode-Development
 * Created by Akkashi
 * on 07/06/2022 at 17:23
 */
public class LobbyJumpTimer extends BukkitRunnable {

    private long currentTime = 0;
    private Consumer<Long> onChanged;

    @Override
    public void run() {
        this.currentTime++;

        if (this.onChanged != null) {
            this.onChanged.accept(this.currentTime);
        }
    }

    public void onChanged(Consumer<Long> onChanged) {
        this.onChanged = onChanged;
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

}
