package fr.hyriode.lobby.listener;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.event.HyriEventHandler;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageUpdatedEvent;
import fr.hyriode.api.leveling.event.NetworkLevelEvent;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.ModerationUpdatedEvent;
import fr.hyriode.api.player.event.NicknameUpdatedEvent;
import fr.hyriode.api.player.event.RankUpdatedEvent;
import fr.hyriode.api.player.event.VanishUpdatedEvent;
import fr.hyriode.api.player.model.IHyriNickname;
import fr.hyriode.hyrame.npc.NPC;
import fr.hyriode.hyrame.npc.NPCManager;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.game.host.HostWaitingAnimation;
import fr.hyriode.lobby.ui.npc.LobbyNPCHandler;
import fr.hyriode.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;


/**
 * Created by AstFaster
 * on 29/06/2022 at 15:19
 */
public class AccountListener {

    private final HyriLobby plugin;

    public AccountListener(HyriLobby plugin) {
        this.plugin = plugin;

        HyriAPI.get().getNetworkManager().getEventBus().register(this);
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

            this.plugin.getHologramManager().getEnglishHolograms().forEach(hologram -> hologram.removeReceiver(player));
            this.plugin.getHologramManager().getFrenchHolograms().forEach(hologram -> hologram.removeReceiver(player));

            if(event.getLanguage() == HyriLanguage.FR) {
                this.plugin.getHologramManager().getFrenchHolograms().forEach(hologram -> hologram.addReceiver(player));
            } else {
                this.plugin.getHologramManager().getEnglishHolograms().forEach(hologram -> hologram.addReceiver(player));
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

            lobbyPlayer.leaveJump0();
            lobbyPlayer.setInPvP(false);

            final HostWaitingAnimation hostAnimation = HostWaitingAnimation.ANIMATIONS.remove(lobbyPlayer.getUniqueId());

            if (hostAnimation != null) {
                hostAnimation.stop();
            }
        }
    }

    @HyriEventHandler
    public void onVanish(VanishUpdatedEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayerId());

        if (lobbyPlayer == null) {
            return;
        }

        if (event.isVanished()) {
            lobbyPlayer.initStatusBar(event.getSession());

            if (lobbyPlayer.isInPvp()) {
                lobbyPlayer.handleLogin(false, true);
                lobbyPlayer.setInPvP(false);
            }
        } else {
            lobbyPlayer.initPlayersVisibility(lobbyPlayer.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), false);

            lobbyPlayer.removeStatusBar(event.getSession());
        }
    }

    @HyriEventHandler
    public void onNicknameUpdated(NicknameUpdatedEvent event) {
        final UUID playerId = event.getPlayerId();
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(playerId);

        if (lobbyPlayer == null) {
            return;
        }

        final IHyriNickname nickname = event.getNickname();

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final LobbyPlayer newLobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(playerId);

            if (newLobbyPlayer == null) {
                return;
            }

            if (!newLobbyPlayer.hasJump() && !newLobbyPlayer.isInPvp()) {
                newLobbyPlayer.giveDefaultItems();
            }

            if (nickname.has()) {
                newLobbyPlayer.initStatusBar(IHyriPlayerSession.get(playerId));
            } else {
                newLobbyPlayer.removeStatusBar(IHyriPlayerSession.get(playerId));
            }
        }, 2L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            final LobbyPlayer target = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            target.initPlayersVisibility(target.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), false);
        }
    }

    @HyriEventHandler
    public void onRankUpdated(RankUpdatedEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(player.getUniqueId());

            lobbyPlayer.initPlayersVisibility(lobbyPlayer.asHyriPlayer().getSettings().getPlayersVisibilityLevel(), false);
        }
    }

    @HyriEventHandler
    public void onLevelUp(NetworkLevelEvent event) {
        final LobbyPlayer lobbyPlayer = this.plugin.getPlayerManager().getLobbyPlayer(event.getPlayerId());

        if (lobbyPlayer == null) {
            return;
        }

        lobbyPlayer.asPlayer().setExhaustion(lobbyPlayer.getExp(lobbyPlayer.asHyriPlayer().getNetworkLeveling()));
    }

}
