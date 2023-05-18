package fr.hyriode.lobby.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.server.event.LobbyRestartingEvent;
import fr.hyriode.lobby.language.LobbyMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by AstFaster
 * on 17/04/2023 at 10:12
 */
public class InternalListener {

    @HyriEventHandler
    public void onLobbyRestarting(LobbyRestartingEvent event) {
        if (HyriAPI.get().getServer().getName().equals(event.getLobby())) {
            final int count = event.getCount();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(LobbyMessage.LOBBY_RESTARTING_MESSAGE.asString(player)
                        .replace("%count%", String.valueOf(count)));
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 2.0F, 0.5F);
            }
        }
    }

}
