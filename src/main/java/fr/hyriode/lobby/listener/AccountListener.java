package fr.hyriode.lobby.listener;

import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.language.HyriLanguageUpdatedEvent;
import fr.hyriode.api.player.event.ModerationUpdatedEvent;
import fr.hyriode.api.player.event.NicknameUpdatedEvent;
import fr.hyriode.api.player.event.RankUpdatedEvent;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.LobbyNPCHandler;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * Created by AstFaster
 * on 29/06/2022 at 15:19
 */
public class AccountListener {

    private final HyriLobby plugin;

    public AccountListener(HyriLobby plugin) {
        this.plugin = plugin;
    }

    @HyriEventHandler
    public void onLanguageUpdated(HyriLanguageUpdatedEvent event) {
        final Player player = Bukkit.getPlayer(event.getPlayerId());

        if (player == null) {
            return;
        }
        
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            for (LobbyNPCHandler handler : this.plugin.getNPCManager().getNPCHandlers()) {
                handler.onLogout(player);
                handler.onLogin(player);
            }

            for (NPC npc : NPCManager.getNPCs()) {
                NPCManager.sendNPC(player, npc);
            }

            lobbyPlayer.getLobbyScoreboard().update(true);
            lobbyPlayer.giveDefaultItems();

            this.plugin.getLeaderboardManager().refreshLeaderboards(player);
        }, 5L);
    }

    @HyriEventHandler
    public void onModeration(ModerationUpdatedEvent event) {
        if (!event.isModerating()) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayerId());

                if (lobbyPlayer == null) {
                    return;
                }

                lobbyPlayer.handleLogin(false, false);
            }, 1L);
        } else {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayerId());

            lobbyPlayer.leaveJump(false);
        }
    }

    @HyriEventHandler
    public void onNicknameUpdated(NicknameUpdatedEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            lobbyPlayer.initPlayersVisibility(lobbyPlayer.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), false);
        }
    }

    @HyriEventHandler
    public void onRankUpdated(RankUpdatedEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            lobbyPlayer.initPlayersVisibility(lobbyPlayer.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), false);
        }
    }

}
