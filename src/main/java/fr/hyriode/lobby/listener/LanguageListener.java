package fr.hyriode.lobby.listener;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.hyrame.language.HyriLanguageUpdatedEvent;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.LobbyNPCHandler;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.entity.Player;


/**
 * Created by AstFaster
 * on 29/06/2022 at 15:19
 */
public class LanguageListener {

    private final HyriLobby plugin;

    public LanguageListener(HyriLobby plugin) {
        this.plugin = plugin;
    }

    @HyriEventHandler
    public void onLanguageUpdated(HyriLanguageUpdatedEvent event) {
        final Player player = event.getPlayer();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

        for (LobbyNPCHandler handler : this.plugin.getNPCManager().getNPCHandlers()) {
            handler.onLogout(player);
            handler.onLogin(player);
        }

        lobbyPlayer.getLobbyScoreboard().update();
        lobbyPlayer.giveDefaultItems();

        this.plugin.getLeaderboardManager().refreshLeaderboards(player);
    }

}
