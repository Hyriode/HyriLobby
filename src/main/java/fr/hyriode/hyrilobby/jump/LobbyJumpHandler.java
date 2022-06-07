package fr.hyriode.hyrilobby.jump;

import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.language.LobbyMessage;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 18/05/2022 at 16:30
 */
public class LobbyJumpHandler extends HyriListener<HyriLobby> {

    public LobbyJumpHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayer().getUniqueId());
        final Location location = event.getPlayer().getLocation();

        if (this.checkLocation(location, this.plugin.getConfiguration().getJumpStart().asBukkit())) {
            if (!lobbyPlayer.hasJump()) {
                lobbyPlayer.startJump();
                this.playStartSound(lobbyPlayer.asPlayer());
            } else {
                if (lobbyPlayer.getJump().getTimer().getCurrentTime() >= 5) {
                    lobbyPlayer.resetTimer();
                    this.playCheckPointSound(lobbyPlayer.asPlayer());
                }
            }
        }

        if (lobbyPlayer.hasJump()) {
            for (LocationWrapper checkpoint : this.plugin.getConfiguration().getCheckpoints()) {
                if (this.checkLocation(location, checkpoint.asBukkit()) &&
                        !this.checkLocation(location, lobbyPlayer.getJump().getActualCheckPoint().getLocation()) &&
                        this.checkLocation(location, lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()) + 1).getLocation())
                ) {
                    lobbyPlayer.getJump().setActualCheckPoint(lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()) + 1));
                    lobbyPlayer.asPlayer().sendMessage(lobbyPlayer.getJump().getPrefix(lobbyPlayer.asPlayer()) + LobbyMessage.JUMP_SUCCESS_CHECKPOINT.get().getForPlayer(lobbyPlayer.asPlayer())
                            .replace("%value%", String.valueOf(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()))));
                    this.playCheckPointSound(lobbyPlayer.asPlayer());
                    break;
                }
            }
        }

        if (this.checkLocation(location, this.plugin.getConfiguration().getJumpEnd().asBukkit()) && lobbyPlayer.hasJump() && lobbyPlayer.getJump().getActualCheckPoint().equals(lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().size() - 1))) {
            lobbyPlayer.endJump();
            this.playEndSound(lobbyPlayer.asPlayer());
        }

        if (lobbyPlayer.hasJump()) {
            if (location.getBlockY() <= lobbyPlayer.getJump().getActualCheckPoint().getYTeleport()) {
                lobbyPlayer.asPlayer().teleport(lobbyPlayer.getJump().getActualCheckPoint().getLocation());
                new ActionBar(LobbyMessage.JUMP_RESPAWN_BAR.get().getForPlayer(lobbyPlayer.asPlayer())).send(lobbyPlayer.asPlayer());
            }
        }
    }

    private boolean checkLocation(Location location1, Location location2) {
        double loc1x = location1.getBlockX();
        double loc1y = location1.getBlockY();
        double loc1z = location1.getBlockZ();

        return loc1x == location2.getBlockX() &&
                loc1y == location2.getBlockY() &&
                loc1z == location2.getBlockZ();
    }

    private void playStartSound(Player player) {
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 3f, 1f);
    }

    private void playCheckPointSound(Player player) {
        player.playSound(player.getLocation(), Sound.CLICK, 3f, 1f);
    }

    private void playEndSound(Player player) {
        for (int i = 0; i < 5; i++) {
            float volume = 0.5F + i * 0.2F;
            int ticks = i * 5;
            Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, volume);
                }
            }, ticks);
        }
    }
}
