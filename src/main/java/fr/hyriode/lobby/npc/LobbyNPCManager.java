package fr.hyriode.lobby.npc;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.model.GameNPCHandler;
import fr.hyriode.lobby.npc.model.HostNPCHandler;
import fr.hyriode.lobby.npc.model.LanguageNPCHandler;
import fr.hyriode.lobby.npc.model.RotatingGameNPCHandler;
import fr.hyriode.lobby.quest.AQuest;
import fr.hyriode.lobby.quest.TestQuest;
import fr.hyriode.lobby.vip.casino.game.zzs.ZeroZeroSevenNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by AstFaster
 * on 27/06/2022 at 14:54
 */
public class LobbyNPCManager {

    private final List<LobbyNPCHandler> npcHandlers;

    public LobbyNPCManager(HyriLobby plugin) {
        this.npcHandlers = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(new Handler(), plugin);

        this.registerHandler(new GameNPCHandler(plugin));
        this.registerHandler(new LanguageNPCHandler(plugin));
        this.registerHandler(new RotatingGameNPCHandler(plugin));
        this.registerHandler(new HostNPCHandler(plugin));

        this.registerHandler(new ZeroZeroSevenNPC(plugin));

        this.registerQuests(plugin);
    }

    private void registerQuests(HyriLobby plugin) {
        final Set<AQuest> questSet = new HashSet<>();
        questSet.add(new TestQuest(plugin));

        for (AQuest quest : questSet) {
            this.registerHandler(quest);
        }
    }

    public <T extends LobbyNPCHandler> T registerHandler(T npcHandler) {
        this.npcHandlers.add(npcHandler);
        return npcHandler;
    }

    public List<LobbyNPCHandler> getNPCHandlers() {
        return this.npcHandlers;
    }

    private class Handler implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            final Player player = event.getPlayer();

            for (LobbyNPCHandler npcHandler : npcHandlers) {
                npcHandler.onLogin(player);
            }
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            final Player player = event.getPlayer();

            for (LobbyNPCHandler npcHandler : npcHandlers) {
                npcHandler.onLogout(player);
            }
        }

    }
}
