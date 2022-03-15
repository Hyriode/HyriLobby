package fr.hyriode.lobby.jump;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.api.LobbyAPI;
import fr.hyriode.lobby.api.jump.LobbyCheckpoint;
import fr.hyriode.lobby.api.jump.LobbyJump;
import fr.hyriode.lobby.api.jump.LobbyJumpManager;
import fr.hyriode.lobby.api.leaderboard.LobbyLeaderboardManager;
import fr.hyriode.lobby.api.player.LobbyPlayer;
import fr.hyriode.lobby.api.player.LobbyPlayerManager;
import fr.hyriode.lobby.api.utils.LobbyLocation;
import fr.hyriode.lobby.utils.LocationConverter;
import fr.hyriode.lobby.utils.RandomTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class JumpHandler extends HyriListener<HyriLobby> {

    private final LobbyJumpManager jm;
    private final LobbyPlayerManager pm;
    private final LobbyLeaderboardManager lm;

    JumpHandler(HyriLobby plugin) {
        super(plugin);

        this.jm = LobbyAPI.get().getJumpManager();
        this.pm = LobbyAPI.get().getPlayerManager();
        this.lm = LobbyAPI.get().getLeaderboardManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final LobbyPlayer player = this.pm.get(event.getEntity().getUniqueId());

        if (player.getStartedJump() == null) {
            return;
        }
        
        event.getEntity().teleport(LocationConverter.toBukkitLocation(event.getEntity().getWorld(), player.getLastCheckpoint().getLocation()));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Location bukkitLoc = event.getTo();
        final LobbyLocation loc = LocationConverter.toLobbyLocation(bukkitLoc);

        final LobbyJump start = this.jm.getJumpByStart(loc);
        final LobbyJump end = this.jm.getJumpByEnd(loc);
        final LobbyCheckpoint checkpoint = this.jm.getCheckpointByLocation(loc);

        final LobbyPlayer player = this.pm.get(event.getPlayer().getUniqueId());

        //Check if jump start/end or checkpoint exists at this location
        if (start == null && end == null && checkpoint == null) {
            return;
        }

        //Starting the jump
        if (start != null) {
            if (player.getStartedJump() != null || player.getLastCheckpoint() != null) {
                return;
            }

            player.setStartedJump(start.getName());
            //Checkpoint 0 is always the start
            player.setLastCheckpoint(start.getCheckpoints().get(0));

            this.pm.save(player);

            this.jm.addTask(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () ->
                    this.jm.addTimer(player.getUniqueId(), this.jm.getTimers().getOrDefault(player.getUniqueId(), 0) + 1), 0, 20));
            return;
        }

        //Checkpoint reached
        if (checkpoint != null) {
            final RandomTools.HyriDuration duration = new RandomTools.HyriDuration(this.jm.getTimers().get(player.getUniqueId()));

            event.getPlayer().sendMessage(RandomTools.getPrefix(false) + "Congrats ! Checkpoint reached in " + duration.toMinutesPart()
                    + " minutes and " + duration.toSecondsPart() + " seconds !");
            player.setLastCheckpoint(checkpoint);

            this.pm.save(player);
            return;
        }

        //End of jump reached
        final RandomTools.HyriDuration duration = new RandomTools.HyriDuration(this.jm.getTimers().get(player.getUniqueId()));

        event.getPlayer().sendMessage(RandomTools.getPrefix(false) + "Congrats ! Jump "+ end.getName() + " ended in " + duration.toMinutesPart()
                + " minutes and " + duration.toSecondsPart() + " seconds !");

        Bukkit.getScheduler().cancelTask(this.jm.getTaskIds().get(player.getUniqueId()));
        player.setStartedJump(null);
        player.setLastCheckpoint(null);

        this.lm.removeFromLeaderboard(end.getName(), player.getUniqueId().toString());
        this.lm.addToLeaderboard(end.getName(), player.getUniqueId().toString(), duration.toSeconds());

        this.jm.getTaskIds().remove(player.getUniqueId());
        this.jm.getTimers().remove(player.getUniqueId());
    }

    public void stop() {
        this.jm.getTaskIds().values().forEach(id -> Bukkit.getScheduler().cancelTask(id));
    }
}
