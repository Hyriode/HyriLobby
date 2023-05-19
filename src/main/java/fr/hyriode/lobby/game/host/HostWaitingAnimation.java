package fr.hyriode.lobby.game.host;

import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.title.Title;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by AstFaster
 * on 30/07/2022 at 16:55
 */
public class HostWaitingAnimation {

    public static final Map<UUID, HostWaitingAnimation> ANIMATIONS = new HashMap<>();

    private static final List<String> PHASES = Arrays.asList("▇▇▆▅▄▃▂▁▁", "▇▇▇▆▅▄▃▂▁", "▆▇▇▇▆▅▄▃▂", "▅▆▇▇▇▆▅▄▃", "▄▅▆▇▇▇▆▅▄", "▃▄▅▆▇▇▇▆▅", "▂▃▄▅▆▇▇▇▆", "▁▂▃▄▅▆▇▇▇", "▁▁▂▃▄▅▆▇▇");

    private BukkitTask task;
    private ActionBar actionBar;

    private final Player player;

    public HostWaitingAnimation(Player player) {
        this.player = player;

        ANIMATIONS.put(player.getUniqueId(), this);
    }

    public void start(JavaPlugin plugin) {
        this.actionBar = new ActionBar(LobbyMessage.HOST_CREATING_HOST_BAR.asString(this.player));
        this.actionBar.sendPermanent(plugin, this.player);

        this.task = new BukkitRunnable() {

            private int phaseIndex = 0;
            private boolean reversing;

            @Override
            public void run() {
                if (Bukkit.getPlayer(player.getUniqueId()) == null) {
                    this.cancel();
                    return;
                }

                Title.sendTitle(player, "", ChatColor.WHITE + PHASES.get(this.phaseIndex), 0, 20, 0);

                if (this.reversing) {
                    this.phaseIndex--;
                } else {
                    this.phaseIndex++;
                }

                if (this.phaseIndex == PHASES.size() - 1) {
                    this.reversing = true;
                } else if (this.phaseIndex == 0){
                    this.reversing = false;
                }
            }
        }.runTaskTimer(plugin, 3L, 3L);
    }

    public void stop() {
        this.actionBar.remove();
        this.task.cancel();
    }

}
