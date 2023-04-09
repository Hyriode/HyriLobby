package fr.hyriode.lobby.host;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStartedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerStoppedEvent;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;
import fr.hyriode.hyggdrasil.api.server.HyggServer;
import fr.hyriode.lobby.HyriLobby;
import fr.hyriode.lobby.gui.host.HostGUI;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by AstFaster
 * on 30/07/2022 at 14:23
 */
public class HostHandler {

    public static final int MAX_HOSTS = 20;

    private final List<UUID> players = new ArrayList<>();

    private final HyriLobby plugin;

    public HostHandler(HyriLobby plugin) {
        this.plugin = plugin;
        final HyggEventBus eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();

        eventBus.subscribe(HyggServerStartedEvent.class, event -> this.handleEvent(event.getServer()));
        eventBus.subscribe(HyggServerUpdatedEvent.class, event -> this.handleEvent(event.getServer()));
        eventBus.subscribe(HyggServerStoppedEvent.class, event -> this.handleEvent(event.getServer()));

        new HostListener();
    }

    public boolean isWaiting(UUID player) {
        return this.players.contains(player);
    }

    public void addWaitingPlayer(UUID player) {
        this.players.add(player);
    }

    public void removeWaitingPlayer(UUID player) {
        this.players.remove(player);
    }

    private void handleEvent(HyggServer server) {
        if (server.getAccessibility() != HyggServer.Accessibility.HOST) {
            return;
        }

        for (HostGUI gui : this.plugin.getHyrame().getInventoryManager().getInventories(HostGUI.class)) {
            gui.update();
        }
    }

}
