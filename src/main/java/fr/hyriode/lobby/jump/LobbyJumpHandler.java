package fr.hyriode.lobby.jump;

import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.hyrame.actionbar.ActionBar;
import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.language.LobbyMessage;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi & Calyx
 * on 18/05/2022 at 16:30 & 11/04/2023 at 23:28
 */
public class LobbyJumpHandler extends HyriListener<HyriLobby> {

    public LobbyJumpHandler(HyriLobby plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());
        final Location location = event.getPlayer().getLocation();

        if (this.checkLocation(location, this.plugin.config().getJumpStart().asBukkit())) {
            if (!lobbyPlayer.hasJump()) {
                if (IHyriPlayerSession.get(player.getUniqueId()).isModerating()) {
                    return;
                }

                lobbyPlayer.startJump();
                this.playStartSound(lobbyPlayer.asPlayer());
            } else {
                if (lobbyPlayer.getJump().getTimer().getCurrentTime() >= 3) {
                    lobbyPlayer.resetTimer();
                    this.playCheckPointSound(lobbyPlayer.asPlayer());
                }
            }
        }

        if (lobbyPlayer.hasJump()) {
            for (LocationWrapper checkpoint : this.plugin.config().getCheckpoints()) {
                if (this.checkLocation(location, checkpoint.asBukkit()) &&
                        !this.checkLocation(location, lobbyPlayer.getJump().getActualCheckPoint().getLocation()) &&
                        this.checkLocation(location, lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()) + 1).getLocation())
                ) {
                    lobbyPlayer.getJump().setActualCheckPoint(lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()) + 1));
                    player.sendMessage(LobbyMessage.JUMP_SUCCESS_CHECKPOINT_MESSAGE.asString(player)
                            .replace("%value%", String.valueOf(lobbyPlayer.getJump().getCheckPoints().indexOf(lobbyPlayer.getJump().getActualCheckPoint()))));
                    this.playCheckPointSound(lobbyPlayer.asPlayer());
                    break;
                }
            }
        }

        if (this.checkLocation(location, this.plugin.config().getJumpEnd().asBukkit()) && lobbyPlayer.hasJump() && lobbyPlayer.getJump().getActualCheckPoint().equals(lobbyPlayer.getJump().getCheckPoints().get(lobbyPlayer.getJump().getCheckPoints().size() - 1))) {
            lobbyPlayer.endJump();
            this.playEndSound(player);
        }

        if (lobbyPlayer.hasJump()) {
            if (location.getBlockY() <= lobbyPlayer.getJump().getActualCheckPoint().getYTeleport()) {
                player.teleport(lobbyPlayer.getJump().getActualCheckPoint().getLocation());
                new ActionBar(LobbyMessage.JUMP_RESPAWN_BAR.asString(player)).send(player);
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
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 3f, 2.0f);
    }

    private void playEndSound(Player player) {
        for (int i = 0; i < 5; i++) {
            float volume = 0.5F + i * 0.2F;
            int ticks = i * 5;
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1, volume), ticks);
        }
    }
}
