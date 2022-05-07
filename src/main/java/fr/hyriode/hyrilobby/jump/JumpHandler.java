package fr.hyriode.hyrilobby.jump;

import fr.hyriode.hyrame.listener.HyriListener;
import fr.hyriode.hyrame.utils.LocationWrapper;
import fr.hyriode.hyrilobby.HyriLobby;
import fr.hyriode.hyrilobby.player.LobbyPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Project: HyriLobby
 * Created by Akkashi
 * on 03/05/2022 at 20:10
 */
public class JumpHandler extends HyriListener<HyriLobby> {

    public JumpHandler(HyriLobby plugin) {
        super(plugin);
    }

    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

        if(lobbyPlayer.isInJump()) {

            if(player.getLocation().getY() <= lobbyPlayer.getLastCheckpoint().getYPos()) {
                player.teleport(lobbyPlayer.getLastCheckpoint().getLocation().asBukkit());
            }

            if(player.getLocation().getBlock().getType() == Material.IRON_PLATE) {
                for (Jump.CheckPoint checkPoint : lobbyPlayer.getJump().getCheckPoints()) {
                    if(player.getLocation().equals(checkPoint.getLocation().asBukkit())) {
                        lobbyPlayer.setLastCheckpoint(checkPoint);
                    }
                }
            }

            if(player.getLocation().getBlock().getType() == Material.GOLD_PLATE) {
                if(player.getLocation().equals(this.plugin.getConfiguration().getJumpEnd().asBukkit())) {
                    this.plugin.getJumpManager().endJump(lobbyPlayer);
                }
            }

        }
    }
}
