package fr.hyriode.lobby.jump;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.DurationConverter;
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
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.function.Supplier;

public class JumpHandler extends HyriListener<HyriLobby> {

    private final Supplier<LobbyJumpManager> jm;
    private final Supplier<LobbyPlayerManager> pm;
    private final Supplier<LobbyLeaderboardManager> lm;

    public JumpHandler(HyriLobby plugin) {
        super(plugin);

        this.jm = () -> LobbyAPI.get().getJumpManager();
        this.pm = () -> LobbyAPI.get().getPlayerManager();
        this.lm = () -> LobbyAPI.get().getLeaderboardManager();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        event.getEntity().spigot().respawn();

        final LobbyPlayer player = this.pm.get().get(event.getEntity().getUniqueId());

        if (player.getStartedJump() == null || player.getLastCheckpoint() == -1) {
            return;
        }

        final LobbyJump jump = this.jm.get().get(player.getStartedJump());
        final LobbyCheckpoint nextCheckpoint = jump.getCheckpoints().size() < player.getLastCheckpoint() + 1 ? jump.getCheckpoints().get(player.getLastCheckpoint() + 1) : null;
        final Location loc = LocationConverter.toBukkitLocation(jump.getCheckpoints().get(player.getLastCheckpoint()).getLocation());

        final Location nextLoc = LocationConverter.toBukkitLocation(nextCheckpoint != null ? nextCheckpoint.getLocation() : jump.getEnd());
        nextLoc.setY(nextLoc.getY() - 1);

        final Vector vector = nextLoc.toVector().subtract(loc.toVector());

        loc.setDirection(vector);
        event.getEntity().teleport(loc);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Location bukkitLoc = event.getTo();
        final LobbyLocation loc = LocationConverter.toLobbyLocation(bukkitLoc);

        final LobbyJump start = this.jm.get().getJumpByStart(loc);
        final LobbyJump end = this.jm.get().getJumpByEnd(loc);
        final LobbyCheckpoint checkpoint = this.jm.get().getCheckpointByLocation(loc);

        final LobbyPlayer player = this.pm.get().get(event.getPlayer().getUniqueId());

        //Check if jump start/end or checkpoint exists at this location
        if (start == null && end == null && checkpoint == null) {
            return;
        }

        //Starting the jump
        if (start != null && checkpoint != null && end == null) {
            if (player.getStartedJump() != null || player.getLastCheckpoint() != -1) {
                return;
            }

            event.getPlayer().sendMessage(RandomTools.getPrefix(false) + "You started the jump " + start.getName() + " !");

            player.setStartedJump(start.getName());
            //Checkpoint 0 is always the start
            player.setLastCheckpoint(0);

            this.pm.get().save(player);

            this.jm.get().addTimer(player.getUniqueId(), 0);

            this.jm.get().addTask(player.getUniqueId(), Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () ->
                    this.jm.get().increaseTimer(player.getUniqueId(), 1), 0, 20));
            return;
        }

        //Checkpoint reached
        if (checkpoint != null && start == null && end == null) {
            if (player.getStartedJump() == null || player.getLastCheckpoint() == -1) {
                return;
            }

            if (!player.getStartedJump().equalsIgnoreCase(checkpoint.getJumpName())) {
                return;
            }

            if (player.getLastCheckpoint() >= checkpoint.getId()) {
                return;
            }

            final DurationConverter duration = new DurationConverter(Duration.ofSeconds(this.jm.get().getTimers().get(player.getUniqueId())));

            event.getPlayer().sendMessage(RandomTools.getPrefix(false) + "Congrats ! Checkpoint reached in "
                    + RandomTools.getDurationMessage(duration, player.getUniqueId()) + " !");

            player.setLastCheckpoint(checkpoint.getId());

            this.pm.get().save(player);
            return;
        }

        //End of jump reached
        if (end != null && start == null && checkpoint == null) {
            if (player.getStartedJump() == null || player.getLastCheckpoint() == -1) {
                return;
            }

            if (!player.getStartedJump().equalsIgnoreCase(end.getName())) {
                return;
            }

            final DurationConverter duration = new DurationConverter(Duration.ofSeconds(this.jm.get().getTimers().getOrDefault(player.getUniqueId(), 0)));

            event.getPlayer().sendMessage(RandomTools.getPrefix(false) + "Congrats ! Jump " + end.getName() + " ended in "
                    + RandomTools.getDurationMessage(duration, player.getUniqueId()) + " !");

            if (this.jm.get().getTaskIds().get(player.getUniqueId()) != null) {
                Bukkit.getScheduler().cancelTask(this.jm.get().getTaskIds().get(player.getUniqueId()));
            }

            this.lm.get().removeFromLeaderboard(end.getName(), player);
            this.lm.get().addToLeaderboard(end.getName(), player, duration.toSeconds());

            this.jm.get().getTaskIds().remove(player.getUniqueId());
            this.jm.get().getTimers().remove(player.getUniqueId());

            player.setStartedJump(null);
            player.setLastCheckpoint(-1);

            if (!player.getFinishedJumps().contains(end.getName())) {
                player.getFinishedJumps().add(end.getName());
                HyriAPI.get().getPlayerManager().getPlayer(player.getUniqueId()).getHyris().add(500);
            }

            this.pm.get().save(player);
        }
    }

    public void stop() {
        this.jm.get().getTaskIds().values().forEach(id -> Bukkit.getScheduler().cancelTask(id));

        this.jm.get().getTimers().keySet().forEach(uuid -> {
            final LobbyPlayer player = this.pm.get().get(uuid);

            player.setStartedJump(null);
            player.setLastCheckpoint(-1);

            this.pm.get().save(player);
        });
    }
}
