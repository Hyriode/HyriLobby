package fr.hyriode.lobby.npc;

import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.npc.model.GameNPCHandler;
import fr.hyriode.lobby.npc.model.HostNPCHandler;
import fr.hyriode.lobby.npc.model.LanguageNPCHandler;
import fr.hyriode.lobby.npc.model.RotatingGameNPCHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

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
