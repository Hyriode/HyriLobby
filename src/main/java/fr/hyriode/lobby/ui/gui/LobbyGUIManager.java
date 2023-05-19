package fr.hyriode.lobby.ui.gui;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.hyggdrasil.api.event.HyggEventBus;
import fr.hyriode.hyggdrasil.api.event.model.server.HyggServerUpdatedEvent;
import fr.hyriode.hyrame.IHyrame;
import fr.hyriode.lobby.ui.gui.selector.game.ServerSelectorGUI;

/**
 * Created by AstFaster
 * on 18/05/2023 at 16:40
 */
public class LobbyGUIManager {

    public LobbyGUIManager() {
        this.start();
    }

    private void start() {
        // Server selector GUIs
        final HyggEventBus eventBus = HyriAPI.get().getHyggdrasilManager().getHyggdrasilAPI().getEventBus();

        eventBus.subscribe(HyggServerUpdatedEvent.class, event -> {
            for (ServerSelectorGUI gui : IHyrame.get().getInventoryManager().getInventories(ServerSelectorGUI.class)) {
                gui.update();
            }
        });
    }

}
