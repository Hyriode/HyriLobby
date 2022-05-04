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
            if(player.getLocation().getBlock().getType() == Material.IRON_PLATE) {

                for (LocationWrapper checkpoint : this.plugin.getConfiguration().getCheckpoints()) {

                    if(player.getLocation().equals(checkpoint.asBukkit())) {
                        this.plugin.getJumpManager().setLastCheckpoint();
                    }
                }
            }


        }
    }
}
